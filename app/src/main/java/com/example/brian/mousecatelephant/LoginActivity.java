package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button submitButton, registerButton;
    String userInput, passInput, gameMode;
    TextView loginMsg;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submitButton = (Button) findViewById(R.id.SubmitButton);
        registerButton = (Button) findViewById(R.id.RegisterButton);
        MyOnClickListener myOnClickListener = new MyOnClickListener();

        usernameField = (EditText) findViewById(R.id.userField);
        passwordField = (EditText) findViewById(R.id.passField);
        usernameField.bringToFront();
        passwordField.bringToFront();

        submitButton.setOnClickListener(myOnClickListener);
        submitButton.bringToFront();
        registerButton.setOnClickListener(myOnClickListener);
        registerButton.bringToFront();

        loginMsg = (TextView) findViewById(R.id.loginMessage);
        loginMsg.bringToFront();

        db = new DatabaseHandler(this);

        gameMode = getIntent().getStringExtra("game_mode");
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.SubmitButton:
                    //Log.d("SQLite: ", "Reading all contacts..");
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
                        if (user.getPassword().equals(passInput)) startPlay(gameMode, user.getName());
                        else loginMsg.setText("Invalid username or password");
                    }
                    catch (Exception e) {
                        if (passInput.equals(null) || passInput.equals(""))
                            loginMsg.setText("Invalid username or password.");
        /*                else {
                            db.addUser(new User(userInput, passInput));
                            loginMsg.setText("Created new user: " + userInput + ". Please re-submit.");
                        }*/
                    }
                    break;
                case R.id.RegisterButton:
                    startRegistration();
                    break;
                default:
                    break;
            }
        }
    }
    public void startPlay(String mode, String user){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("game_mode", mode);
        intent.putExtra("user_name", user);
        startActivity(intent);
    }
    public void startRegistration(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
