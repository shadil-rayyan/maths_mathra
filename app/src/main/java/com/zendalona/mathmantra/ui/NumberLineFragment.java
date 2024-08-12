package com.zendalona.mathmantra.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentNumberLineBinding;
import com.zendalona.mathmantra.utils.TTSUtility;

public class NumberLineFragment extends Fragment {

    private FragmentNumberLineBinding binding;
    private TTSUtility tts;
    private final String CURRENT_POSITION = "You're standing on number : \n";


    public NumberLineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(1.25f);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Lock orientation to landscape when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        requireActivity().getActionBar().setTitle("Number Line");
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);
        binding.btnLeft.setOnClickListener(v -> {
            int currentPosition = binding.numberLineView.moveLeft();
            binding.currentPositionTv.setText(CURRENT_POSITION + currentPosition);
            tts.speak(Integer.toString(currentPosition));
        });
        binding.btnRight.setOnClickListener(v -> {
            int currentPosition = binding.numberLineView.moveRight();
            binding.currentPositionTv.setText(CURRENT_POSITION + currentPosition);
            tts.speak(Integer.toString(currentPosition));
        });
        return binding.getRoot();
    }
}