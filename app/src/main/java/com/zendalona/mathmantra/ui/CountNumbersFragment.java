package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        // Inflate the layout for this fragment using View Binding
        binding = FragmentCountNumbersBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize the correct sequence and other variables
        correctSequence = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            correctSequence.add(i);
        }
        currentIndex = 0;

        // Initialize PermissionManager and request microphone permission
//        permissionManager = new PermissionManager(requireActivity());
//        permissionManager.requestMicrophonePermission();

        // Initialize SpeechRecognitionUtility
//        speechRecognitionUtil = new SpeechRecognitionUtility(getContext(), speechRecognitionListener);

        // Set up button listener to start listening
        binding.startListeningButton.setOnClickListener(v -> {
            currentIndex = 0;
            speechRecognitionUtil.startListening();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (speechRecognitionUtil != null) speechRecognitionUtil.destroy();
        binding = null;
    }

    // Handle permission result
/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // Speech recognition listener implementation
    private final SpeechRecognitionUtility.SpeechRecognitionListener speechRecognitionListener = new SpeechRecognitionUtility.SpeechRecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // Inform the user that the system is ready for speech input
        }

        @Override
        public void onBeginningOfSpeech() {
            // Indicate the beginning of speech input
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // Handle changes in the input volume
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // Handle additional data from the speech recognition buffer
        }

        @Override
        public void onEndOfSpeech() {
            // Handle the end of speech input
        }

        @Override
        public void onError(int error) {
            // Handle errors during recognition
            // Consider adding error handling or informing the user
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null && !matches.isEmpty()) {
                String spokenText = matches.get(0).toLowerCase().trim();
                validateCounting(spokenText);
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // Handle partial results if necessary
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // Handle additional events
        }
    };

    private void validateCounting(String spokenText) {
        String[] words = spokenText.split("\\s+");
        for (String word : words) {
            try {
                int number = Integer.parseInt(word);
                if (number == correctSequence.get(currentIndex)) {
                    currentIndex++;
                    if (currentIndex == correctSequence.size()) {
                        binding.promptTextView.setText("Well done! You've counted to 10.");
                        speechRecognitionUtil.stopListening();
                        return;
                    }
                } else {
                    binding.promptTextView.setText("Please start from 1 again.");
                    currentIndex = 0;
                    break;
                }
            } catch (NumberFormatException e) {
                // Handle non-integer spoken words
                binding.promptTextView.setText("Please use numbers to count.");
                break;
            }
        }
        if (currentIndex < correctSequence.size()) {
            speechRecognitionUtil.startListening();
        }
    }*/
}
