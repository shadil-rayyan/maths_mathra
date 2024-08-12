package com.zendalona.mathmantra.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentTapTablaBinding;
import com.zendalona.mathmantra.utils.SoundEffectUtility;

public class TapTablaFragment extends Fragment {

    private int count;
    private FragmentTapTablaBinding binding;
    private SoundEffectUtility soundEffectUtility;

    public TapTablaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTapTablaBinding.inflate(inflater, container, false);

        count = 0;

        LottieAnimationView tablaAnimationView = binding.tablaAnimationView;
        tablaAnimationView.setOnClickListener(v -> onTablaTapped());

        return binding.getRoot();
    }

    private void onTablaTapped() {
        binding.tapCount.setText(String.valueOf(++count));
        binding.tablaAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.drums_sound);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release resources related to binding
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release sound effect resources when fragment is destroyed
        // FIXME : soundEffectUtility.release();
    }
}
