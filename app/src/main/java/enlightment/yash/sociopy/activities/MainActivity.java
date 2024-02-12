package enlightment.yash.sociopy.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.viewmodels.UserModel;

public class MainActivity extends AppCompatActivity {

    private TextView value;
    private final Context CONTEXT = MainActivity.this;
    private Button button, pickContact;

    private final static String STATE = "STATE";

    private final int PICK_CONTACT_REQUEST = 300;

    private UserModel viewModel;

    private ActivityResultLauncher<Intent> launcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        viewModel = new ViewModelProvider(this).get(UserModel.class);
        button.setOnClickListener(v -> {
            viewModel.incrementCounter();
            value.setText(String.valueOf(viewModel.getCounter()));
        });
        pickContact.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            launcher.launch(intent);
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Uri data = result.getData().getData();
                            startActivity(new Intent(Intent.ACTION_VIEW, data));
                        }
                    }
                });
        Toast.makeText(CONTEXT, "onCreate() called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        value.setText(String.valueOf(viewModel.getCounter()));
        Toast.makeText(CONTEXT, "onStart called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(CONTEXT, "onResume() called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(CONTEXT, "onSaveInstanceState() called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(CONTEXT, "onRestoreInstanceState() called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        value = findViewById(R.id.text);
        button = findViewById(R.id.button);
        pickContact = findViewById(R.id.pickButton);
    }
}