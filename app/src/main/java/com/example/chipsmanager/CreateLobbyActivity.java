package com.example.chipsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateLobbyActivity extends AppCompatActivity {
    private Button add_lobby;
    private EditText player_name;
    private EditText lobby_name;
    private final int DEFAULT_BALANCE = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        add_lobby = findViewById(R.id.add_lobby);
        player_name = findViewById(R.id.player_create);
        lobby_name = findViewById(R.id.lobby_create);

        add_lobby.setOnClickListener(v -> {
            String txt_lobby = lobby_name.getText().toString();
            String txt_player = player_name.getText().toString();
            if (TextUtils.isEmpty(txt_lobby) || TextUtils.isEmpty(txt_player)) {
                Toast.makeText(CreateLobbyActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
            } else if (txt_lobby.length() > 15 || txt_player.length() > 15) {
                Toast.makeText(this, "Name too long (<15 chars)!", Toast.LENGTH_SHORT).show();
            } else {
                createLobbyFromCredentials(txt_lobby, txt_player);
            }
        });
    }

    private void createLobbyFromCredentials(String lobby, String player) {
        try {
            FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby).child("Pot").setValue(0);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby).child("Players").push();
            reference.child("name").setValue(player);
            reference.child("balance").setValue(DEFAULT_BALANCE);
            reference.child("bet").setValue(0);

            Toast.makeText(this, "Lobby created successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateLobbyActivity.this, LobbyActivity.class);
            intent.putExtra("Lobby", lobby);
            intent.putExtra("User", reference.getKey());
            startActivity(intent);
        }catch (RuntimeException e) {
            Toast.makeText(this, "Lobby creation failed!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreateLobbyActivity.this, MainActivity.class));
        } finally {
            finish();
        }
    }
}