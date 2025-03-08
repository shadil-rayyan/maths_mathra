package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zendalona.mathmantra.R;

public class UserGuideFragment extends Fragment {

    public UserGuideFragment() {
        super(R.layout.fragment_userguide);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ScrollView scrollView = view.findViewById(R.id.scrollView);
        TextView tvUserGuide = view.findViewById(R.id.tvUserGuide);
        Button btnGoToTop = view.findViewById(R.id.btnGoToTop);

        // Load user guide text
        String userGuideText =
                "Maths Manthraa User Guide\n\n" +
                        "1. Introduction\n\n" +
                        "Maths Manthraa is an innovative and inclusive math learning app designed for students of all abilities. " +
                        "With a variety of interactive modes, including touch, motion, and sound-based learning, it makes math engaging and accessible.\n\n" +

                        "2. How to Use Maths Manthraa?\n\n" +
                        "Maths Manthraa provides multiple game and learning modes, designed to reinforce core mathematical concepts through fun, interactive exercises.\n\n" +

                        "3. Features Overview\n\n" +
                        "Game Modes\n" +
                        "1. Quick Play Mode\n" +
                        "   - Instantly starts a random math challenge without navigating menus.\n\n" +
                        "2. Drum Mode\n" +
                        "   - Tap the screen to match the required count.\n\n" +
                        "3. Bell Mode\n" +
                        "   - Shake the phone to count bells.\n\n" +
                        "4. Drawing Mode\n" +
                        "   - Draw basic shapes for spatial awareness.\n\n" +
                        "5. Directional Mode\n" +
                        "   - Rotate the device to match target directions.\n\n" +
                        "6. Touch the Screen Mode\n" +
                        "   - Place a specific number of fingers on the screen.\n\n" +
                        "7. Stereo Sound Mode\n" +
                        "   - Solve math problems using stereo audio cues.\n\n" +
                        "8. MCQ Mode\n" +
                        "   - Answer multiple-choice math questions.\n\n" +

                        "Learning Modes\n" +
                        "1. Currency Mode\n" +
                        "   - Learn to manage money with real-life purchase scenarios.\n\n" +
                        "2. Time Mode\n" +
                        "   - Solve time-related math problems.\n\n" +
                        "3. Distance Mode\n" +
                        "   - Calculate speed and distance problems.\n\n" +
                        "4. Core Arithmetic\n" +
                        "   - Basic addition, subtraction, multiplication, and division.\n\n" +
                        "5. Number Line Mode\n" +
                        "   - Swipe to solve problems on a number line.\n\n" +

                        "4. Accessibility Features\n\n" +
                        "- Fully supports TalkBack and custom accessibility gestures.\n" +
                        "- Text-to-Speech (TTS) guides users through menus and exercises.\n\n" +

                        "5. Settings\n\n" +
                        "- Customize sound, speech, and high-contrast mode.\n" +
                        "- Enable or disable background music and vibrations.\n\n" +

                        "6. Quit Option\n\n" +
                        "- Users are prompted with a confirmation dialog before exiting the app.\n\n" +

                        "7. Localization\n\n" +
                        "- Supports multiple languages for a wider reach.\n\n" +

                        "Enjoy learning with Maths Manthraa!";

        tvUserGuide.setText(userGuideText);

        // Scroll to the top when "Go To Top" button is clicked
        btnGoToTop.setOnClickListener(v -> scrollView.smoothScrollTo(0, 0));
    }
}
