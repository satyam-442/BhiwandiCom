package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterTwoActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ImageView backButton;
    Button next, login;
    TextView titleText;
    String gender, age, fullname, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        mAuth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");

    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void CallNextRegisterLastScreen(View view) {
        Intent intent = new Intent(this, RegisterThreeActivity.class);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(backButton,"transition_back_btn");
        pairs[1] = new Pair<View,String>(titleText,"transition_title_text");
        pairs[2] = new Pair<View,String>(next,"transition_next_btn");
        pairs[3] = new Pair<View,String>(login,"transition_login_btn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterTwoActivity.this,pairs);
        startActivity(intent,options.toBundle());
    }
}
