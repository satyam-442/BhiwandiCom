package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_three);
    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, RegisterThreeActivity.class);
        startActivity(intent);
        finish();
    }

    public void CallNextRegisterScreen(View view) {
        Intent intent = new Intent(this, VerifyPhoneActivity.class);
        startActivity(intent);
        finish();
    }
}
