package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccessLobbyActivity extends AppCompatActivity {
    private EditText player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_lobby);

        Button access = findViewById(R.id.access_lobby);
        player = findViewById(R.id.player_access);

        access.setOnClickListener(v -> {
            String txt_player = player.getText().toString();
            if (TextUtils.isEmpty(txt_player)) {
                Toast.makeText(AccessLobbyActivity.this, "No name entered!", Toast.LENGTH_SHORT).show();
            } else if (txt_player.length() > 15) {
                Toast.makeText(this, "Name too long (max 15 chars)!", Toast.LENGTH_SHORT).show();
            } else {
                accessLobbyWithName(txt_player);
            }
        });
    }

    private void accessLobbyWithName(String player) {
        Intent intent = getIntent();
        String lobby = intent.getStringExtra("Lobby");
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby).child("Players").push();
            reference.child("name").setValue(player);
            FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby).child("Starting").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    int starting = Integer.parseInt(String.valueOf(task.getResult().getValue()));
                    reference.child("balance").setValue(starting);
                } else {
                    Toast.makeText(AccessLobbyActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
                }
            });
            reference.child("bet").setValue(0);
            reference.child("fold").setValue(false);
            reference.child("active").setValue(true);

            Toast.makeText(this, "Lobby accessed successfully!", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(AccessLobbyActivity.this, LobbyActivity.class);
            intent1.putExtra("Lobby", lobby);
            intent1.putExtra("User", reference.getKey());
            startActivity(intent1);
        } catch (RuntimeException e) {
            Toast.makeText(this, "Lobby access failed!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AccessLobbyActivity.this, MainActivity.class));
        } finally {
            finish();
        }
    }

}