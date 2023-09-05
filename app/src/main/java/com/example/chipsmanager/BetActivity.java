package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BetActivity extends AppCompatActivity {
    private TextView max_amount;
    private TextView bet_import;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);

        ImageButton chip0 = findViewById(R.id.chip0);
        ImageButton chip1 = findViewById(R.id.chip1);
        ImageButton chip2 = findViewById(R.id.chip2);
        ImageButton chip3 = findViewById(R.id.chip3);
        ImageButton chip4 = findViewById(R.id.chip4);
        ImageButton chip5 = findViewById(R.id.chip5);
        ImageButton chip6 = findViewById(R.id.chip6);
        ImageButton chip7 = findViewById(R.id.chip7);
        ImageButton chip8 = findViewById(R.id.chip8);
        Button ok = findViewById(R.id.ok_bet);
        Button cancel = findViewById(R.id.cancel_bet);
        Button reset = findViewById(R.id.reset_bet);
        Button all = findViewById(R.id.all_bet);
        max_amount = findViewById(R.id.max_amount);
        bet_import = findViewById(R.id.bet_import);

        String type = getIntent().getStringExtra("Type");
        String lobby_name = getIntent().getStringExtra("Lobby");
        String user_name = getIntent().getStringExtra("User");
        DatabaseReference lobby_reference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name);
        DatabaseReference user_reference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players").child(user_name);
        bet_import.setText("0");

        setMaxAmount(max_amount, type, lobby_reference, user_reference);

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
            Intent intent = new Intent(BetActivity.this, LobbyActivity.class);
            intent.putExtra("Lobby", lobby_name);
            intent.putExtra("User", user_name);
            startActivity(intent);
            finish();
        });

        reset.setOnClickListener(v -> bet_import.setText("0"));

        ok.setOnClickListener(v -> {
            int selected_value = Integer.parseInt(bet_import.getText().toString());
            int max_value = Integer.parseInt(max_amount.getText().toString());
            if (selected_value > max_value) {
                Toast.makeText(BetActivity.this, "Exceeded the max amount!", Toast.LENGTH_SHORT).show();
            } else {
                if (type.equals("take")) {
                    takePartialPot(lobby_reference, user_reference, selected_value);
                } else {
                    betImport(lobby_reference, user_reference, selected_value);
                }
                Intent intent = new Intent(BetActivity.this, LobbyActivity.class);
                intent.putExtra("Lobby", lobby_name);
                intent.putExtra("User", user_name);
                startActivity(intent);
                finish();
            }
        });

        all.setOnClickListener(v -> bet_import.setText(max_amount.getText()));
    }

    private void setMaxAmount(TextView max_amount, String type, DatabaseReference lobby, DatabaseReference user) {
        if (type.equals("take")) {
            lobby.child("Pot").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    max_amount.setText(String.valueOf(snapshot.getValue()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            user.child("balance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    max_amount.setText(String.valueOf(snapshot.getValue()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void betImport(DatabaseReference lobby, DatabaseReference user, int bet_import) {
        lobby.child("Pot").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.child("balance").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        user.child("bet").get().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                int pot_dim = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                                int user_bal = Integer.parseInt(String.valueOf(task1.getResult().getValue()));
                                int user_bet = Integer.parseInt(String.valueOf(task2.getResult().getValue()));
                                pot_dim += bet_import;
                                lobby.child("Pot").setValue(pot_dim);
                                user_bal -= bet_import;
                                user.child("balance").setValue(user_bal);
                                user_bet += bet_import;
                                user.child("bet").setValue(user_bet);
                            } else {
                                Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takePartialPot(DatabaseReference lobby, DatabaseReference user, int bet_import) {
        lobby.child("Pot").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.child("balance").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        int pot_dim = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                        int user_bal = Integer.parseInt(String.valueOf(task1.getResult().getValue()));
                        pot_dim -= bet_import;
                        lobby.child("Pot").setValue(pot_dim);
                        user_bal += bet_import;
                        user.child("balance").setValue(user_bal);
                        if (pot_dim == 0) {
                            resetAllBets(lobby);
                        }
                    } else {
                        Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAllBets(DatabaseReference lobby) {
        DatabaseReference playersReference = lobby.child("Players");
        playersReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Player p = snapshot.getValue(Player.class);
                    assert p != null;
                    if (!p.isActive()) {
                        snapshot.getRef().removeValue();
                    } else {
                        snapshot.getRef().child("bet").setValue(0);
                        snapshot.getRef().child("fold").setValue(false);
                    }
                }
            } else {
                Toast.makeText(BetActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBetImport(TextView bet_import, int add_value) {
        int current_import = Integer.parseInt(bet_import.getText().toString());
        current_import += add_value;
        bet_import.setText(String.valueOf(current_import));
    }
}