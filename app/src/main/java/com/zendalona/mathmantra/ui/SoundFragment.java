package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentSoundBinding;
import com.zendalona.mathmantra.enums.Difficulty;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.TTSUtility;

public class SoundFragment extends Fragment {

    private FragmentSoundBinding binding;
    private RandomValueGenerator random;
    private TTSUtility ttsUtility;
    private int correctAnswer;
    private int num1, num2;

    public SoundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSoundBinding.inflate(inflater, container, false);
        random = new RandomValueGenerator();
        ttsUtility = new TTSUtility(requireContext());

        setAccessibilityDescriptions();

        generateNewQuestion();

        // Auto-focus and play the question on fragment load
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.readQuestionBtn.requestFocus();
            readQuestionAloud();
        }, 500);

        binding.readQuestionBtn.setOnClickListener(v -> {
            readQuestionAloud();
        });

        binding.submitAnswerBtn.setOnClickListener(v -> submitAnswer());

        // Auto-submit on pressing enter
        binding.answerEt.setOnEditorActionListener((v, actionId, event) -> {
            submitAnswer();
            return true;
        });

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        int[] numbers = random.generateSubtractionValues(Difficulty.EASY);
        num1 = numbers[0];
        num2 = numbers[1];
        correctAnswer = num1 - num2; // Fixing incorrect logic

        binding.answerEt.setText("");

        binding.answerEt.announceForAccessibility("A new question has been generated. Tap 'Read the Question' to listen.");
    }

    private void readQuestionAloud() {
        ttsUtility.speak("Listen carefully. Subtract the second number from the first number.");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ttsUtility.speak("The first number is " + num1 + ". The second number is " + num2 + ".");
        }, 2000);
    }

    private void submitAnswer() {
        String userInput = binding.answerEt.getText().toString();
        if (!userInput.isEmpty()) {
            boolean isCorrect = Integer.parseInt(userInput) == correctAnswer;
            showResultDialog(isCorrect);
        } else {
            binding.answerEt.requestFocus();
            binding.answerEt.announceForAccessibility("Please enter an answer before submitting.");
            Toast.makeText(requireContext(), "Please enter an answer!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer!" : "Wrong Answer. Try again.";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        Glide.with(this)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.show();
        dialogView.announceForAccessibility(message);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                generateNewQuestion();
            }
        }, 2000); // Show for 2 seconds only
    }

    private void setAccessibilityDescriptions() {
        binding.readQuestionBtn.setContentDescription("Read question aloud.");
        binding.answerEt.setContentDescription("Answer field. Enter your answer.");
        binding.submitAnswerBtn.setContentDescription("Submit your answer.");
    }
}