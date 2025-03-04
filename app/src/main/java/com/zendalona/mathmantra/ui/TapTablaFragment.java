package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.graphics.Region;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.MainActivity;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentTapTablaBinding;
import com.zendalona.mathmantra.utils.AccessibilityUtils;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.SoundEffectUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

public class TapTablaFragment extends Fragment {

    private FragmentTapTablaBinding binding;
    private SoundEffectUtility soundEffectUtility;
    private RandomValueGenerator randomValueGenerator;
    private TTSUtility tts;
    private int count, target;
    private boolean talkBackEnabled = false;

    public TapTablaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
        talkBackEnabled = AccessibilityUtils.isMathsManthraAccessibilityServiceEnabled(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTapTablaBinding.inflate(inflater, container, false);
        randomValueGenerator = new RandomValueGenerator();
        tts = new TTSUtility(requireContext());
        startGame();

        // Always allow tapping tabla directly
        binding.tablaAnimationView.setOnClickListener(v -> onTablaTapped());

        setupGlobalTouchHandler();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Disable Explore By Touch if TalkBack is enabled
        if (talkBackEnabled) {
            ((MainActivity) requireActivity()).disableExploreByTouch();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Restore Explore By Touch if TalkBack was enabled
        if (talkBackEnabled) {
            ((MainActivity) requireActivity()).resetExploreByTouch();
        }
    }

    private void setupGlobalTouchHandler() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                playDrumSound();
            }
            return true; // Consume all touch events
        });
    }

    private void playDrumSound() {
        onTablaTapped();
    }

    private void onTablaTapped() {
        binding.tapCount.setText(String.valueOf(++count));
        binding.tablaAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.drums_sound);

        if (count == target) {
            appreciateUser();
        }
    }

    private void appreciateUser() {
        String message = "Well done";
        int gifResource = R.drawable.right;

        DialogResultBinding dialogBinding = DialogResultBinding.inflate(getLayoutInflater());
        Glide.with(this).asGif().load(gifResource).into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);
        tts.speak("Well done! Click on continue!");

        new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    binding.tapCount.setText("0");
                    startGame();
                })
                .create()
                .show();
    }

    private void startGame() {
        count = 0;
        target = randomValueGenerator.generateNumberForCountGame();
        String targetText = "Tap the drum " + target + " times";
        tts.speak(targetText);
        binding.tapMeTv.setText(targetText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
