package com.zendalona.mathmantra.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.zendalona.mathmantra.databinding.FragmentLearningmodeBinding;
import com.zendalona.mathmantra.utils.FragmentNavigation;

public class LearningPageFragment extends Fragment {

    private FragmentLearningmodeBinding binding;
    private FragmentNavigation navigationListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation)
            navigationListener = (FragmentNavigation) context;
        else throw new RuntimeException(context.toString() + " must implement FragmentNavigation");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLearningmodeBinding.inflate(inflater, container, false);

        binding.cardTime.setOnClickListener(v -> {
            navigationListener.loadFragment(new TimeFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardCurrency.setOnClickListener(v -> {
            navigationListener.loadFragment(new CurrencyFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardDistance.setOnClickListener(v -> {
            navigationListener.loadFragment(new DistanceFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardAddition.setOnClickListener(v -> {
            navigationListener.loadFragment(new AdditionFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardSubtraction.setOnClickListener(v -> {
            navigationListener.loadFragment(new SubtractionFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardMultiplication.setOnClickListener(v -> {
            navigationListener.loadFragment(new MultiplicationFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardDivision.setOnClickListener(v -> {
            navigationListener.loadFragment(new DivisionFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        binding.cardTilerFrame.setOnClickListener(v -> {
            navigationListener.loadFragment(new TilerFrameFragment(), FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        });

        return binding.getRoot();
    }
}
