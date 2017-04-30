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
import java.util.Timer;
import java.util.TimerTask;
import com.sdsmdg.tastytoast.TastyToast;
import com.yalantis.phoenix.PullToRefreshView;

import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;



public class LoginActivity extends AppCompatActivity {
    EditText usernameField, passwordField;
    Button submitButton, registerButton;
    String userInput, passInput, gameMode;
    TextView loginMsg;
    DatabaseHandler db;
    private PullToRefreshView mPullToRefreshView;
    private ExplosionField mExplosionField;
    private BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 300);
            }
        });

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

        mExplosionField = ExplosionField.attach2Window(this);

        gameMode = getIntent().getStringExtra("game_mode");

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
        bmb.addBuilder(BuilderManager.getTextInsideCircleButtonBuilder());
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
                        } else {
                            showErrorToast(v);
                        }
                    } catch (Exception e) {
                        if (passInput.equals(null) || passInput.equals("")) showErrorToast(v);
                        else {
                            db.addUser(new User(userInput, passInput));
                            reset(v);
                            showWarningToast(v);
                        }
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

        public void startPlay(String mode, String user) {
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
            TastyToast.makeText(getApplicationContext(), "Let's go !", TastyToast.LENGTH_SHORT,
                    TastyToast.SUCCESS);
        }

        public void showWarningToast(View view) {
            TastyToast.makeText(getApplicationContext(), "Created new user ! \nSubmit again to login !", TastyToast.LENGTH_SHORT,
                    TastyToast.WARNING);
        }

        public void showErrorToast(View view) {
            TastyToast.makeText(getApplicationContext(), "Invalid username or password !", TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR);
        }

        public void startRegistration() {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }

        @Override
        public void onBackPressed() {
            // Do Here what ever you want do on back press;
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
}

