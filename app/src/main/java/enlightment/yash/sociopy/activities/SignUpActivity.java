package enlightment.yash.sociopy.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import enlightment.yash.sociopy.Helper;
import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.SociopyService;
import enlightment.yash.sociopy.beans.Status;
import enlightment.yash.sociopy.viewmodels.CredentialModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {

    private CredentialModel credentialModel;

    private TextInputEditText fullName, email, password, confirmPassword;
    private MaterialButton signUp;

    @Inject
    public SociopyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectUnsafeIntentLaunch()
                    .penaltyLog()
                    .build());
        }

        credentialModel = new ViewModelProvider(this).get(CredentialModel.class);

        initializeViews();

        signUp.setOnClickListener(v -> {
            try {
                check(v);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                confirmPassword.setTextColor(Color.RED);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = Objects.requireNonNull(password.getText()).toString();
                String confirmValue = s.toString();
                if (!confirmValue.isEmpty() && confirmValue.equals(value)) {
                    confirmPassword.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void check(View v) throws Exception {
        int result = credentialModel.verify(0, Objects.requireNonNull(fullName.getText()).toString(), Objects.requireNonNull(email.getText()).toString(), Objects.requireNonNull(password.getText()).toString(), Objects.requireNonNull(confirmPassword.getText()).toString());
        switch (result) {
            case 1, 4 -> fullName.setError(credentialModel.getMessage());
            case 2, 5 -> email.setError(credentialModel.getMessage());
            case 3 -> password.setError(credentialModel.getMessage());
            case 6 -> confirmPassword.setError(credentialModel.getMessage());
            default -> {
                if (credentialModel.isValid()) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("email", credentialModel.getEmail());
                    map.put("first_name", credentialModel.getFirstName());
                    map.put("last_name", credentialModel.getLastName());
                    map.put("password", credentialModel.getPassword());
                    File file = Helper.getJsonFile(getApplicationContext().getFilesDir() + "register.json", Helper.getJSONObject(map));
                    String zipFilePath = getApplication().getFilesDir() + "/register.7z";
                    if (Helper.create7ZArchive(zipFilePath, file)) {
                        file.delete();
                        file = new File(zipFilePath);
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/7z"), file);
                    String signature = Helper.getMd5Sum(file);
                    Call<Status> status = signature.equals("unavailable") ? service.getService().register(body, signature) : null;
                    if (status == null) {
                        return;
                    }
                    status.enqueue(new Callback<>() {
                        @Override
                        public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                            Status message = response.body();
                            if (response.code() == 200) {
                                Toast.makeText(SignUpActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, message.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Status> call, @NonNull Throwable t) {
                            Toast.makeText(SignUpActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

    private void initializeViews() {
        fullName = findViewById(R.id.userNameSignUpTextInputEditText);
        email = findViewById(R.id.emailSignUpTextInputEditText);
        password = findViewById(R.id.passwordSignUpTextInputEditText);
        confirmPassword = findViewById(R.id.confirmPasswordSignUpTextInputEditText);
        signUp = findViewById(R.id.signUpButton);
        confirmPassword.setTextColor(Color.RED);
    }

    public void signInAlready(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}