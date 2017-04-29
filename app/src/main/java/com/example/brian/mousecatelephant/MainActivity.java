package com.example.brian.mousecatelephant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton mouseButton, catButton, elephantButton;
    ImageView playerChoice, computerChoice;
    public String difficulty, user;
    TextView result, gameDifficulty;
    int[] playerMoves = {0, 0, 0};
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        mouseButton = (ImageButton) findViewById(R.id.buttonMouse);
        mouseButton.setImageResource(R.drawable.mouse);
        catButton = (ImageButton) findViewById(R.id.buttonCat);
        catButton.setImageResource(R.drawable.cat);
        elephantButton = (ImageButton) findViewById(R.id.buttonElephant);
        elephantButton.setImageResource(R.drawable.elephant);

        playerChoice = (ImageView) findViewById(R.id.playerChoice);
        computerChoice = (ImageView) findViewById(R.id.computerChoice);
        playerChoice.setImageResource(R.drawable.question);
        computerChoice.setImageResource(R.drawable.question);

        result = (TextView) findViewById(R.id.result);
        gameDifficulty = (TextView) findViewById(R.id.difficulty);

        difficulty = getIntent().getStringExtra("game_mode");
        gameDifficulty.setText("Mode: " + difficulty);

        user = getIntent().getStringExtra("user_name");

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        mouseButton.setOnClickListener(myOnClickListener);
        catButton.setOnClickListener(myOnClickListener);
        elephantButton.setOnClickListener(myOnClickListener);
    }

    boolean soundPlaying = false;
    public void playSound(String playerMove){
        MediaPlayer mp;
        if (playerMove.equals("Mouse")) mp = MediaPlayer.create(MainActivity.this, R.raw.mouse);
        else if (playerMove.equals("Cat")) mp = MediaPlayer.create(MainActivity.this, R.raw.cat);
        else mp = MediaPlayer.create(MainActivity.this, R.raw.elephant);

        mp.start();
        soundPlaying = true;
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                soundPlaying = false;
                player.release();
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //do not respond to clicks if sound is still playing
            if (!soundPlaying) {
                //get player choice depending on which button was pressed
                String player = getPlayerChoice(v.getId());

                //set the associated image with player choice
                if (player.equals("Mouse")) {
                    //each move increments counter to keep track of move
                    playerMoves[0]++;
                    playerChoice.setImageResource(R.drawable.mouse);
                } else if (player.equals("Cat")) {
                    playerMoves[1]++;
                    playerChoice.setImageResource(R.drawable.cat);
                } else {
                    playerMoves[2]++;
                    playerChoice.setImageResource(R.drawable.elephant);
                }
                //play animal sound corresponding to player choice
                playSound(player);

                //repeat with computer choice - computer move depends on difficulty chosen
                String computer = getComputerChoice(difficulty, playerMoves);

                //change image associated with computer choice
                if (computer.equals("Mouse")) computerChoice.setImageResource(R.drawable.mouse);
                else if (computer.equals("Cat")) computerChoice.setImageResource(R.drawable.cat);
                else computerChoice.setImageResource(R.drawable.elephant);

                //display the result of the player and computer choices
                if (getResult(player, computer).equals("You tied!")) {
                    db.updateScores( user , "d");
                    int scores[] = db.getScores(user);
                    gameDifficulty.setText("Mode: " + difficulty + "\t\t" + user + "\tWins: " + scores[0] + " Losses: " + scores[1] + " Draws: " + scores[2]);
                    result.setTextColor(Color.BLACK);
                }
                else if (getResult(player, computer).equals("You lose!")){
                    db.updateScores( user, "l");
                    int scores[] = db.getScores(user);
                    gameDifficulty.setText("Mode: " + difficulty + "\t\t" + user + "\t Wins: " + scores[0] + " Losses: " + scores[1] + " Draws: " + scores[2]);
                    result.setTextColor(getColor(R.color.red));
                }
                else {
                    db.updateScores( user, "w");
                    int scores[] = db.getScores(user);
                    gameDifficulty.setText("Mode: " + difficulty + "\t\t" + user + "\t Wins: " + scores[0] + " Losses: " + scores[1] + " Draws: " + scores[2]);
                    result.setTextColor(getColor(R.color.blue));
                }

                result.setText(getResult(player, computer));
            }
        }
    }
    private static String getComputerChoice(String difficulty, int[] playerMoves){
        int mostPlayed = 0, rand;

        //in hard difficulty, computer move beats or ties with most played
        //based on player move counter
        //ex: most played is mouse - computer chooses randomly between mouse or cat
        if (difficulty.equals("HARD")) {
            mostPlayed = findMaxIndex(playerMoves);
            rand = (int) (Math.random()*2);
            if (rand == 0) rand = mostPlayed;
            else {
                switch (mostPlayed) {
                    case 2:
                        rand = 0;
                        break;
                    default:
                        rand = mostPlayed + 1;
                        break;
                }
            }
        }
        //otherwise, random move chosen from the three animals
        else rand = (int) (Math.random()*3);

        if (rand == 0){
            return "Mouse";
        }
        else if (rand == 1){
            return "Cat";
        }
        else {
            return "Elephant";
        }
    }
    private static String getPlayerChoice(int buttonPressed) {
        //return string of animal associated with player choice
        if (buttonPressed == R.id.buttonMouse){
            return "Mouse";
        }
        else if (buttonPressed == R.id.buttonCat){
            return "Cat";
        }
        else {
            return "Elephant";
        }
    }

    private static String getResult(String player, String computer){
        if (player.equals(computer)) return "You tied!";

        else if (player.equals("Mouse")){
            if (computer.equals("Elephant")) return "You win!";
            else return "You lose!";
        }

        else if (player.equals("Cat")){
            if (computer.equals("Mouse")) return "You win!";
            else return "You lose!";
        }

        else {
            if (computer.equals("Cat")) return "You win!";
            else return "You lose!";
        }
    }

    public static int findMaxIndex(int[] arr){
        boolean[] duplicateEntries = {false, false, false};
        for (int i = 0; i < arr.length-1; i++){
            for (int j = i+1; j < arr.length; j++){
                if (arr[i] == arr[j]){
                    duplicateEntries[i] = duplicateEntries[j] = true;
                }
            }
        }
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++){
            if (arr[i] > arr[maxIndex]) maxIndex = i;
        }

        //no other entries with same max value, just return max index
        if (duplicateEntries[maxIndex] == false)
            return maxIndex;
        else {
            //choose randomly among the duplicate entries
            while(true){
                int rand = (int) (Math.random()*3);
                if (duplicateEntries[rand]){
                    return rand;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
