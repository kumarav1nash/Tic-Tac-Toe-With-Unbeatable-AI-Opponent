package com.virgindroid.tictac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.virgindroid.tictac.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String extraPlayerName = "com.virgindroid.tictac.playername";
    public static final String extraPlayerCharacter = "com.virgindroid.tictac.playerside";
    public static final String extraOpponentName = "com.virgindroid.tictac.opponentname";
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.continueBtn.setOnClickListener(this::openGameActivity);
        binding.opponentSelectionGp.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton getSelectedOpponent = findViewById(binding.opponentSelectionGp.getCheckedRadioButtonId());
            if (getSelectedOpponent == binding.humanRadioBtn) {
                binding.opponentNameEditText.setVisibility(View.VISIBLE);
                binding.opponentNameEditText.setEnabled(true);
                Toast.makeText(getApplicationContext(), "human Selected", Toast.LENGTH_SHORT).show();
            } else {
                binding.opponentNameEditText.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "AI Selected", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void openGameActivity(View v) {
        String playerName = "Player 1";
        String playerSide = "Cross";
        String opponentName = "Player 2";
        RadioButton getSelectedBtn = findViewById(binding.playerSide.getCheckedRadioButtonId());
        if (getSelectedBtn == binding.crossSide) {
            playerSide = "Cross";
        } else if (getSelectedBtn == binding.zeroSide) {
            playerSide = "Zero";
        }
        if (binding.playerNameEditText.getEditText() != null) {
            String n = binding.playerNameEditText.getEditText().getText().toString();
            if (!n.equals(""))
                playerName = n;
        }

        RadioButton getOpponentInfo = findViewById(binding.opponentSelectionGp.getCheckedRadioButtonId());
        if (getOpponentInfo == binding.humanRadioBtn) {
            if (binding.opponentNameEditText.getEditText() != null) {
                String n = binding.opponentNameEditText.getEditText().getText().toString();
                if (!n.equals(""))
                    opponentName = n;
            }
        } else {
            opponentName = "SuperBot";
        }
        Intent intent = new Intent(this, GameBoardActivity.class);
        intent.putExtra(extraPlayerName, playerName);
        intent.putExtra(extraPlayerCharacter, playerSide);
        intent.putExtra(extraOpponentName, opponentName);
        startActivity(intent);
    }
}
