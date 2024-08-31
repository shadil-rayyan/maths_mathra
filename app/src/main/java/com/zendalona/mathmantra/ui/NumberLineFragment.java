package com.zendalona.mathmantra.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.databinding.FragmentNumberLineBinding;
import com.zendalona.mathmantra.utils.TTSUtility;
import com.zendalona.mathmantra.viewModels.NumberLineViewModel;

public class NumberLineFragment extends Fragment {

    private FragmentNumberLineBinding binding;
    private NumberLineViewModel viewModel;
    private TTSUtility tts;
    private final String CURRENT_POSITION = "You're standing on number : \n";

    public NumberLineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NumberLineViewModel.class);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(1.25f);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Lock orientation to landscape when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        viewModel.reset();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);

        setupObservers();

        binding.btnLeft.setOnClickListener(v -> {
            viewModel.moveLeft();
            binding.numberLineView.moveLeft();
        });
        binding.btnRight.setOnClickListener(v -> {
            viewModel.moveRight();
            binding.numberLineView.moveRight();
        });

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.lineStart.observe(getViewLifecycleOwner(), start -> {
            int end = viewModel.lineEnd.getValue() != null ? viewModel.lineEnd.getValue() : start + 10;
            int position = viewModel.currentPosition.getValue() != null ? viewModel.currentPosition.getValue() : start;
            binding.numberLineView.updateNumberLine(start, end, position);
        });

        viewModel.lineEnd.observe(getViewLifecycleOwner(), end -> {
            int start = viewModel.lineStart.getValue() != null ? viewModel.lineStart.getValue() : end - 10;
            int position = viewModel.currentPosition.getValue() != null ? viewModel.currentPosition.getValue() : start;
            binding.numberLineView.updateNumberLine(start, end, position);
        });

        viewModel.currentPosition.observe(getViewLifecycleOwner(), position -> {
            binding.currentPositionTv.setText(CURRENT_POSITION + position);
            tts.speak(Integer.toString(position));
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}