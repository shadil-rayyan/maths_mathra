package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentCurrencyBinding;
import com.bumptech.glide.Glide;

import java.util.Random;

public class CurrencyFragment extends Fragment {

    private FragmentCurrencyBinding binding;
    private final String[] names = {"Amit", "Priya", "Rahul", "Neha", "Arjun", "Sneha", "Vikram", "Pooja"};
    private int correctAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCurrencyBinding.inflate(inflater, container, false);

        generateNewQuestion();

        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();

        // Pick a random name
        String name = names[random.nextInt(names.length)];

        // Generate two random amounts
        int p = random.nextInt(500) + 50; // ₹50 - ₹550
        int q = random.nextInt(500) + 50; // ₹50 - ₹550

        correctAnswer = p + q;

        // Set the question text
        String question = name + " had ₹" + p + ". He earned ₹" + q + " more. How much money does he have now?";
        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription("Question: " + question); // TalkBack reads this
        binding.questionTv.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT);

        // Clear input field
        binding.answerEt.setText("");
        binding.answerEt.setHint("Enter your answer here");
        binding.answerEt.setContentDescription("Enter your answer in numbers only. Example: 100 or 150.");
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(binding.answerEt.getText().toString());
            boolean isCorrect = userAnswer == correctAnswer;
            showResultDialog(isCorrect);
        } catch (NumberFormatException e) {
            binding.answerEt.setError("Please enter a valid number");
            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
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
        dialogBinding.messageTextView.setContentDescription(message); // TalkBack will read this

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.show();

        // Announce the result
        dialogView.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT);

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
