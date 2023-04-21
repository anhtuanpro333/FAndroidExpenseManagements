package com.example.fandroidexpensemanagements.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Model.HelperClass;
import com.example.fandroidexpensemanagements.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText emails,passwords;
    Button createAccount;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        emails = findViewById(R.id.signUpEmail);
        passwords = findViewById(R.id.signUpPassword);
        createAccount = findViewById(R.id.createAccount);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait");
        dialog.setMessage("We are sending you your email's verification code");

        createAccount.setOnClickListener(view -> {
            String email = emails.getText().toString();
            String password = passwords.getText().toString();
            if(checkEmail(email) && password.length()>0){
                dialog.show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                            if(task.isSuccessful()){
                                Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }else{
                                dialog.cancel();
                                Toast.makeText(this, "Sorry but we can't send you your email verification code", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Toast.makeText(SignUp.this, "Try another email", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
            }
        });

    }

    public boolean checkEmail(String email){
        email = email.trim();
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}