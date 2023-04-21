package com.example.fandroidexpensemanagements.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Main.Navigation;
import com.example.fandroidexpensemanagements.Model.HelperClass;
import com.example.fandroidexpensemanagements.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    EditText email, password;
    Button logIn;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = findViewById(R.id.signInEmail);
        password = findViewById(R.id.signInPassword);
        logIn = findViewById(R.id.signInLogIn);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Login");
        dialog.setMessage("We are login you in our app");


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String emails = email.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                if (checkInput(email,password)){
                    if (checkEmail(emails)){
                        mAuth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                dialog.cancel();
                                changeActivity(new Navigation());
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                dialog.cancel();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.cancel();
                                Toast.makeText(SignIn.this, "Sorry we think you have some false in the input", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }

    public boolean checkInput(EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().length() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean checkEmail(String email) {
        email = email.trim();
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public void changeActivity(AppCompatActivity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

    public void getUser(String table) {
        reference = FirebaseDatabase.getInstance().getReference().child(table);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);
                    Toast.makeText(SignIn.this, email+" "+password, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignIn.this, "Không thấy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void update(String table){
        reference = FirebaseDatabase.getInstance().getReference().child(table);
        HelperClass helperClass = new HelperClass("ditmemay@gmail.com","ặcă");
        Map<String,Object> aClass = new HashMap<>();
        aClass.put("-NSWb8KRtIzx8V9RfAgw",helperClass);
        reference.updateChildren(aClass);
    }

    private void ditconmemay(String gmail, String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        if (user == null){
            return;
        } else {
            DatabaseReference newUserRef = userRef.child(user.getUid());
            newUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
//                        String currentGmail = snapshot.child("email").getValue(String.class);
//                        String currentPassword = snapshot.child("password").getValue(String.class);
                        snapshot.getRef().child("email").setValue(gmail);
                        snapshot.getRef().child("password").setValue(password);
                        Toast.makeText(SignIn.this, "1", Toast.LENGTH_SHORT).show();
                    } else {
                        HelperClass newUsers = new HelperClass(gmail,password);
                        newUserRef.setValue(newUsers);
                        Toast.makeText(SignIn.this, "2", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}