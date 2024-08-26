package com.zendalona.mathmantra.utils;

public class NumberLineValues {
    private static int LINE_START;
    private static int LINE_END;
    private static int CURRENT_POSITION;


    public static int getNumberLineStart() {
        return LINE_START;
    }

    public static void setNumberLineStart(int LINE_START) {
        NumberLineValues.LINE_START = LINE_START;
    }

    public static int getNumberLineEnd() {
        return LINE_END;
    }

    public static void setNumberLineEnd(int LINE_END) {
        NumberLineValues.LINE_END = LINE_END;
    }

    public static int getCurrentPosition() {
        return CURRENT_POSITION;
    }

    public static void setCurrentPosition(int currentPosition) {
        CURRENT_POSITION = currentPosition;
    }
}
