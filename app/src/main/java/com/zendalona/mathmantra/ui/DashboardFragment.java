package com.zendalona.mathmantra.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.databinding.FragmentDashboardBinding;
import com.zendalona.mathmantra.utils.FragmentNavigation;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
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
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.ringBellCv.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new RingBellFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        binding.tapTablaCv.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new TapTablaFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        binding.angleButton.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new AngleFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        binding.drawingButton.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new DrawingFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        binding.mcqCv.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new MCQFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        //can use this for quick play screen
        binding.directionButton.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new DirectionFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });
        binding.numberLineCv.setOnClickListener(v -> {
            if (navigationListener != null) navigationListener.loadFragment(new NumberLineFragment(),FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        return binding.getRoot();
    }
}