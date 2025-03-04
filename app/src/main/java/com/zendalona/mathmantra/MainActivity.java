package com.zendalona.mathmantra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.zendalona.mathmantra.databinding.ActivityMainBinding;
import com.zendalona.mathmantra.ui.DashboardFragment;
import com.zendalona.mathmantra.ui.LandingPageFragment;
import com.zendalona.mathmantra.ui.LearningPageFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;
import com.zendalona.mathmantra.utils.PermissionManager;

public class MainActivity extends AppCompatActivity implements FragmentNavigation {
    private ActivityMainBinding binding;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set immersive full-screen mode
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIconTint(getColor(android.R.color.white));

        if (savedInstanceState == null) {
            // Load the landing page initially
            LandingPageFragment landingFragment = new LandingPageFragment();
            loadFragment(landingFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

            // Ensure the back button is hidden on initial load
            updateUpButtonVisibility(landingFragment);
        }

        // Permissions management
        permissionManager = new PermissionManager(this, new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d("PermissionManager.PermissionCallback", "Granted!");
            }

            @Override
            public void onPermissionDenied() {
                Log.w("PermissionManager.PermissionCallback", "Denied!");
            }
        });

        // Request necessary permissions
        permissionManager.requestMicrophonePermission();
        // permissionManager.requestAccelerometerPermission();  // Uncomment if needed
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            // If no fragments are left in back stack, reload the landing page
            loadFragment(new LandingPageFragment(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handlePermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Load a new fragment into the container and manage the back stack + up button visibility.
     */
    public void loadFragment(Fragment fragment, int transition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);

        if (fragment instanceof LandingPageFragment) {
            // Clear back stack when loading the Landing Page
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            // Add other fragments to back stack
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();

        // Update back button visibility based on the current fragment
        updateUpButtonVisibility(fragment);
    }

    /**
     * Updates the toolbar's back button visibility based on which fragment is currently shown.
     */
    private void updateUpButtonVisibility(Fragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean isHomePage = fragment instanceof LandingPageFragment;
            actionBar.setDisplayHomeAsUpEnabled(!isHomePage);  // Hide back button on home page
        }
    }
}
