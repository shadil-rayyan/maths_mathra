package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.utils.RotationSensorUtility;
import com.zendalona.mathmantra.utils.RandomValueGenerator;

public class AngleFragment extends Fragment implements RotationSensorUtility.RotationListener {

    private TextView rotationTextView, questionTextView;
    private RotationSensorUtility rotationSensorUtility;
    private RandomValueGenerator random;

    private float targetRotation = 0f;
    private boolean questionAnswered = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_angle, container, false);

        rotationTextView = view.findViewById(R.id.rotation_angle_text);
        questionTextView = view.findViewById(R.id.angle_question);

        random = new RandomValueGenerator();
        generateNewQuestion();

        rotationSensorUtility = new RotationSensorUtility(requireContext(), this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rotationSensorUtility != null) {
            rotationSensorUtility.unregisterListener();
        }
    }

    @Override
    public void onRotationChanged(float azimuth, float pitch, float roll) {
        if (rotationTextView != null) {
            requireActivity().runOnUiThread(() -> {
                rotationTextView.setText(
                        String.format("Rotation Angle:\nAzimuth: %.1f°\nPitch: %.1f°\nRoll: %.1f°", azimuth, pitch, roll)
                );
                checkIfCorrect(roll);
            });
        }
    }

    private void checkIfCorrect(float currentRoll) {
        if (questionAnswered) return;

        float difference = Math.abs(targetRotation - currentRoll);

        if (difference <= 10) { // Acceptable margin
            questionAnswered = true;
            showResultDialog();
        }
    }

    private void showResultDialog() {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Correct!")
                .setMessage("You rotated correctly to " + (int) targetRotation + "°!")
                .setCancelable(false)
                .create();

        dialog.show();

        // Announce result for accessibility
        questionTextView.announceForAccessibility("Correct! You rotated to " + (int) targetRotation + "°!");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                generateNewQuestion();
            }
        }, 3000);
    }

    private void generateNewQuestion() {
        targetRotation = random.generateRandomDegree();
        questionAnswered = false;

        String questionText = "Rotate to " + (int) targetRotation + "°";
        questionTextView.setText(questionText);
        questionTextView.announceForAccessibility(questionText);
    }
}
