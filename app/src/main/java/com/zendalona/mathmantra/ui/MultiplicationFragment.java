package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.databinding.FragmentMentalCalculationBinding;
import com.zendalona.mathmantra.databinding.FragmentMultiplicationBinding;
import com.zendalona.mathmantra.databinding.FragmentSubtractionBinding;

public class MultiplicationFragment extends Fragment {

    private FragmentMultiplicationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentMultiplicationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
