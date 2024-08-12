package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;
import java.util.Locale;

public class SpeechRecognitionUtility {

    private SpeechRecognizer speechRecognizer;
    private Context context;
    private RecognitionListener recognitionListener;

    public SpeechRecognitionUtility(Context context, RecognitionListener recognitionListener) {
        this.context = context;
        this.recognitionListener = recognitionListener;
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(recognitionListener);
        speechRecognizer.startListening(intent);
    }

    public void stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}
