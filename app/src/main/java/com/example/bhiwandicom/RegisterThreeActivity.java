package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterThreeActivity extends AppCompatActivity {

    Button signup_next_button;
    TextInputLayout acceptNoFromUser;
    String gender, age, fullname, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_three);

        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");

        acceptNoFromUser = findViewById(R.id.acceptNoFromUser);
        acceptNoFromUser.getEditText().setText(phone);

        signup_next_button = findViewById(R.id.signup_next_button);
        signup_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterThreeActivity.this, VerifyPhoneActivity.class);
                startActivity(intent);
            }
        });
    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, RegisterThreeActivity.class);
        startActivity(intent);
        finish();
    }

    public void CallNextRegisterScreen(View view) {

    }
}
