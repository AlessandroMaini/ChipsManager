package com.example.chipsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BetActivity extends AppCompatActivity {
    private ImageButton chip0;
    private ImageButton chip1;
    private ImageButton chip2;
    private ImageButton chip3;
    private ImageButton chip4;
    private ImageButton chip5;
    private ImageButton chip6;
    private ImageButton chip7;
    private ImageButton chip8;
    private Button ok;
    private Button cancel;
    private Button reset;
    private TextView bet_import;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        chip0 = findViewById(R.id.chip0);
        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);
        ok = findViewById(R.id.ok_bet);
        cancel = findViewById(R.id.cancel_bet);
        reset = findViewById(R.id.reset_bet);
        bet_import = findViewById(R.id.bet_import);
    
        String type = getIntent().getStringExtra("Type");
        String lobby_name = getIntent().getStringExtra("Lobby");
        String user_name = getIntent().getStringExtra("User");
        bet_import.setText("0");

        chip0.setOnClickListener(v -> showBetImport(bet_import, 10));

        chip1.setOnClickListener(v -> showBetImport(bet_import, 20));

        chip2.setOnClickListener(v -> showBetImport(bet_import, 50));

        chip3.setOnClickListener(v -> showBetImport(bet_import, 100));

        chip4.setOnClickListener(v -> showBetImport(bet_import, 200));

        chip5.setOnClickListener(v -> showBetImport(bet_import, 500));

        chip6.setOnClickListener(v -> showBetImport(bet_import, 1000));

        chip7.setOnClickListener(v -> showBetImport(bet_import, 5000));

        chip8.setOnClickListener(v -> showBetImport(bet_import, 10000));

        cancel.setOnClickListener(v -> {
            startActivity(new Intent(BetActivity.this, LobbyActivity.class));
            finish();
        });
        
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bet_import.setText("0");
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("take")) {
                    takePartialPot(lobby_name, user_name, bet_import);
                } else {
                    betImport(lobby_name, user_name, bet_import);
                }
                startActivity(new Intent(BetActivity.this, LobbyActivity.class));
                finish();
            }
        });
    }

    private void betImport(String lobby_name, String user_name, TextView bet_import) {
    }

    private void takePartialPot(String lobby_name, String user_name, TextView bet_import) {
        
    }

    private void showBetImport(TextView bet_import, int add_value) {
        int current_import = Integer.parseInt(bet_import.getText().toString());
        current_import += add_value;
        bet_import.setText(String.valueOf(current_import));
    }
}