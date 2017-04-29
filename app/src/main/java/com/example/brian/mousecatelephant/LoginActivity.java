package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import tyrantgit.explosionfield.ExplosionField;
import android.view.View;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.sdsmdg.tastytoast.TastyToast;



public class LoginActivity extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button submitButton;
    String userInput, passInput, gameMode;
    TextView loginMsg;
    DatabaseHandler db;
    private ExplosionField mExplosionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        submitButton = (Button) findViewById(R.id.SubmitButton);
        MyOnClickListener myOnClickListener = new MyOnClickListener();


        usernameField = (EditText) findViewById(R.id.userField);
        passwordField = (EditText) findViewById(R.id.passField);
        usernameField.bringToFront();
        passwordField.bringToFront();

        submitButton.setOnClickListener(myOnClickListener);
        submitButton.bringToFront();

        loginMsg = (TextView) findViewById(R.id.loginMessage);
        loginMsg.bringToFront();

        db = new DatabaseHandler(this);

        mExplosionField = ExplosionField.attach2Window(this);

        gameMode = getIntent().getStringExtra("game_mode");
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Log.d("SQLite: ", "Reading all contacts..");
            /*List<User> users = db.getAllUsers();

            for (User cn : users) {
                String log = "Name: " + cn.getName() +
                        ", Pass: " + cn.getPassword();
                // Writing Contacts to log
                Log.d("SQLite: ", log);
            }*/

            userInput = usernameField.getText().toString();
            passInput = passwordField.getText().toString();
            User user;

            try {
                user = db.getUser(userInput);
                if (user.getPassword().equals(passInput)) {
                    final View tmp = v;
                    addListener(tmp);
                    mExplosionField.explode(v);
                    showSuccessToast(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(tmp);// Reset the view after explosion
                            startPlay(gameMode, db.getUser(userInput).getName());
                        }
                    }, 1200);
                }

                else {
                    showErrorToast(v);
                }
            }
            catch (Exception e){
                if (passInput.equals(null) || passInput.equals("")) loginMsg.setText("Please enter a valid password for new user.");
                else {
                    db.addUser(new User(userInput, passInput));
                    reset(v);
                    showWarningToast(v);
                }
            }
        }
    }

    public void startPlay(String mode, String user){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game_mode", mode);
        intent.putExtra("user_name", user);
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

    public void showSuccessToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Let's go !", TastyToast.LENGTH_LONG,
                TastyToast.SUCCESS);
    }

    public void showWarningToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Created new user !", TastyToast.LENGTH_LONG,
                TastyToast.WARNING);
    }

    public void showErrorToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Invalid username or password !", TastyToast.LENGTH_LONG,
                TastyToast.ERROR);
    }

}
