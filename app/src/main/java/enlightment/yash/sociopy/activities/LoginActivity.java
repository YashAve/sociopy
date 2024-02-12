package enlightment.yash.sociopy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import enlightment.yash.sociopy.Env;
import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.SociopyService;
import enlightment.yash.sociopy.beans.Profile;
import enlightment.yash.sociopy.viewmodels.CredentialModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private CredentialModel credentialModel;

    private TextInputEditText emailView;
    private TextInputEditText passwordView;

    @Inject
    public SociopyService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();



        credentialModel = new ViewModelProvider(this).get(CredentialModel.class);
    }

    public void signUpFirst(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    public void logIn(View view) throws Exception {
        String email = Objects.requireNonNull(emailView.getText()).toString();
        String password = Objects.requireNonNull(passwordView.getText()).toString();
        switch (credentialModel.verify(1, email, password)) {
            case 1, 2 -> emailView.setError(credentialModel.getMessage());
            case 3 -> passwordView.setError(credentialModel.getMessage());
            default -> {
                Call<Profile> call = service.getService().login(email, password);
                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                        switch (response.code()) {
                            case 200 -> {
                                if (response.body() != null) {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.putExtra("profile", response.body());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            case 201 -> emailView.setError("please enter a registered email address");
                            default -> passwordView.setError("password is incorrect");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, Env.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void initializeView() {
        emailView = findViewById(R.id.emailLoginTextInputEditText);
        passwordView = findViewById(R.id.passwordLoginTextInputEditText);
    }
}