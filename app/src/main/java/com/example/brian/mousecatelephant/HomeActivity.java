package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button exitButton, playEasyButton, playHardButton, rulesButton, multiplayButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        playEasyButton = (Button) findViewById(R.id.playButton);
        playHardButton = (Button) findViewById(R.id.playButton2);
        exitButton = (Button) findViewById(R.id.exitButton);
        rulesButton = (Button) findViewById(R.id.rulesButton);
        multiplayButton = (Button) findViewById(R.id.playButton3);

        MyOnClickListener myOnClickListener = new MyOnClickListener();

        multiplayButton.setOnClickListener(myOnClickListener);
        playEasyButton.setOnClickListener(myOnClickListener);
        playHardButton.setOnClickListener(myOnClickListener);
        exitButton.setOnClickListener(myOnClickListener);
        rulesButton.setOnClickListener(myOnClickListener);
    }
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.playButton:
                    startPlay("EASY");
                    break;
                case R.id.playButton2:
                    startPlay("HARD");
                    break;
                case R.id.playButton3:
                    startMultiplayer();
                    break;
                case R.id.exitButton:
                    finish();
                    break;
                case R.id.rulesButton:
                    showRules();
                    break;
                default:
                    break;
            }
        }
    }
    public void startPlay(String mode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game_mode", mode);
        startActivity(intent);
    }
    public void startMultiplayer(){
        Intent intent = new Intent(this, MultiplayActivity.class);
        startActivity(intent);
    }
    public void showRules() {
        Intent intent = new Intent(this, RulesActivity.class);
        startActivity(intent);
    }
}
