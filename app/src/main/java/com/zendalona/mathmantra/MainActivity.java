package com.zendalona.mathmantra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.graphics.Region;
import android.view.Display;

import com.zendalona.mathmantra.databinding.ActivityMainBinding;
import com.zendalona.mathmantra.ui.DashboardFragment;
import com.zendalona.mathmantra.ui.LandingPageFragment;
import com.zendalona.mathmantra.ui.LearningPageFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;
import com.zendalona.mathmantra.utils.PermissionManager;
import com.zendalona.mathmantra.utils.AccessibilityUtils;
import com.zendalona.mathmantra.utils.MathsManthraAccessibilityService;

public class MainActivity extends AppCompatActivity implements FragmentNavigation {
    private ActivityMainBinding binding;
    private PermissionManager permissionManager;

    // Static reference to accessibility service
    private static MathsManthraAccessibilityService appAccessibilityService;
    private Region getRegionOfFullScreen(MainActivity activity) {
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        return new Region(0, 0, screenWidth, screenHeight);
    }

    // Static instance of AccessibilityUtils
    private static AccessibilityUtils accessibilityUtils = new AccessibilityUtils();

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
            LandingPageFragment landingFragment = new LandingPageFragment();
            loadFragment(landingFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            updateUpButtonVisibility(landingFragment);
        }

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

        permissionManager.requestMicrophonePermission();

        // 🔔 Add Accessibility Service Check Here
        checkAccessibilityService();
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

    public void loadFragment(Fragment fragment, int transition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);

        if (fragment instanceof LandingPageFragment) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
        updateUpButtonVisibility(fragment);
    }

    private void updateUpButtonVisibility(Fragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            boolean isHomePage = fragment instanceof LandingPageFragment;
            actionBar.setDisplayHomeAsUpEnabled(!isHomePage);
        }
    }

    // Static method to allow the AccessibilityService to set itself
    public static void set_accessibility_service(MathsManthraAccessibilityService myAccessibilityService) {
        appAccessibilityService = myAccessibilityService;
    }

    // Optional: Provide access to AccessibilityUtils if needed elsewhere
    public static AccessibilityUtils getAccessibilityUtils() {
        return accessibilityUtils;
    }

    // Optional: Method to interact with the accessibility service if needed
    public static void updateWindowState() {
        Log.d("MainActivity", "Accessibility Event Triggered: Updating Window State");
        // Example logic; you can add more here if needed.
    }

    private void checkAccessibilityService() {
        AccessibilityUtils utils = new AccessibilityUtils();

        boolean isTalkBackOn = utils.isSystemExploreByTouchEnabled(this);
        boolean isServiceEnabled = AccessibilityUtils.isMathsManthraAccessibilityServiceEnabled(this);

        if (isTalkBackOn && !isServiceEnabled) {
            Log.w("AccessibilityCheck", "TalkBack is ON but MathsManthraAccessibilityService is OFF. Redirecting user.");
            AccessibilityUtils.redirectToAccessibilitySettings(this);
        } else {
            Log.d("AccessibilityCheck", "Accessibility check passed.");
        }
    }

    public void disableExploreByTouch() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && appAccessibilityService != null) {
                Region fullScreenRegion = getRegionOfFullScreen(this);

                Log.e("AAAA disableExploreByTouch", "Region " + fullScreenRegion);

                appAccessibilityService.setTouchExplorationPassthroughRegion(Display.DEFAULT_DISPLAY, fullScreenRegion);
                appAccessibilityService.setGestureDetectionPassthroughRegion(Display.DEFAULT_DISPLAY, fullScreenRegion);

                Log.e("AAAA disableExploreByTouch", "Explore by Touch disabled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetExploreByTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && appAccessibilityService != null) {
            Region emptyRegion = new Region();  // Clear passthrough regions (restore normal TalkBack behavior)
            appAccessibilityService.setTouchExplorationPassthroughRegion(Display.DEFAULT_DISPLAY, emptyRegion);
            appAccessibilityService.setGestureDetectionPassthroughRegion(Display.DEFAULT_DISPLAY, emptyRegion);
        }
    }

}
