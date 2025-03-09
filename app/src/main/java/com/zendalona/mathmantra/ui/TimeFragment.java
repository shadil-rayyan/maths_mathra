package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.databinding.FragmentSettingBinding; // Adjust the package name
import com.zendalona.mathmantra.databinding.FragmentTimeBinding;
import com.zendalona.mathmantra.utils.FragmentNavigation;

public class TimeFragment extends Fragment {

    private FragmentTimeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using View Binding
        binding = FragmentTimeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
