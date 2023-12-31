package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LobbyActivity extends AppCompatActivity {
    private TextView pot_size;
    private TextView player_name;
    private TextView player_balance;
    private TextView current_bet;
    private TextView fold_signal;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Button exit = findViewById(R.id.exit_lobby);
        ListView lobby_players = findViewById(R.id.lobby_players);
        TextView current_lobby = findViewById(R.id.lobby_name);
        pot_size = findViewById(R.id.pot_size);
        player_name = findViewById(R.id.player_name);
        player_balance = findViewById(R.id.player_balance);
        current_bet = findViewById(R.id.current_bet);
        fold_signal = findViewById(R.id.fold_signal);
        Button take_all = findViewById(R.id.take_all);
        Button take_partial = findViewById(R.id.take_partial);
        Button bet = findViewById(R.id.bet_button);
        Button fold = findViewById(R.id.fold_button);

        List<String> players = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, players);
        lobby_players.setAdapter(adapter);

        Intent intent = getIntent();
        String lobby_name = intent.getStringExtra("Lobby");
        String user_name = intent.getStringExtra("User");

        DatabaseReference playersReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players");
        DatabaseReference potReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Pot");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players").child(user_name);

        exit.setOnClickListener(v -> handleExitUser(userReference));

        playersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                players.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Player p = dataSnapshot.getValue(Player.class);
                    assert p != null;
                    if (p.getBet() != 0 || p.isActive()) {
                        String txt = p.getName() + ": " + p.getBalance() + "   Bet: " + p.getBet();
                        if (p.isFold()) {
                            txt += "   FOLDED";
                        }
                        if (p.getBalance() == 0 && p.getBet() != 0) {
                            txt += "   ALL IN";
                        }
                        players.add(txt);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        current_lobby.setText(lobby_name);

        userReference.child("name").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                player_name.setText(String.valueOf(task.getResult().getValue()));
            } else {
                Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });

        potReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pot_size.setText(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child("bet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                current_bet.setText(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                player_balance.setText(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userReference.child("fold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean folded = Boolean.parseBoolean(String.valueOf(snapshot.getValue()));
                if (folded) {
                    fold_signal.setVisibility(View.VISIBLE);
                } else {
                    fold_signal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        take_all.setOnClickListener(v -> {
            if (fold_signal.getVisibility() == View.VISIBLE) {
                Toast.makeText(LobbyActivity.this, "You've folded!", Toast.LENGTH_SHORT).show();
            } else {
                withdrawAllPot(user_name, lobby_name);
            }
        });

        take_partial.setOnClickListener(v -> {
            if (fold_signal.getVisibility() == View.VISIBLE) {
                Toast.makeText(LobbyActivity.this, "You've folded!", Toast.LENGTH_SHORT).show();
            } else {
                potReference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int pot_dim = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                        if (pot_dim == 0) {
                            Toast.makeText(LobbyActivity.this, "Empty pot!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent take_intent = new Intent(LobbyActivity.this, BetActivity.class);
                            take_intent.putExtra("Type", "take");
                            take_intent.putExtra("Lobby", lobby_name);
                            take_intent.putExtra("User", user_name);
                            startActivity(take_intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bet.setOnClickListener(v -> {
            if (fold_signal.getVisibility() == View.VISIBLE) {
                Toast.makeText(LobbyActivity.this, "You've folded!", Toast.LENGTH_SHORT).show();
            } else {
                userReference.child("balance").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int user_bal = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                        if (user_bal == 0) {
                            Toast.makeText(LobbyActivity.this, "Empty balance!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent take_intent = new Intent(LobbyActivity.this, BetActivity.class);
                            take_intent.putExtra("Type", "bet");
                            take_intent.putExtra("Lobby", lobby_name);
                            take_intent.putExtra("User", user_name);
                            startActivity(take_intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fold.setOnClickListener(v -> {
            if (fold_signal.getVisibility() == View.VISIBLE) {
                Toast.makeText(LobbyActivity.this, "Already folded!", Toast.LENGTH_SHORT).show();
            } else {
                userReference.child("balance").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int user_bal = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                        if (user_bal == 0) {
                            Toast.makeText(LobbyActivity.this, "Empty balance!", Toast.LENGTH_SHORT).show();
                        } else {
                            userReference.child("fold").setValue(true);
                            Toast.makeText(LobbyActivity.this, "Folded!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        lobby_players.setOnItemClickListener((parent, view, position, id) -> {
            List<String> easter_eggs = List.of("IS A LOSER", "IS BLUFFING", "IS GUIDO ಥ_ಥ", "IS GAY ಠ_ಠ \uD83C\uDFF3️\u200D\uD83C\uDF08", "IS NEAPOLITAN (✖╭╮✖) \uD83C\uDF0B", "IS A CHAD \uD83D\uDDFF", "IS BLACK \uD83D\uDC80");
            if (Math.random() * 100 < 1) {
                Toast.makeText(this, easter_eggs.get(random.nextInt(7)), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        String user_name = intent.getStringExtra("User");
        String lobby_name = intent.getStringExtra("Lobby");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players").child(user_name);
        handleExitUser(userReference);
    }

    private void handleExitUser(DatabaseReference userReference) {
        userReference.child("active").setValue(false);
        userReference.child("bet").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int user_bet = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                if (user_bet == 0) {
                    userReference.removeValue();
                }
            } else {
                Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(LobbyActivity.this, "Exited lobby!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LobbyActivity.this, MainActivity.class));
        finish();
    }

    private void withdrawAllPot(String user_name, String lobby_name) {
        DatabaseReference potReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Pot");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players").child(user_name).child("balance");
        potReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int pot_dim = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                if (pot_dim == 0) {
                    Toast.makeText(LobbyActivity.this, "Empty pot!", Toast.LENGTH_SHORT).show();
                } else {
                    userReference.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            potReference.setValue(0);
                            int user_bal = Integer.parseInt(String.valueOf(task1.getResult().getValue()));
                            user_bal += pot_dim;
                            userReference.setValue(user_bal);
                            resetAllBets(lobby_name);
                        } else {
                            Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetAllBets(String lobby_name) {
        DatabaseReference playersReference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby_name).child("Players");
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
                Toast.makeText(LobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}