//package com.zendalona.mathmantra.ui;
//
//import android.app.AlertDialog;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.zendalona.mathmantra.databinding.FragmentMathQuizBinding;
//https://github.com/zendalona/Math-Mantra.git
//import java.util.Random;
//
//public class FingerTouchFragment extends Fragment {
//
//    private FragmentMathQuizBinding binding;
//    private int targetFingerCount;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = FragmentMathQuizBinding.inflate(inflater, container, false);
//
//        // Generate and display random number of fingers required
//        generateNewInstruction();
//
//        // Set touch listener on root layout
//        binding.getRoot().setOnTouchListener(this::onTouchEvent);
//
//        return binding.getRoot();
//    }
//
//    private void generateNewInstruction() {
//        targetFingerCount = new Random().nextInt(10) + 1; // Random 1 to 10
//        binding.fingerInstruction.setText("Touch the screen with " + targetFingerCount + " fingers.");
//    }
//
//    private boolean onTouchEvent(View view, MotionEvent event) {
//        int pointerCount = event.getPointerCount(); // Number of fingers touching screen
//
//        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN || event.getAction() == MotionEvent.ACTION_DOWN) {
//            checkFingerCount(pointerCount);
//        }
//
//        return true;  // We handled the touch event
//    }
//
//    private void checkFingerCount(int actualFingerCount) {
//        boolean isCorrect = actualFingerCount == targetFingerCount;
//
//        showResultDialog(isCorrect);
//
//        // After showing result, prepare next instruction
//        new Handler(Looper.getMainLooper()).postDelayed(this::generateNewInstruction, 4000);
//    }
//
//    private void showResultDialog(boolean isCorrect) {
//        String message = isCorrect ? "Correct! 🎉" : "Wrong! You used " + targetFingerCount + " fingers.";
//        new AlertDialog.Builder(requireContext())
//                .setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }
//}
