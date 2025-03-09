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
import com.zendalona.mathmantra.databinding.FragmentMentalCalculationBinding;

import java.util.Random;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MentalCalculationFragment extends Fragment {

    private FragmentMentalCalculationBinding binding;
    private String currentExpression;
    private int correctAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMentalCalculationBinding.inflate(inflater, container, false);
        generateNewQuestion();

        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());

        return binding.getRoot();
    }

    private void generateNewQuestion() {
        Random random = new Random();
        int num1 = random.nextInt(10) + 1;
        int num2 = random.nextInt(10) + 1;
        int num3 = random.nextInt(10) + 1;
        int num4 = random.nextInt(10) + 1;

        String[] operators = {"+", "-", "*", "/"};
        String op1 = operators[random.nextInt(4)];
        String op2 = operators[random.nextInt(4)];
        String op3 = operators[random.nextInt(4)];

        currentExpression = num1 + " " + op1 + " " + num2 + " " + op2 + " " + num3 + " " + op3 + " " + num4;
        correctAnswer = evaluateExpression(currentExpression);

        binding.mentalCalculation.setText(currentExpression + " = ?");
        binding.answerEt.setText(""); // Clear previous answer
    }

    private int evaluateExpression(String expression) {
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            return (int) Math.round(exp.evaluate()); // Evaluates using BODMAS
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void checkAnswer() {
        String userInput = binding.answerEt.getText().toString();
        if (userInput.isEmpty()) {
            Toast.makeText(getContext(), "Enter your answer", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = Integer.parseInt(userInput) == correctAnswer;
        showResultDialog(isCorrect);
    }

    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Correct Answer!" : "Wrong Answer! The correct answer is " + correctAnswer;
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
        binding = null;
    }
}
