package com.zendalona.mathmantra;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.graphics.Region;
import android.view.Display;
import android.view.MotionEvent;

import com.zendalona.mathmantra.databinding.ActivityMainBinding;
import com.zendalona.mathmantra.ui.LandingPageFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;
import com.zendalona.mathmantra.utils.PermissionManager;
import com.zendalona.mathmantra.utils.AccessibilityUtils;
import com.zendalona.mathmantra.utils.MathsManthraAccessibilityService;

public class MainActivity extends AppCompatActivity implements FragmentNavigation {
    private ActivityMainBinding binding;
    private PermissionManager permissionManager;

    // Static reference to accessibility service
    private static MathsManthraAccessibilityService appAccessibilityService;
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
        binding.toolbar.setNavigationContentDescription("Back Button");

        // Hide the Up button initially (so it doesn't appear on Landing Page when app starts)
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (savedInstanceState == null) {
            LandingPageFragment landingFragment = new LandingPageFragment();
            loadFragment(landingFragment, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        permissionManager = new PermissionManager(this, new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Log.d("PermissionManager", "Granted!");
            }

            @Override
            public void onPermissionDenied() {
                Log.w("PermissionManager", "Denied!");
            }
        });
        permissionManager.requestMicrophonePermission();

        checkAccessibilityService();
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            loadFragment(new LandingPageFragment(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        }
        return true;
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
            actionBar.setHomeActionContentDescription("Back Button");
        }
    }

    public static void setAccessibilityService(MathsManthraAccessibilityService myAccessibilityService) {
        appAccessibilityService = myAccessibilityService;
    }

    public static AccessibilityUtils getAccessibilityUtils() {
        return accessibilityUtils;
    }

    public static void updateWindowState() {
        Log.d("MainActivity", "Accessibility Event Triggered: Updating Window State");
    }

    private void checkAccessibilityService() {
        boolean isTalkBackOn = accessibilityUtils.isSystemExploreByTouchEnabled(this);
        boolean isServiceEnabled = AccessibilityUtils.isMathsManthraAccessibilityServiceEnabled(this);

        if (isTalkBackOn && !isServiceEnabled) {
            Log.w("AccessibilityCheck", "TalkBack is ON but MathsManthraAccessibilityService is OFF. Redirecting user.");
            showAccessibilityDialog();
        } else {
            Log.d("AccessibilityCheck", "Accessibility check passed.");
        }
    }

    private void showAccessibilityDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Accessibility Service")
                .setMessage("MathsManthra needs Accessibility Service to function properly. Would you like to enable it?")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void disableExploreByTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && appAccessibilityService != null) {
            Region fullScreenRegion = new Region(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
            appAccessibilityService.setTouchExplorationPassthroughRegion(Display.DEFAULT_DISPLAY, fullScreenRegion);
            appAccessibilityService.setGestureDetectionPassthroughRegion(Display.DEFAULT_DISPLAY, fullScreenRegion);
        }
    }

    public void resetExploreByTouch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && appAccessibilityService != null) {
            Region emptyRegion = new Region();
            appAccessibilityService.setTouchExplorationPassthroughRegion(Display.DEFAULT_DISPLAY, emptyRegion);
            appAccessibilityService.setGestureDetectionPassthroughRegion(Display.DEFAULT_DISPLAY, emptyRegion);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                if (event.getY(0) < event.getHistoricalY(0, 0) && event.getY(1) < event.getHistoricalY(1, 0)) {
                    onBackPressed();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
