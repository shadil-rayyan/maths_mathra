package com.zendalona.mathmantra.ui;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.databinding.FragmentNumberLineBinding;
import com.zendalona.mathmantra.enums.Direction;
import com.zendalona.mathmantra.utils.NumberLineValues;
import com.zendalona.mathmantra.utils.TTSUtility;

public class NumberLineFragment extends Fragment {

    private FragmentNumberLineBinding binding;
    private TTSUtility tts;
    private final String CURRENT_POSITION = "You're standing on number : \n";

    public NumberLineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(1.25f);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Lock orientation to landscape when this fragment is visible
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ActionBar actionBar = requireActivity().getActionBar();
        if(actionBar != null) actionBar.setTitle("Number Line");
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);

        NumberLineValues.setNumberLineStart(-5);
        NumberLineValues.setCurrentPosition(0);
        NumberLineValues.setNumberLineEnd(5);
        binding.numberLineView.reDrawNumberLine();
        binding.currentPositionTv.setText(CURRENT_POSITION + NumberLineValues.getCurrentPosition());

        binding.btnLeft.setOnClickListener(v -> moveCharacter(Direction.LEFT));
        binding.btnRight.setOnClickListener(v -> moveCharacter(Direction.RIGHT));
        return binding.getRoot();
    }

    private void moveCharacter(Direction direction) {
        int startPosition = NumberLineValues.getNumberLineStart();
        int endPosition = NumberLineValues.getNumberLineEnd();
        int currentPosition = NumberLineValues.getCurrentPosition();

        Log.d("moveCharacter() : Starting : ", String.valueOf(startPosition));
        Log.d("moveCharacter() : CurrentPosition : ", String.valueOf(currentPosition));
        Log.d("moveCharacter() : Ending : ", String.valueOf(endPosition));

        if(currentPosition < endPosition && currentPosition > startPosition){
            switch (direction){
                case LEFT:
                    NumberLineValues.setCurrentPosition(binding.numberLineView.moveLeft());
                    break;
                case RIGHT:
                    NumberLineValues.setCurrentPosition(binding.numberLineView.moveRight());
                    break;
                default: Log.d("Number Line", "Direction Unknown!");
            }
        }
        else {
            // TODO : redraw number line with current position as start/end position
            binding.numberLineView.reDrawNumberLine();
        }
        tts.speak(Integer.toString(currentPosition));
        binding.currentPositionTv.setText(CURRENT_POSITION + currentPosition);

    }
}