package com.example.chipsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name_signup);
        password = findViewById(R.id.password_signup);
        Button signup = findViewById(R.id.signup_signup);
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            String txt_name = name.getText().toString();
            String txt_password = password.getText().toString();
            if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(SignupActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6) {
                Toast.makeText(SignupActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txt_name, txt_password);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, StartActivity.class));
        finish();
    }

    private void registerUser(String name, String password) {
        auth.createUserWithEmailAndPassword(name, password).addOnCompleteListener(SignupActivity.this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignupActivity.this, "Registering user successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}