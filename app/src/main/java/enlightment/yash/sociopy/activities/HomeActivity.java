package enlightment.yash.sociopy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.beans.Profile;
import enlightment.yash.sociopy.fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment = new HomeFragment();
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();

        Intent intent = getIntent();
        Profile profile = (intent != null && intent.getParcelableExtra("profile") != null) ? intent.getParcelableExtra("profile") : null;

        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            addFragment(fragment);
        }
    }

    private void addFragment(Fragment fragment) {
        Toast.makeText(this, fragment.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        manager.beginTransaction()
                .replace(R.id.homeFragmentContainerView, fragment)
                .commit();
    }

    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.homeBottomNavigationView);
    }
}