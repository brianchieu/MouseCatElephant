package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.sdsmdg.tastytoast.TastyToast;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;


public class RegisterActivity extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button submitButton;
    String userInput, passInput;
    private ExplosionField mExplosionField;
    TextView loginMsg;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        submitButton = (Button) findViewById(R.id.SubmitButton);
        MyOnClickListener myOnClickListener = new MyOnClickListener();

        mExplosionField = ExplosionField.attach2Window(this);

        usernameField = (EditText) findViewById(R.id.userField);
        passwordField = (EditText) findViewById(R.id.passField);
        usernameField.bringToFront();
        passwordField.bringToFront();

        submitButton.setOnClickListener(myOnClickListener);
        submitButton.bringToFront();

        loginMsg = (TextView) findViewById(R.id.loginMessage);
        loginMsg.bringToFront();

        db = new DatabaseHandler(this);
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("SQLite: ", "Reading all contacts..");
            List<User> users = db.getAllUsers();

            for (User cn : users) {
                String log = "Name: " + cn.getName() +
                        ", Pass: " + cn.getPassword();
                // Writing Contacts to log
                Log.d("SQLite: ", log);
            }

            userInput = usernameField.getText().toString();
            passInput = passwordField.getText().toString();
            User user;

            try {
                user = db.getUser(userInput);
                if (user.getName().equals(userInput) &&
                        !(user.getPassword().equals(null) || user.getPassword().equals(""))) {
                    showWarningToast(v);
                }
            }
            catch (Exception e) {
                if (userInput.equals(null) || userInput.equals("")){
                    showNameErrorToast(v);
                }
                else if (passInput.equals(null) || passInput.equals("")) {
                    showPassErrorToast(v);
                }
                else {
                    db.addUser(new User(userInput, passInput));
                    final View tmp = v;
                    addListener(tmp);
                    mExplosionField.explode(v);
                    showSuccessToast(v);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            reset(tmp);
                            BacktoLogin();
                        }
                    }, 1500);
                }
            }
        }
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
        TastyToast.makeText(getApplicationContext(), "Created new user !", TastyToast.LENGTH_SHORT,
                TastyToast.SUCCESS);
    }

    public void showWarningToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Username already taken !", TastyToast.LENGTH_SHORT,
                TastyToast.WARNING);
    }

    public void showPassErrorToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Invalid password !", TastyToast.LENGTH_SHORT,
                TastyToast.ERROR);
    }

    public void showNameErrorToast(View view) {
        TastyToast.makeText(getApplicationContext(), "Invalid username !", TastyToast.LENGTH_SHORT,
                TastyToast.ERROR);
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void BacktoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}