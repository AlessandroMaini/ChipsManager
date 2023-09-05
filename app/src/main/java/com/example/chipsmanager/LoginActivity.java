package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name_login);
        password = findViewById(R.id.password_login);
        Button login = findViewById(R.id.login_login);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(v -> {
            String txt_name = name.getText().toString();
            String txt_password = password.getText().toString();
            loginUser(txt_name, txt_password);
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, StartActivity.class));
        finish();
    }

    private void loginUser(String name, String password) {
        auth.signInWithEmailAndPassword(name, password).addOnCompleteListener(task -> {
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}