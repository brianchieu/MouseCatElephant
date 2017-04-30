package com.example.brian.mousecatelephant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button submitButton;
    String userInput, passInput, passConfirmInput;
    TextView loginMsg;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                loginMsg.setText("Username already taken.");
            }
            catch (Exception e) {
                if (userInput.equals(null) || userInput.equals(""))
                    loginMsg.setText("Invalid username.");
                else if (passInput.equals(null) || passInput.equals(""))
                    loginMsg.setText("Invalid password.");
                else {
                    db.addUser(new User(userInput, passInput));
                    loginMsg.setText("Created new user: " + userInput + ".");
                }
            }
        }
    }
}