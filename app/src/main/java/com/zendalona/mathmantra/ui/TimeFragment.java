package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.zendalona.mathmantra.databinding.FragmentTimeBinding;

import java.util.Random;

public class TimeFragment extends Fragment {

    private FragmentTimeBinding binding;
    private final String[] names = {"Emma", "Oliver", "Sophia", "Liam", "Ava", "Noah", "Mia", "Ethan"};
    private final String[] questionTemplates = {
            "%s studied for %d hours in the morning and %d more hours in the evening. How many hours in total?",
            "%s worked for %d hours before lunch and %d hours after lunch. What is the total working time?",
            "%s spent %d hours playing in the morning and %d more hours in the evening. How many hours did they play in total?",
            "%s was awake for %d hours before noon and %d hours after noon. What is the total time spent awake?"
    };

    public TimeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimeBinding.inflate(inflater, container, false);

        // Ensuring TalkBack reads this fragment when it loads
        binding.getRoot().setFocusable(true);
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();

        generateNewQuestion();
        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random rand = new Random();
        String name = names[rand.nextInt(names.length)];
        int morningHours = rand.nextInt(5) + 1; // Random number between 1 and 5
        int eveningHours = rand.nextInt(5) + 1; // Random number between 1 and 5
        int totalHours = morningHours + eveningHours;

        String question = String.format(questionTemplates[rand.nextInt(questionTemplates.length)], name, morningHours, eveningHours);

        binding.answerEt.setText("");
        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription("Question: " + question); // Ensures TalkBack reads the question properly

        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer(totalHours));
    }

    private void checkAnswer(int correctAnswer) {
        String userInput = binding.answerEt.getText().toString().trim();
        if (userInput.isEmpty()) {
            binding.answerEt.setError("Please enter a valid number");
            Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = Integer.parseInt(userInput) == correctAnswer;
        showResultDialog(isCorrect);
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer" : "Wrong Answer";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        Glide.with(this).asGif().load(gifResource).into(dialogBinding.gifImageView);
        dialogBinding.messageTextView.setText(message);
        dialogBinding.messageTextView.setContentDescription("Result: " + message); // TalkBack support

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                generateNewQuestion();
            }
        }, 4000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}
