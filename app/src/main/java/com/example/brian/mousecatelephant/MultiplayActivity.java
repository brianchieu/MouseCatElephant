package com.example.brian.mousecatelephant;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MultiplayActivity extends AppCompatActivity {
    ImageButton mouseButton, catButton, elephantButton;
    Button replayButton;
    ImageView player1Choice, player2Choice;
    TextView result, turn, player1Wins, player2Wins;
    int playerTurn, player1WinCount, player2WinCount;
    boolean bothDone, replayOn;
    String player1Move = "", player2Move = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplay);

        mouseButton = (ImageButton) findViewById(R.id.buttonMouse);
        mouseButton.setImageResource(R.drawable.mouse);
        catButton = (ImageButton) findViewById(R.id.buttonCat);
        catButton.setImageResource(R.drawable.cat);
        elephantButton = (ImageButton) findViewById(R.id.buttonElephant);
        elephantButton.setImageResource(R.drawable.elephant);

        replayButton = (Button) findViewById(R.id.replay);

        player1Choice = (ImageView) findViewById(R.id.player1);
        player2Choice = (ImageView) findViewById(R.id.player2);
        player1Choice.setImageResource(R.drawable.question);
        player2Choice.setImageResource(R.drawable.question);

        turn = (TextView) findViewById(R.id.playerTurn);
        result = (TextView) findViewById(R.id.result);
        player1Wins = (TextView) findViewById(R.id.player1wins);
        player2Wins = (TextView) findViewById(R.id.player2wins);
        player1Wins.setText("Player 1:  0 wins");
        player2Wins.setText("Player 2:  0 wins");

        turn.setText("PLAYER 1 TURN");
        turn.setTypeface(null, Typeface.BOLD);
        turn.setTextColor(Color.BLUE);

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        mouseButton.setOnClickListener(myOnClickListener);
        catButton.setOnClickListener(myOnClickListener);
        elephantButton.setOnClickListener(myOnClickListener);
        replayButton.setOnClickListener(myOnClickListener);

        playerTurn = 0;
        bothDone = false;
        replayOn = true;

        player1WinCount = player2WinCount = 0;
    }
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //replay button pressed
            if (v.getId() == R.id.replay) {
                //if (playerTurn == 0){
                    player1Choice.setImageResource(R.drawable.question);
                    player2Choice.setImageResource(R.drawable.question);
                    result.setText("");
                    turn.setText("PLAYER 1 TURN");
                    turn.setTypeface(null, Typeface.BOLD);
                    turn.setTextColor(Color.BLUE);
                    playerTurn = 0;
                    bothDone = false;
                    replayOn = true;
                //}
            }
            //animal button pressed
            else if (replayOn){
                if (playerTurn == 0) {
                    player1Move = getPlayerChoice(v.getId());
                    playerTurn = 1;
                    player1Choice.setImageResource(R.drawable.question);
                    player2Choice.setImageResource(R.drawable.question);
                    result.setText("");
                    turn.setText("PLAYER 2 TURN"); turn.setTypeface(null, Typeface.BOLD); turn.setTextColor(Color.RED);
                } else if (playerTurn == 1) {
                    player2Move = getPlayerChoice(v.getId());
                    playerTurn = 0;
                    turn.setText(""); turn.setTypeface(null, Typeface.BOLD);
                    bothDone = true;
                }

                //when both players have chosen, show the result
                if (bothDone) {
                    if (player1Move.equals("Mouse")) player1Choice.setImageResource(R.drawable.mouse);
                    else if (player1Move.equals("Cat")) player1Choice.setImageResource(R.drawable.cat);
                    else player1Choice.setImageResource(R.drawable.elephant);

                    if (player2Move.equals("Mouse")) player2Choice.setImageResource(R.drawable.mouse);
                    else if (player2Move.equals("Cat")) player2Choice.setImageResource(R.drawable.cat);
                    else player2Choice.setImageResource(R.drawable.elephant);

                    if (getResult(player1Move, player2Move).equals("You tied!")) result.setTextColor(Color.BLACK);
                    else if (getResult(player1Move, player2Move).equals("Player 2 wins!")) result.setTextColor(getColor(R.color.red));
                    else result.setTextColor(getColor(R.color.blue));

                    result.setText(getResult(player1Move, player2Move));

                    if (getResult(player1Move, player2Move).equals("Player 1 wins!")){
                        if (player1WinCount == 0) player1Wins.setText("Player 1:  " + Integer.toString(++player1WinCount) + " win");
                        else player1Wins.setText("Player 1:  " + Integer.toString(++player1WinCount) + " wins");
                    }
                    else if (getResult(player1Move, player2Move).equals("Player 2 wins!"))
                        if (player2WinCount == 0) player2Wins.setText("Player 2:  " + Integer.toString(++player2WinCount) + " win");
                        else player2Wins.setText("Player 2:  " + Integer.toString(++player2WinCount) + " wins");

                    bothDone = false;
                    replayOn = false;
                }
            }
        }
    }

    private static String getPlayerChoice(int buttonPressed) {
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
            if (computer.equals("Elephant")) return "Player 1 wins!";
            else return "Player 2 wins!";
        }

        else if (player.equals("Cat")){
            if (computer.equals("Mouse")) return "Player 1 wins!";
            else return "Player 2 wins!";
        }

        else {
            if (computer.equals("Cat")) return "Player 1 wins!";
            else return "Player 2 wins!";
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
