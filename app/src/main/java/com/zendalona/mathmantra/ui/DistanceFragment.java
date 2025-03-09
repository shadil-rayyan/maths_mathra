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

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentDistanceBinding;
import com.bumptech.glide.Glide;

import java.util.Random;

public class DistanceFragment extends Fragment {

    private FragmentDistanceBinding binding;
    private final String[] names = {"Sarah", "Amit", "Priya", "Rahul", "Neha", "Arjun", "Sneha", "Vikram"};
    private int correctAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDistanceBinding.inflate(inflater, container, false);

        generateNewQuestion();
        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();

        // Pick a random name
        String name = names[random.nextInt(names.length)];

        // Generate two random distances
        int x = random.nextInt(10) + 1; // 1 - 10 km
        int y = random.nextInt(10) + 1; // 1 - 10 km

        correctAnswer = x + y;

        // Set the question text dynamically
        String question = name + " walked " + x + " kilometers in the morning. In the evening, " +
                "they walked " + y + " more kilometers. How far did they walk in total?";

        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription(question); // TalkBack description
        binding.answerEt.setText(""); // Clear input field
        binding.answerEt.setHint("Enter total distance in km");
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
        String message = isCorrect ? "Right Answer!" : "Wrong Answer!";
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
