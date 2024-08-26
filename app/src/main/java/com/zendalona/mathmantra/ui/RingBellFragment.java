package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentRingBellBinding;
import com.zendalona.mathmantra.utils.AccelerometerUtility;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.SoundEffectUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

public class RingBellFragment extends Fragment {

    private FragmentRingBellBinding binding;
    private AccelerometerUtility accelerometerUtility;
    private SoundEffectUtility soundEffectUtility;
    private RandomValueGenerator randomValueGenerator;
    private TTSUtility tts;
    int count, target;

    public RingBellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
        accelerometerUtility = new AccelerometerUtility(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRingBellBinding.inflate(inflater, container, false);
        randomValueGenerator = new RandomValueGenerator();
        tts = new TTSUtility(requireContext());
        count = 0;
        target = randomValueGenerator.generateNumberForBellRing();
        String targetText = "Ring the bell" + target + " times";
        tts.speak(targetText);
        binding.ringMeTv.setText(targetText);
        return binding.getRoot();
    }

    private void ringBell() {
        binding.ringCount.setText(String.valueOf(++count));
        binding.bellAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.bell_ring);
        if(count == target){

        }
        //TODO : if count == askedFor(<=9), display appreciation popup ; set count = 0 and a new asked for
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start a thread to check for shakes
        new Thread(() -> {
            while (isVisible()) {
                try {
                    Thread.sleep(200);
                }
                catch (InterruptedException e) {
                    Log.d("Accelerometer Thread sleep Error",e.getLocalizedMessage());
                    e.printStackTrace();
                }
                if (accelerometerUtility.isDeviceShaken()) requireActivity().runOnUiThread(this::ringBell);
            }
        }).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        accelerometerUtility.unregisterListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tts.shutdown();
    }
}