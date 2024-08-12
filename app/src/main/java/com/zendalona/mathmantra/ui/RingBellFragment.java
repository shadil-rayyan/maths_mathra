package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentRingBellBinding;
import com.zendalona.mathmantra.utils.AccelerometerUtility;
import com.zendalona.mathmantra.utils.SoundEffectUtility;

public class RingBellFragment extends Fragment {

    private FragmentRingBellBinding binding;
    private AccelerometerUtility accelerometerUtility;
    private SoundEffectUtility soundEffectUtility;
    int count;

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
        count = 0;
        return binding.getRoot();
    }

    private void ringBell() {
        binding.ringCount.setText(String.valueOf(++count));
        binding.bellAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.bell_ring);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start a thread to check for shakes
        new Thread(() -> {
            while (isVisible()) {
                try {Thread.sleep(200);} catch (InterruptedException e) {
                    Log.d("Errrr","Errrr");
                    e.printStackTrace();}
                if (accelerometerUtility.isDeviceShaken()) requireActivity().runOnUiThread(() -> ringBell());
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
    }
}