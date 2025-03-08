package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private int num1, num2; // Store numbers for TTS

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

        // Set accessibility descriptions
        setAccessibilityDescriptions();

        generateNewQuestion(); // Generate question but don't read aloud

        // Read question when button is pressed
        binding.readQuestionBtn.setOnClickListener(v -> {
            readQuestionAloud();
            binding.readQuestionBtn.announceForAccessibility("Reading the math problem aloud.");
        });

        // Submit answer listener
        binding.submitAnswerBtn.setOnClickListener(v -> {
            String userInput = binding.answerEt.getText().toString();
            if (!userInput.isEmpty()) {
                showResultDialog(Integer.parseInt(userInput) == correctAnswer);
            } else {
                binding.submitAnswerBtn.announceForAccessibility("Please enter an answer before submitting.");
            }
        });

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        // Generate two numbers for subtraction
        int[] numbers = random.generateSubtractionValues(Difficulty.EASY);
        num1 = numbers[0];
        num2 = numbers[1];
        correctAnswer = numbers[2]; // Store the correct answer

        // Clear previous answer
        binding.answerEt.setText("");

        // Announce new question for screen readers
        binding.answerEt.announceForAccessibility("A new question has been generated. Tap 'Read the Question' to listen.");
    }

    private void readQuestionAloud() {
        // Speak instruction and numbers
        ttsUtility.speak("Listen carefully. Subtract the first number you hear from the second number.");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ttsUtility.speak("The first number is " + num1 + ". The second number is " + num2 + ".");
        }, 2000);
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer" : "Wrong Answer";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        // Load the GIF using Glide
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

        // Announce result for screen readers
        dialogView.announceForAccessibility(message);

        // Automatically dismiss the dialog after 4 seconds and generate a new question
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                generateNewQuestion();
            }
        }, 4000);
    }

    private void setAccessibilityDescriptions() {
        binding.readQuestionBtn.setContentDescription("Button to read the math question aloud.");
        binding.answerEt.setContentDescription("Enter your answer here.");
        binding.submitAnswerBtn.setContentDescription("Submit your answer.");
    }
}
