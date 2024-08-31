package com.zendalona.mathmantra.viewModels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zendalona.mathmantra.utils.NumberLineValues;

public class NumberLineViewModel extends ViewModel {

    private final MutableLiveData<Integer> currentPosition = new MutableLiveData<>(NumberLineValues.DEFAULT_POSITION);
    private final MutableLiveData<Integer> numberLineStart = new MutableLiveData<>(NumberLineValues.DEFAULT_START);
    private final MutableLiveData<Integer> numberLineEnd = new MutableLiveData<>(NumberLineValues.DEFAULT_END);

    public LiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }

    public LiveData<Integer> getNumberLineStart() {
        return numberLineStart;
    }

    public LiveData<Integer> getNumberLineEnd() {
        return numberLineEnd;
    }

    public void moveLeft() {
        int position = currentPosition.getValue() != null ? currentPosition.getValue() : NumberLineValues.DEFAULT_POSITION;
        if (position > numberLineStart.getValue()) {
            currentPosition.setValue(position - 1);
        }
    }

    public void moveRight() {
        int position = currentPosition.getValue() != null ? currentPosition.getValue() : NumberLineValues.DEFAULT_POSITION;
        if (position < numberLineEnd.getValue()) {
            currentPosition.setValue(position + 1);
        }
    }

    public void setPosition(int position) {
        currentPosition.setValue(position);
    }

    public void reset() {
        numberLineStart.setValue(NumberLineValues.DEFAULT_START);
        numberLineEnd.setValue(NumberLineValues.DEFAULT_END);
        currentPosition.setValue(NumberLineValues.DEFAULT_POSITION);
    }
}
