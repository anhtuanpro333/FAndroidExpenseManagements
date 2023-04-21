package com.example.fandroidexpensemanagements;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fandroidexpensemanagements.Log.Login;
import com.example.fandroidexpensemanagements.Log.SignIn;
import com.example.fandroidexpensemanagements.Log.SignUp;
import com.example.fandroidexpensemanagements.Main.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user!=null){
                    String userID = user.getUid();
                    if (!userID.equals(null)) {
                        changeActivity(new Navigation());

                    } else {
                        changeActivity(new Login());
                    }
                }else{
                    changeActivity(new Login());
                }
            }
        }, 2000);
    }

    public void changeActivity(AppCompatActivity activity) {
        Intent intent = new Intent(MainActivity.this, activity.getClass());
        startActivity(intent);
    }
}