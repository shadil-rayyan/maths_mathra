package com.zendalona.mathmantra.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.zendalona.mathmantra.R;

public class NumberLineView extends View {

    private int currentPosition; // Start position of the mascot
    private Paint linePaint;
    private Paint numberPaint;
    private Paint mascotPaint;

    private final int numberRangeStart = -5;
    private final int numberRangeEnd = 5;
    private float gap;

    public NumberLineView(Context context) {
        super(context);
        init();
    }

    public NumberLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        currentPosition = 0;
        linePaint = new Paint();
        int lineColor = ContextCompat.getColor(getContext(), R.color.blue);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(12f);

        numberPaint = new Paint(); // Initialize Paint for numbers
        int numberColor = ContextCompat.getColor(getContext(), R.color.lightBlue);
        numberPaint.setColor(numberColor);
        numberPaint.setTextSize(40f);

        mascotPaint = new Paint(); // Paint for the mascot (emoji)
        mascotPaint.setTextSize(200f); // Larger size for the mascot
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawNumberLine(canvas);
        drawMascot(canvas);
    }

    private void drawNumberLine(Canvas canvas) {
        float startX = getWidth() * 0.01f;
        float endX = getWidth() * 0.98f;
        float centerY = getHeight() / 2f;

        canvas.drawLine(0 , centerY, getWidth(), centerY, linePaint);

        gap = (endX - startX) / (numberRangeEnd - numberRangeStart );

        // Draw numbers on the number line
        for (int number = numberRangeStart; number <= numberRangeEnd; number++) {
            float x = startX + (number - numberRangeStart) * gap;
            canvas.drawText(String.valueOf(number), x, centerY + 50f, numberPaint);
        }
    }

    private void drawMascot(Canvas canvas) {
        float centerY = getHeight() / 2f;

        gap = (float) getWidth() / (numberRangeEnd - numberRangeStart - 1);
        float mascotPosition = (currentPosition - numberRangeStart - 1) * gap;
//        float mascotPosition = -4;
        canvas.drawText("\uD83E\uDDCD\u200D♂\uFE0F", mascotPosition , centerY - 50f, mascotPaint);
    }

    public int moveLeft() {
        if (currentPosition > numberRangeStart) {
            currentPosition--;
            invalidate();
        }
        return currentPosition;
    }

    public int moveRight() {
        if (currentPosition < numberRangeEnd) {
            currentPosition++;
            invalidate();
        }
        return currentPosition;
    }

}
