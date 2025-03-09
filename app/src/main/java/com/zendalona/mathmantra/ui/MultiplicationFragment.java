package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentMultiplicationBinding;

import java.util.Random;

public class MultiplicationFragment extends Fragment {

    private FragmentMultiplicationBinding binding;
    private int correctAnswer;

    private final String[] questions = {
            "A tree has %d branches, and each branch has %d nests. How many nests are there in total?",
            "There are %d classrooms, and each classroom has %d desks. How many desks are there in total?",
            "A bakery makes %d cakes per day. How many cakes are made in %d days?",
            "A farmer plants %d rows of crops, with %d plants in each row. How many plants are there in total?",
            "A parking lot has %d sections, and each section has %d cars. How many cars are parked in total?"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMultiplicationBinding.inflate(inflater, container, false);

        generateNewQuestion();
        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();

        // Pick a random question template
        String questionTemplate = questions[random.nextInt(questions.length)];

        // Generate two random numbers for multiplication
        int a = random.nextInt(9) + 2; // Range: 2 - 10
        int b = random.nextInt(9) + 2; // Range: 2 - 10

        correctAnswer = a * b;

        // Format the question
        String question = String.format(questionTemplate, a, b);

        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription(question); // TalkBack description
        binding.answerEt.setText(""); // Clear input field
        binding.answerEt.setHint("Enter the total count");
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(binding.answerEt.getText().toString());
            showResultDialog(userAnswer == correctAnswer);
        } catch (NumberFormatException e) {
            binding.answerEt.setError("Please enter a valid number");
            binding.answerEt.setContentDescription("Invalid input. Enter only numbers.");
        }
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Correct Answer!" : "Incorrect Answer!";
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
        dialogBinding.messageTextView.setContentDescription("Result: " + message);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.show();

        // Dismiss after 3 seconds and generate a new question
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                generateNewQuestion();
            }
        }, 3000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
