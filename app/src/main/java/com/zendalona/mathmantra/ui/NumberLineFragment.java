package com.zendalona.mathmantra.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zendalona.mathmantra.R;
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
        NumberLineValues.setNumberLineStart(-6);
        NumberLineValues.setCurrentPosition(0);
        NumberLineValues.setNumberLineEnd(6);
//        requireActivity().getActionBar().setTitle("Number Line");
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNumberLineBinding.inflate(inflater, container, false);
        binding.btnLeft.setOnClickListener(v -> moveCharacter(Direction.LEFT));
        binding.btnRight.setOnClickListener(v -> moveCharacter(Direction.RIGHT));
        return binding.getRoot();
    }

    private void moveCharacter(Direction direction) {
        int startPosition = binding.numberLineView.getNumberRangeStart();
        int endPosition = binding.numberLineView.getNumberRangeEnd();
        int currentPosition = NumberLineValues.getCurrentPosition();
        switch(direction){
            case LEFT : {
                currentPosition = binding.numberLineView.moveLeft();
                NumberLineValues.setCurrentPosition(currentPosition);
                NumberLineValues.setNumberLineEnd(currentPosition);
                NumberLineValues.setNumberLineStart(currentPosition - 12);
                break;
            }
            case RIGHT : {
                currentPosition = binding.numberLineView.moveRight();
                NumberLineValues.setNumberLineEnd(currentPosition - 12);
                NumberLineValues.setNumberLineStart(currentPosition);
                NumberLineValues.setCurrentPosition(currentPosition);
                break;
            }
            default : currentPosition = 0;
        }
        binding.currentPositionTv.setText(CURRENT_POSITION + currentPosition);
        tts.speak(Integer.toString(currentPosition));
        Log.d("Start : ", String.valueOf(NumberLineValues.getNumberLineStart()));
        Log.d("Current : ", String.valueOf(NumberLineValues.getCurrentPosition()));
        Log.d("End : ", String.valueOf(NumberLineValues.getNumberLineEnd()));

        if(NumberLineValues.getCurrentPosition() == NumberLineValues.getNumberLineEnd()
        || NumberLineValues.getCurrentPosition() == NumberLineValues.getNumberLineStart()){
            // scroll number line
            Log.d("setNumberRange() called w/ params","," );
            binding.numberLineView.reDrawNumberLine();
        }

    }


}