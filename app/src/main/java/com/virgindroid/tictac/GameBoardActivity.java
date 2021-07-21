package com.virgindroid.tictac;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.virgindroid.tictac.databinding.ActivityGameBoardBinding;

public class GameBoardActivity extends AppCompatActivity {
    public static final int DELAY_MILLIS = 1000;
    char[] Board = Helper.board;
    ImageButton[] buttons = new ImageButton[9];
    char currentPlayer;

    ActivityGameBoardBinding binding;
    String playerName;
    String opponentName;
    char playerCharacter;
    char oppositionCharacter;
    boolean isHumanOpp;

    boolean hasHumanClicked;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_board);

        Intent data = getIntent();
        playerName = data.getStringExtra(MainActivity.extraPlayerName);
        opponentName = data.getStringExtra(MainActivity.extraOpponentName);

        playerCharacter = data.getStringExtra(MainActivity.extraPlayerCharacter).equals("Cross") ? 'x' : '0';
        oppositionCharacter = playerCharacter == 'x' ? '0' : 'x';

        binding.playerName.setText(playerName);
        binding.opponentName.setText(opponentName);
        binding.playerPlusOpponentScore.setText("0 - 0");

        isHumanOpp = !opponentName.equals("SuperBot");

        //setting up the board
        for (int i = 0; i < 9; i++) {
            String btnID = "btn_" + i;
            int btnResourceId = getResources().getIdentifier(btnID, "id", getPackageName());
            buttons[i] = findViewById(btnResourceId);
            buttons[i].setOnClickListener(this::placeTic);
        }
        newBoard();
        hasHumanClicked = false;
    }

    public void placeTic(View v) {
        nextMove(v, isHumanOpp);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void nextMove(View v, boolean isHumanOpponent) {

        String btnID = v.getResources().getResourceEntryName(v.getId());
        int userPressPos = Character.getNumericValue(btnID.charAt(btnID.length() - 1));
        if (isHumanOpponent) {
            if (Board[userPressPos] == '#') {
                int drawableId = currentPlayer == 'x' ? R.drawable.ic_tic1icon : R.drawable.ic_tic0icon;
                buttons[userPressPos].setImageDrawable(getDrawable(drawableId));
                buttons[userPressPos].setScaleX(2);
                buttons[userPressPos].setScaleY(2);
                Helper.updateBoard(Board, userPressPos, currentPlayer);
                setScoreBoard();
                currentPlayer = currentPlayer == 'x' ? '0' : 'x';
            } else {
                Toast.makeText(this.getApplication(), "Click on empty spot", Toast.LENGTH_SHORT).show();
            }
        } else {

            if (!hasHumanClicked) {
                if (Board[userPressPos] == '#') {
                    int drawableId = currentPlayer == 'x' ? R.drawable.ic_tic1icon : R.drawable.ic_tic0icon;
                    buttons[userPressPos].setImageDrawable(getDrawable(drawableId));
                    buttons[userPressPos].setScaleX(2);
                    buttons[userPressPos].setScaleY(2);
                    Helper.updateBoard(Board, userPressPos, currentPlayer);
                    setScoreBoard();
                    currentPlayer = currentPlayer == 'x' ? '0' : 'x';
                    hasHumanClicked = true;
                } else {
                    Toast.makeText(this.getApplication(), "Click on empty spot", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Ai's turn", Toast.LENGTH_SHORT).show();
            }


            //for AI player
            Handler handler = new Handler();
            handler.postDelayed(this::aiMove, DELAY_MILLIS);

        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void aiMove() {
        if (hasHumanClicked) {
            int getAiPos = Helper.getNextSmartMove(currentPlayer);
            if (getAiPos != -1) {
                int drawableId = currentPlayer == 'x' ? R.drawable.ic_tic1icon : R.drawable.ic_tic0icon;
                buttons[getAiPos].setImageDrawable(getDrawable(drawableId));
                buttons[getAiPos].setScaleX(2);
                buttons[getAiPos].setScaleY(2);
                Helper.updateBoard(Board, getAiPos, currentPlayer);
                setScoreBoard();
                currentPlayer = currentPlayer == 'x' ? '0' : 'x';
                hasHumanClicked = false;

            } else {
                (new Handler()).postDelayed(this::newBoard, DELAY_MILLIS);

            }
        }
    }


    private void setScoreBoard() {
        int gameState = Helper.checkTerminalState(currentPlayer, currentPlayer == 'x' ? '0' : 'x');
        if (gameState != -2 && gameState != 0) {
            String winner;
            String scoreText = binding.playerPlusOpponentScore.getText().toString();
            int getPlayerScore = Character.getNumericValue(scoreText.charAt(0));
            int getOpponentScore = Character.getNumericValue(scoreText.charAt(scoreText.length() - 1));
            if (currentPlayer == playerCharacter) {
                winner = playerName;
                getPlayerScore += 1;
            } else {
                winner = opponentName;
                getOpponentScore += 1;
            }
            Toast.makeText(getApplication(), winner + " Won the Game", Toast.LENGTH_SHORT).show();
            scoreText = getPlayerScore + " - " + getOpponentScore;
            binding.playerPlusOpponentScore.setText(scoreText);
            (new Handler()).postDelayed(this::newBoard, DELAY_MILLIS);
        } else if (gameState == 0) {
            (new Handler()).postDelayed(this::newBoard, DELAY_MILLIS);
        }
    }


    public void resetGame(View view) {
        for (ImageButton btn : buttons) {
            String btnID = btn.getResources().getResourceEntryName(btn.getId());
            int pos = Character.getNumericValue(btnID.charAt(btnID.length() - 1));
            Board[pos] = '#';
            btn.setImageResource(0);
            hasHumanClicked = false;
            currentPlayer = playerCharacter;
        }
    }

    private void newBoard() {
        for (ImageButton btn : buttons) {
            String btnID = btn.getResources().getResourceEntryName(btn.getId());
            int pos = Character.getNumericValue(btnID.charAt(btnID.length() - 1));
            Board[pos] = '#';
            btn.setImageResource(0);
            hasHumanClicked = false;
            currentPlayer = playerCharacter;
        }
    }

}