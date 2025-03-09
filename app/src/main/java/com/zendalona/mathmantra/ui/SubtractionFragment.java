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
import com.zendalona.mathmantra.databinding.FragmentSubtractionBinding;
import com.bumptech.glide.Glide;

import java.util.Random;

public class SubtractionFragment extends Fragment {

    private FragmentSubtractionBinding binding;
    private int correctAnswer;

    private final String[] questions = {
            "There were %d cookies on the plate. %d cookies were eaten. How many cookies are left?",
            "A bookshelf had %d books. %d books were borrowed. How many books are left on the shelf?",
            "A farmer had %d apples. He sold %d apples. How many apples does he have now?",
            "A store had %d pencils in stock. %d were sold. How many are left?",
            "A classroom had %d students. %d students went home. How many students are still in the classroom?"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubtractionBinding.inflate(inflater, container, false);

        generateNewQuestion();
        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();

        // Pick a random question template
        String questionTemplate = questions[random.nextInt(questions.length)];

        // Generate two random numbers, ensuring a >= b to avoid negative answers
        int a = random.nextInt(50) + 10; // 10 - 59
        int b = random.nextInt(a / 2) + 1; // Ensure b is smaller than a

        correctAnswer = a - b;

        // Format the question
        String question = String.format(questionTemplate, a, b);

        binding.questionTv.setText(question);
        binding.questionTv.setContentDescription(question); // TalkBack description
        binding.answerEt.setText(""); // Clear input field
        binding.answerEt.setHint("Enter the remaining count");
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
