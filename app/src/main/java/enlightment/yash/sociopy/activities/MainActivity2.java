package enlightment.yash.sociopy.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import enlightment.yash.sociopy.MuchSimpler;
import enlightment.yash.sociopy.R;

@AndroidEntryPoint
public class MainActivity2 extends AppCompatActivity {

    @Inject
    public MuchSimpler simpler;
    private EditText searchEditText;
    private Button searchButton, locationButton;

    private FusedLocationProviderClient locationProviderClient;

    private ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Toast.makeText(this, "coarse location permission is granted", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initializeViews();
        init();

        /*Toast.makeText(this, simpler.getComplexName(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, simpler.getComplicatedName(), Toast.LENGTH_LONG).show();
        Uri uri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI, 4);*/

        searchButton.setOnClickListener(v -> {
            final String string = searchEditText.getText().toString();
            if (!string.isEmpty()) {
                int result = queryWords(string);
                Toast.makeText(this, switch (result) {
                    case 0 -> "word is present";
                    case 1 -> "word was not present but is inserted now";
                    default -> "some error occurred";
                }, Toast.LENGTH_SHORT).show();
            } else {
                searchEditText.setError("please enter something");
            }
        });

        locationButton.setOnClickListener(v -> {
            getCurrentLocation();
        });
    }

    private void changeLocationSettings() {
        LocationRequest request = new LocationRequest.Builder(1000)
                .setPriority(Priority.PRIORITY_LOW_POWER)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, locationSettingsResponse -> {

        });
        task.addOnFailureListener(this, e -> {

        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            String current = String.format("latitude: %s, longitude: %s%n", location.getLatitude(), location.getLongitude());
                            searchEditText.setText(current);
                        }
                    });
        } else {
            launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    private int queryWords(String string) {

        int result = -1;
        String[] projections = {UserDictionary.Words._ID, UserDictionary.Words.WORD};
        String selectionClause = String.format("%s = ?", UserDictionary.Words.WORD);
        String[] selectionArgs = {""};

        Cursor cursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                projections,
                null,
                null,
                null
        );
        try {
            if (cursor != null && cursor.getCount() >= 0) {
                int index = cursor.getColumnIndex(UserDictionary.Words.WORD);
                while (cursor.moveToNext()) {
                    if (cursor.getString(index).equals(string)) {
                        result = 0;
                        return result;
                    }
                }
                ContentValues values = new ContentValues();
                values.put(UserDictionary.Words.APP_ID, "example_user");
                values.put(UserDictionary.Words.LOCALE, "en_US");
                values.put(UserDictionary.Words.WORD, string);
                values.put(UserDictionary.Words.FREQUENCY, "1");
                getContentResolver().insert(
                        UserDictionary.Words.CONTENT_URI,
                        values
                );
                result = 1;
            } else {
                result = 2;
            }
        } finally {
            assert cursor != null;
            cursor.close();
        }
        return result;
    }

    private void init() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initializeViews() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        locationButton = findViewById(R.id.locationButton);
    }
}