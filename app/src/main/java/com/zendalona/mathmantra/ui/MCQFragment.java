package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentMCQBinding;
import com.zendalona.mathmantra.enums.Difficulty;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.TTSUtility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MCQFragment extends Fragment {

    private FragmentMCQBinding binding;
    private RandomValueGenerator random;
    private TTSUtility tts;

    public MCQFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMCQBinding.inflate(inflater, container, false);
        random = new RandomValueGenerator();
        tts = new TTSUtility(requireActivity());
        generateNewQuestion();
        return binding.getRoot();
    }

    private void generateNewQuestion() {
        int topic = random.generateQuestionTopic();
        int[] numbers;
        String operator = "+";

        switch (topic) {
            case 1:
                numbers = random.generateSubtractionValues(Difficulty.EASY);
                operator = "-";
                break;
            case 2:
                numbers = random.generateMultiplicationValues(Difficulty.EASY);
                operator = "*";
                break;
            case 3:
                numbers = random.generateDivisionValues(Difficulty.EASY);
                operator = "/";
                break;
            default:
                numbers = random.generateAdditionValues(Difficulty.EASY);
        }

        String questionText = numbers[0] + " " + operator + " " + numbers[1] + " = ?";
        binding.questionTv.setText(questionText);

        // Update TalkBack contentDescription dynamically
        binding.questionTv.setContentDescription("Question. What is " + numbers[0] + " " + operator + " " + numbers[1] + "? Double tap to repeat.");

        // Generate options
        int[] options = random.generateAdditionValues(Difficulty.EASY);
        List<Integer> choices = new ArrayList<>();
        choices.add(options[0]);
        choices.add(options[1]);
        choices.add(options[2]);
        choices.add(numbers[2]); // Correct answer

        Collections.shuffle(choices);

        // Update buttons dynamically
        updateOption(binding.optionA, choices.get(0), numbers[2]);
        updateOption(binding.optionB, choices.get(1), numbers[2]);
        updateOption(binding.optionC, choices.get(2), numbers[2]);
        updateOption(binding.optionD, choices.get(3), numbers[2]);
    }

    /**
     * Updates the button text and accessibility description
     */
    private void updateOption(MaterialButton button, int value, int correctAnswer) {
        button.setText(String.valueOf(value));
        button.setContentDescription("Option " + button.getText() + ". Double tap to select.");

        button.setOnClickListener(v -> showResultDialog(value == correctAnswer));
    }


    private void showResultDialog(boolean isCorrect) {
        String message = isCorrect ? "Right Answer" : "Wrong Answer";
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;
        tts.speak(message);

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

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
                    generateNewQuestion();
                })
                .create()
                .show();
    }

}