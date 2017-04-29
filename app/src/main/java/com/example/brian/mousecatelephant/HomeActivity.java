package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.LinearGradient;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import android.view.ViewGroup;
import android.view.View;
import tyrantgit.explosionfield.ExplosionField;



public class HomeActivity extends AppCompatActivity {
    Button exitButton, playEasyButton, playHardButton, rulesButton, multiplayButton;
    TextView text;
    private ExplosionField mExplosionField;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        db = new DatabaseHandler(this);
        db.deleteAll();
        //insert
        //Log.d("SQLite", "Inserting ...");
        db.addUser(new User("GGG", "1233211232"));
        /*Log.d("SQLite", "Inserting ...");
        db.addUser(new User("BBB", "999211232"));
        Log.d("SQLite", "Inserting ...");
        db.addUser(new User("CCC", "999211232"));
        Log.d("SQLite", "Inserting ...");
        db.addUser(new User("DDD", "999211232"));

        Log.d("SQLite: ", "Reading all contacts..");*/
/*        List<User> users = db.getAllUsers();

        for (User cn : users) {
            String log = "Name: " + cn.getName() +
                    ", Pass: " + cn.getPassword();
            // Writing Contacts to log
            Log.d("SQLite: ", log);
        }*/


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

        text = (TextView) findViewById(R.id.Mqtext);
        text.bringToFront();

        mExplosionField = ExplosionField.attach2Window(this);

        int[] color = {Color.MAGENTA,Color.CYAN};
        float[] position = {0, 1};
        TileMode tile_mode = TileMode.REPEAT;
        LinearGradient lin_grad = new LinearGradient(0, 0, 0, 35,color,position, tile_mode);
        Shader shader_gradient = lin_grad;
        text.getPaint().setShader(shader_gradient);


    }
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View v){
            switch(v.getId()){
                case R.id.playButton:
                    addListener(findViewById(R.id.playButton));
                    mExplosionField.explode(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(findViewById(R.id.playButton)); // Reset the view after explosion
                            startPlay("EASY");
                        }
                    }, 1200);
                    break;

                case R.id.playButton2:
                    addListener(findViewById(R.id.playButton2));
                    mExplosionField.explode(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(findViewById(R.id.playButton2)); // Reset the view after explosion
                            startPlay("HARD");
                        }
                    }, 1200);
                    break;

                case R.id.playButton3:
                    addListener(findViewById(R.id.playButton3));
                    mExplosionField.explode(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(findViewById(R.id.playButton3)); // Reset the view after explosion
                            startMultiplayer();
                        }
                    }, 1200);
                    break;

                case R.id.exitButton:
                    addListener(findViewById(R.id.exitButton));
                    mExplosionField.explode(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(findViewById(R.id.exitButton)); // Reset the view after explosion
                            finish();
                        }
                    }, 1200);
                    break;

                case R.id.rulesButton:
                    addListener(findViewById(R.id.rulesButton));
                    mExplosionField.explode(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(findViewById(R.id.rulesButton)); // Reset the view after explosion
                            showRules();
                        }
                    }, 1200);
                    break;

                default:
                    break;
            }
        }
    }

    public void startPlay(String mode) {
        //Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, LoginActivity.class);
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

    private void addListener(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                addListener(parent.getChildAt(i));
            }
        } else {
            root.setClickable(true);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExplosionField.explode(v);
                    v.setOnClickListener(null);
                }
            });
        }
    }

    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

}
