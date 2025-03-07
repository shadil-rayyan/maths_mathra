package com.zendalona.mathmantra.ui;
//
//import android.app.AlertDialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import androidx.fragment.app.Fragment;
//import com.bumptech.glide.Glide;
//import com.zendalona.mathmantra.R;
//import com.zendalona.mathmantra.databinding.DialogResultBinding;
//import com.zendalona.mathmantra.databinding.FragmentMathQuizBinding;
//import com.zendalona.mathmantra.enums.Difficulty;
//import com.zendalona.mathmantra.utils.RandomValueGenerator;
//
//public class MathQuizFragment extends Fragment {
//
//    private FragmentMathQuizBinding binding;
//    private RandomValueGenerator random;
//
//    private String currentQuestion;
//    private int correctAnswer;
//
//    public MathQuizFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentMathQuizBinding.inflate(inflater, container, false);
//        random = new RandomValueGenerator();
//        generateNewQuestion();
//
//        binding.submitAnswerBtn.setOnClickListener(v -> checkAnswer());
//        return binding.getRoot();
//    }
//
//    private void generateNewQuestion() {
//        int[] numbers = random.generateAdditionValues(Difficulty.EASY); // Replace with your logic if you want mixed operations
//        currentQuestion = numbers[0] + " + " + numbers[1] + " = ?";
//        correctAnswer = numbers[0] + numbers[1];  // For addition — modify if you need mixed operations logic
//
//        binding.answerEt.setText("");
//        binding.mentalCalculation.setText(currentQuestion);
//    }
//
//    private void checkAnswer() {
//        String answerText = binding.answerEt.getText().toString().trim();
//
//        if (TextUtils.isEmpty(answerText)) {
//            binding.answerEt.setError("Please enter your answer");
//            return;
//        }
//
//        try {
//            int userAnswer = Integer.parseInt(answerText);
//            boolean isCorrect = userAnswer == correctAnswer;
//            showResultDialog(isCorrect);
//        } catch (NumberFormatException e) {
//            binding.answerEt.setError("Invalid number");
//        }
//    }
//
//    private void showResultDialog(boolean isCorrect) {
//        String message = isCorrect ? "Right Answer" : "Wrong Answer";
//        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;
//
//        DialogResultBinding dialogBinding = DialogResultBinding.inflate(getLayoutInflater());
//        View dialogView = dialogBinding.getRoot();
//
//        // Load GIF with Glide
//        Glide.with(this)
//                .asGif()
//                .load(gifResource)
//                .into(dialogBinding.gifImageView);
//
//        dialogBinding.messageTextView.setText(message);
//
//        AlertDialog dialog = new AlertDialog.Builder(requireContext())
//                .setView(dialogView)
//                .setCancelable(false)
//                .create();
//
//        dialog.show();
//
//        // Auto-dismiss and load new question
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            if (dialog.isShowing()) {
//                dialog.dismiss();
//                generateNewQuestion();
//            }
//        }, 4000);  // 4 seconds
//    }
//}
