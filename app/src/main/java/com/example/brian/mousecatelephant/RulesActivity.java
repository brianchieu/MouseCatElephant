package com.example.brian.mousecatelephant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class RulesActivity extends AppCompatActivity {
    ImageView rulesImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        rulesImage = (ImageView) findViewById(R.id.rulesImage);
        rulesImage.setImageResource(R.drawable.rules);
    }
}
