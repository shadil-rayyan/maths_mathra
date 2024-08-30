package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentCountNumbersBinding;
import com.zendalona.mathmantra.utils.PermissionManager;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.SpeechRecognitionUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

import java.util.ArrayList;
import java.util.List;

public class CountNumbersFragment extends Fragment {

    private FragmentCountNumbersBinding binding;
    private SpeechRecognitionUtility speechRecognitionUtil;
    private TTSUtility tts;
    private PermissionManager permissionManager;
    private List<Integer> correctSequence;
    private RandomValueGenerator random;
    private final int COUNT_RANGE = 50;
    private String answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCountNumbersBinding.inflate(inflater, container, false);
        random = new RandomValueGenerator();
        tts = new TTSUtility(requireActivity());
        answer = startGame();

        // Initialize PermissionManager
        permissionManager = new PermissionManager(requireActivity(), new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                // Initialize SpeechRecognitionUtility iff permission is granted
                speechRecognitionUtil = new SpeechRecognitionUtility(requireActivity(), new SpeechRecognitionUtility.SpeechRecognitionCallback() {
                    @Override
                    public void onResults(String spokenText) {
                        binding.micAnimationView.pauseAnimation();
                        String normalizedSpokenText = spokenText.replaceAll("\\s+", "");
                        showResultDialog(normalizedSpokenText.equalsIgnoreCase(answer));
                        Log.d("Recite Numbers : spoken:-", spokenText);
                        Log.d("Recite Numbers : answerFormed:-", answer);
//                       Toast.makeText(requireActivity(),"CountNumbers.java : Heard :- " + spokenText, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int error) {
                        binding.micAnimationView.pauseAnimation();
                        Toast.makeText(requireActivity(),"Error counting" + error, Toast.LENGTH_SHORT).show();
                        Log.e("CountNumbers : Err : -", String.valueOf(error));
                    }
                });
            }


            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });

        permissionManager.requestMicrophonePermission();

        // Set up button listener to start listening
        binding.startListeningButton.setOnClickListener(v -> {
            binding.micAnimationView.playAnimation();
            if(speechRecognitionUtil != null) speechRecognitionUtil.startListening();
            else Log.d("CountNumbers :: startListeningBtnClicked", "speechRecognitionUtil value is null!");
        });

        return binding.getRoot();
    }

    private String startGame() {
        // Initialize the correct sequence and other variables
        correctSequence = new ArrayList<>();
        StringBuilder answer = new StringBuilder();
        int[] range = random.generateNumberRangeForCount(COUNT_RANGE);
        int start = range[0];
        int end = range[1];
        binding.countFromRangeTv.setText("Count from " + start + " to " + end);
        for (int i = start; i <= end; i++) {
            correctSequence.add(i);
            answer.append(i);
        }
        return answer.toString();
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Very Good" : "Try again";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        tts.speak(message);

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    answer = startGame();
                })
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognitionUtil != null) speechRecognitionUtil.destroy();
        if (tts != null) tts.shutdown();
        binding = null;
    }



}
