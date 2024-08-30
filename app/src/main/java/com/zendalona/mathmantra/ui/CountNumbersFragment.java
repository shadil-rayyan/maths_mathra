package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.databinding.FragmentCountNumbersBinding;
import com.zendalona.mathmantra.utils.PermissionManager;
import com.zendalona.mathmantra.utils.SpeechRecognitionUtility;

import java.util.ArrayList;
import java.util.List;

public class CountNumbersFragment extends Fragment {

    private FragmentCountNumbersBinding binding;
    private SpeechRecognitionUtility speechRecognitionUtil;
    private PermissionManager permissionManager;
    private List<Integer> correctSequence;
    private int currentIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCountNumbersBinding.inflate(inflater, container, false);

        // Initialize the correct sequence and other variables
        correctSequence = new ArrayList<>();
        for (int i = 1; i <= 10; i++) correctSequence.add(i);
        currentIndex = 0;

        // Initialize PermissionManager
        permissionManager = new PermissionManager(requireActivity(), new PermissionManager.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                // Initialize SpeechRecognitionUtility iff permission is granted

                speechRecognitionUtil = new SpeechRecognitionUtility(requireActivity(), new SpeechRecognitionUtility.SpeechRecognitionCallback() {
                    @Override
                    public void onResults(String spokenText) {
                        Toast.makeText(requireActivity(),"CountNumbers.java : Heard :- " + spokenText, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int error) {
                        Toast.makeText(requireActivity(),"CountNumbers.java : Err :- " + error, Toast.LENGTH_SHORT).show();
                    }
                });

            }


            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });

        permissionManager.requestMicrophonePermission();

        // Set up button listener to start listening
        binding.startListeningButton.setOnClickListener(v -> {
            if(speechRecognitionUtil != null) {
                currentIndex = 0;
                speechRecognitionUtil.startListening();
            }
            else Log.d("CountNumbers :: startListeningBtnClicked", "speechRecognitionUtil value is null!");
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognitionUtil != null) speechRecognitionUtil.destroy();
        binding = null;
    }



}
