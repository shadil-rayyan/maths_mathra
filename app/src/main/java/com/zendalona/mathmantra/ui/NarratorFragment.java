package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentNarratorBinding;
import com.zendalona.mathmantra.databinding.FragmentRingBellBinding;
import com.zendalona.mathmantra.utils.TTSUtility;

public class NarratorFragment extends Fragment {

    private FragmentNarratorBinding binding;
    private TTSUtility tts;

    private String[] theoryContents = {
            "A long time ago in India, there was a very smart person named Aryabhatta. He loved learning about the stars and numbers. Aryabhatta did something very important for all of us: he helped people understand the number zero.",
                    "Imagine you have a few sweets, but then you give them all away. You don't have any sweets left, right? Aryabhatta showed everyone that we can use the number zero to show that there's nothing left. Before Aryabhatta, people didn't have a way to write down 'zero' or 'nothing,' so his idea was really special." ,
                    "Aryabhatta's discovery of zero was like finding a missing piece of a puzzle. It helped people count and do math better. Thanks to him, we can do things like counting, adding, and even using technology like computers, all because we understand zero. Aryabhatta's ideas are still very important today, and we're grateful for his great work in India!",
            "Zero is a special number. It means nothing. When we have zero cookies, it means we have no cookies at all." +
                    " Zero is also like a superhero in math! It helps us count, even when there's nothing." +
                    " For example, when we start counting, we say zero, one, two, three. Zero is the start. " +
                    "And if you add zero to any number, that number stays the same." +
                    " Zero is cool because it helps us know when there is nothing.",
    };
    private int currentIndex = 0;


    public NarratorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(0.8f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNarratorBinding.inflate(inflater, container, false);
        updateTheoryContent();
        binding.repeatButton.setOnClickListener(v -> tts.speak(binding.theoryText.getText().toString()));
        binding.previousButton.setOnClickListener(v -> {
            currentIndex = (currentIndex - 1) % theoryContents.length;
            updateTheoryContent();
        });
        binding.nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % theoryContents.length;
            updateTheoryContent();
        });
        return binding.getRoot();
    }

    private void updateTheoryContent() {
        String content = theoryContents[currentIndex];
        binding.theoryText.setText(content);
        tts.speak(content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tts.shutdown();
    }
}