package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentRingBellBinding;
import com.zendalona.mathmantra.utils.AccelerometerUtility;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.SoundEffectUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

public class RingBellFragment extends Fragment {

    private FragmentRingBellBinding binding;
    private AccelerometerUtility accelerometerUtility;
    private SoundEffectUtility soundEffectUtility;
    private RandomValueGenerator randomValueGenerator;
    private TTSUtility tts;
    private boolean gameCompleted = false;
    private int count, target;

    public RingBellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
        accelerometerUtility = new AccelerometerUtility(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRingBellBinding.inflate(inflater, container, false);
        randomValueGenerator = new RandomValueGenerator();
        tts = new TTSUtility(requireContext());

        startGame();
        return binding.getRoot();
    }

    private void ringBell() {
        if (gameCompleted) return;

        binding.bellAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.bell_ring);
        count++;
        binding.ringCount.setText(String.valueOf(count));
        binding.ringCount.setContentDescription("Shake count: " + count);

        if (count == target) {
            gameCompleted = true;
            evaluateGameResult();
        }
    }

    private void evaluateGameResult() {
        new Handler().postDelayed(() -> {
            if (count == target) {
                showSuccessDialog();
            } else {
                showFailureDialog();
            }
        }, 2000); // Delay check
    }

    private void showSuccessDialog() {
        showResultDialog("Well done!", R.drawable.right);
    }

    private void showFailureDialog() {
        showResultDialog("Try again!", R.drawable.wrong);
    }

    private void showResultDialog(String message, int gifResource) {
        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        Glide.with(this).asGif().load(gifResource).into(dialogBinding.gifImageView);
        dialogBinding.messageTextView.setText(message);

        tts.speak(message + ", Click continue!");

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    startGame();
                })
                .create()
                .show();
    }

    private void startGame() {
        gameCompleted = false;
        count = 0;
        binding.ringCount.setText("0");
        binding.bellAnimationView.setVisibility(View.VISIBLE);
        target = randomValueGenerator.generateNumberForCountGame();

        String targetText = "Shake the bell " + target + " times";
        tts.speak(targetText);
        binding.ringMeTv.setText(targetText);
        binding.ringMeTv.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();

        new Thread(() -> {
            while (isVisible() && !gameCompleted) {
                try {
                    Thread.sleep(500); // Prevent multiple shakes
                    if (accelerometerUtility.isDeviceShaken()) {
                        requireActivity().runOnUiThread(this::ringBell);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        accelerometerUtility.unregisterListener();
        tts.stop(); // Prevent TalkBack from reading previous screen
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tts.shutdown();
    }
}
