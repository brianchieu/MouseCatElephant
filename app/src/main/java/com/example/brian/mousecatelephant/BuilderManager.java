package com.example.brian.mousecatelephant;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;


public class BuilderManager {

    private static int[] imageResources = new int[]{
            R.drawable.bat,
    };

    static int getImageResource() {
        return imageResources[0];
    }

    static TextInsideCircleButton.Builder getTextInsideCircleButtonBuilder() {
        return new TextInsideCircleButton.Builder()
                .normalImageRes(getImageResource())
                .normalTextRes(R.string.text_inside_circle_button_text_normal);
    }

    private BuilderManager() {
    }
}