package com.zendalona.mathmantra.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.zendalona.mathmantra.databinding.FragmentDashboardBinding;
import com.zendalona.mathmantra.databinding.FragmentLandingpageBinding;
import com.zendalona.mathmantra.utils.FragmentNavigation;

public class LandingPageFragment extends Fragment {

    private FragmentLandingpageBinding binding;
    private FragmentNavigation navigationListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) navigationListener = (FragmentNavigation) context;
        else throw new RuntimeException(context.toString() + " must implement FragmentNavigation");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLandingpageBinding.inflate(inflater, container, false);

        binding.quickPlayButton.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.loadFragment(new MathQuizFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        binding.learningModeButton.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.loadFragment(new LearningPageFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        binding.gameModeButton.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.loadFragment(new DashboardFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        binding.userGuideButton.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.loadFragment(new UserGuideFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        binding.settingsButton.setOnClickListener(v -> {
            if (navigationListener != null) {
                navigationListener.loadFragment(new SettingFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        binding.quitButton.setOnClickListener(v -> {
            getActivity().finish();  // Quit the app
        });




        return binding.getRoot();
    }
}