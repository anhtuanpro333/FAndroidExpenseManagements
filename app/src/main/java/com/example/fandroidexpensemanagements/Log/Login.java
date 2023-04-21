package com.example.fandroidexpensemanagements.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.MainActivity;
import com.example.fandroidexpensemanagements.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button signIn, signUp;
    TextView otherWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = findViewById(R.id.signIN);
        signUp = findViewById(R.id.signUp);

        signUp.setOnClickListener(view -> changeActivity(new SignUp()));
        signIn.setOnClickListener(view->changeActivity(new SignIn()));
    }

    public void changeActivity(AppCompatActivity activity){
        Intent intent = new Intent(Login.this, activity.getClass());
        startActivity(intent);
    }
}