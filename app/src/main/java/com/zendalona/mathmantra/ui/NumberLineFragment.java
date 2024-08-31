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
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);

        viewModel.getCurrentPosition().observe(getViewLifecycleOwner(), position -> {
            updateNumberLine();
            tts.speak(CURRENT_POSITION + position);
            binding.currentPositionTv.setText(CURRENT_POSITION + position);
        });

        binding.btnLeft.setOnClickListener(v -> viewModel.moveLeft());
        binding.btnRight.setOnClickListener(v -> viewModel.moveRight());

        updateNumberLine();

        return binding.getRoot();
    }

    private void updateNumberLine() {
        int start = viewModel.getNumberLineStart().getValue();
        int end = viewModel.getNumberLineEnd().getValue();
        int position = viewModel.getCurrentPosition().getValue();
        binding.numberLineView.updateNumberLine(start, end, position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}