package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentNumberLineBinding;
import com.zendalona.mathmantra.enums.Topic;
import com.zendalona.mathmantra.utils.AccessibilityUtils;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.TTSUtility;
import com.zendalona.mathmantra.viewModels.NumberLineViewModel;

import java.util.HashMap;
import java.util.Map;

public class NumberLineFragment extends Fragment {

    private FragmentNumberLineBinding binding;
    private NumberLineViewModel viewModel;
    private TTSUtility tts;
    private RandomValueGenerator random;
    private final String CURRENT_POSITION = "You're standing on number : \n";
    private int answer;
    private String questionDesc = "";
    private String correctAnswerDesc = "";

    private boolean talkBackEnabled = false;
    private float initialX = 0;
    private boolean isTwoFingerSwipe = false;

    public NumberLineFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NumberLineViewModel.class);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(0.8f);
        talkBackEnabled = AccessibilityUtils.isMathsManthraAccessibilityServiceEnabled(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.reset();
        binding.numberLineView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.numberLineView.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);

        random = new RandomValueGenerator();
        setupObservers();

        correctAnswerDesc = askNewQuestion(0);

        binding.numberLineQuestion.setOnClickListener(v -> tts.speak(questionDesc));
        binding.btnLeft.setOnClickListener(v -> moveLeft());
        binding.btnRight.setOnClickListener(v -> moveRight());

        setupTouchListener();

        return binding.getRoot();
    }

    private void setupTouchListener() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            if (talkBackEnabled) {
                handleTwoFingerSwipe(event);
                return true; // Consume event in TalkBack mode
            }
            return false;
        });
    }

    private void handleTwoFingerSwipe(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    initialX = (event.getX(0) + event.getX(1)) / 2;
                    isTwoFingerSwipe = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isTwoFingerSwipe && event.getPointerCount() == 2) {
                    float currentX = (event.getX(0) + event.getX(1)) / 2;
                    float deltaX = currentX - initialX;

                    if (Math.abs(deltaX) > 100) { // Adjust threshold as needed
                        if (deltaX > 0) {
                            moveRight();
                        } else {
                            moveLeft();
                        }
                        isTwoFingerSwipe = false; // Consume gesture
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTwoFingerSwipe = false;
                break;
        }
    }

    private void moveLeft() {
        viewModel.moveLeft();
        binding.numberLineView.moveLeft();
    }

    private void moveRight() {
        viewModel.moveRight();
        binding.numberLineView.moveRight();
    }

    private String askNewQuestion(int position) {
        Topic topic = random.generateNumberLineQuestion() ? Topic.ADDITION : Topic.SUBTRACTION;
        int unitsToMove = random.generateNumberForCountGame();
        String operator = " plus ";
        String direction = " right ";

        Map<String, String> operatorMap = new HashMap<>();
        operatorMap.put("plus", "+");
        operatorMap.put("minus", "-");

        switch (topic) {
            case ADDITION:
                operator = " plus ";
                direction = " right ";
                answer = position + unitsToMove;
                break;
            case SUBTRACTION:
                operator = " minus ";
                direction = " left ";
                answer = position - unitsToMove;
                break;
        }

        String questionBrief = "What is " + position + operatorMap.get(operator.trim()) + unitsToMove + "?";
        binding.numberLineQuestion.setText(questionBrief);

        questionDesc = "You're standing on " + position + ". " +
                "What is " + position + operator + unitsToMove + "? " +
                "Move " + unitsToMove + " units to the " + direction + " of Number line.";

        tts.speak(questionDesc);

        return position + operator + unitsToMove + " equals " + answer;
    }

    private void setupObservers() {
        viewModel.lineStart.observe(getViewLifecycleOwner(), start -> {
            int end = viewModel.lineEnd.getValue() != null ? viewModel.lineEnd.getValue() : start + 10;
            int position = viewModel.currentPosition.getValue() != null ? viewModel.currentPosition.getValue() : start;
            binding.numberLineView.updateNumberLine(start, end, position);
        });

        viewModel.lineEnd.observe(getViewLifecycleOwner(), end -> {
            int start = viewModel.lineStart.getValue() != null ? viewModel.lineStart.getValue() : end - 10;
            int position = viewModel.currentPosition.getValue() != null ? viewModel.currentPosition.getValue() : start;
            binding.numberLineView.updateNumberLine(start, end, position);
        });

        viewModel.currentPosition.observe(getViewLifecycleOwner(), position -> {
            binding.currentPositionTv.setText(CURRENT_POSITION + position);

            if (position == answer) {
                tts.speak("Correct Answer! " + correctAnswerDesc + ".");
                appreciateUser();
            }
        });
    }

    private void appreciateUser() {
        String message = "Good going";
        int gifResource = R.drawable.right;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        Glide.with(this)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    correctAnswerDesc = askNewQuestion(answer);
                })
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
