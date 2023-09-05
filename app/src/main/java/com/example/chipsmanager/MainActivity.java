package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = findViewById(R.id.logout_main);
        ListView lobbies = findViewById(R.id.listView_lobbies);
        Button create_lobby = findViewById(R.id.create_lobby);

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, StartActivity.class));
        });

        List<String> lobby_list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, lobby_list);
        lobbies.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Lobbies");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lobby_list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String lobby_name = dataSnapshot.getKey();
                    lobby_list.add(lobby_name);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        create_lobby.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateLobbyActivity.class)));

        lobbies.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, AccessLobbyActivity.class);
            intent.putExtra("Lobby", lobby_list.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this, StartActivity.class));
    }
}