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
            // TODO: Implement what happens when Time is clicked
            // Example: navigate to Time learning fragment (if applicable)
        });

        binding.cardCurrency.setOnClickListener(v -> {
            // TODO: Implement what happens when Currency is clicked
        });

        binding.cardDistance.setOnClickListener(v -> {
            // TODO: Implement what happens when Distance is clicked
        });

        binding.cardAddition.setOnClickListener(v -> {
            // TODO: Implement what happens when Arithmetic is clicked
        });

        binding.cardSubtraction.setOnClickListener(v -> {
            // TODO: Implement what happens when Subtraction is clicked
        });

        binding.cardMultiplication.setOnClickListener(v -> {
            // TODO: Implement what happens when Multiplication is clicked
        });

        binding.cardDivision.setOnClickListener(v -> {
            // TODO: Implement what happens when Division is clicked
        });

        binding.cardTilerFrame.setOnClickListener(v -> {
            // TODO: Implement what happens when Tiler Frame is clicked
        });

        return binding.getRoot();
    }
}
