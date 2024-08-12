package com.zendalona.mathmantra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.zendalona.mathmantra.databinding.ActivityMainBinding;
import com.zendalona.mathmantra.ui.DashboardFragment;
import com.zendalona.mathmantra.ui.NumberLineFragment;
import com.zendalona.mathmantra.utils.FragmentNavigation;

public class MainActivity extends AppCompatActivity implements FragmentNavigation {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get the WindowInsetsController
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        // Apply immersive full-screen mode
        controller.hide(WindowInsetsCompat.Type.statusBars() | WindowInsetsCompat.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        if (savedInstanceState == null) loadFragment(new DashboardFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        loadFragment(new DashboardFragment(), FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        return true;
    }

    public void loadFragment(Fragment fragment, int transition) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.replace(binding.fragmentContainer.getId(), fragment);
        fragmentTransaction.commit();
    }

}