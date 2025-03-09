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
import com.zendalona.mathmantra.databinding.FragmentDivisionBinding;

import java.util.Random;

public class DivisionFragment extends Fragment {

    private FragmentDivisionBinding binding;
    private int correctAnswer;

    private final String[] questions = {
            "There are %d toys, and they are shared equally among %d children. How many toys does each child get?",
            "A class has %d students, split equally into %d groups. How many students are in each group?",
            "A bakery bakes %d cupcakes and packs them into boxes of %d each. How many boxes are needed?",
            "A farmer harvested %d apples and placed them into bags containing %d apples each. How many bags were filled?",
            "A bus has %d seats, and they are divided evenly among %d rows. How many seats are in each row?"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDivisionBinding.inflate(inflater, container, false);

        generateNewQuestion();
        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();

        // Pick a random question template
        String questionTemplate = questions[random.nextInt(questions.length)];

        // Ensure division results in a whole number
        int b = random.nextInt(9) + 2; // Random divisor (2-10)
        int correctQuotient = random.nextInt(9) + 2; // Random quotient (2-10)
        int a = b * correctQuotient; // Compute dividend

        correctAnswer = correctQuotient;

        // Format the question
        String question = String.format(questionTemplate, a, b);

        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription(question); // TalkBack description
        binding.answerEt.setText(""); // Clear input field
        binding.answerEt.setHint("Enter the number per group");
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
