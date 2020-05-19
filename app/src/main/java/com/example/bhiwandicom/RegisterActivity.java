package com.example.bhiwandicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ImageView backButton;
    Button next, login;
    TextView titleText;
    TextInputLayout userFullName, userEmail, userContact, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        backButton = findViewById(R.id.signup_back_button);
        next = findViewById(R.id.signup_next_button);
        login = findViewById(R.id.signup_login_button);
        titleText = findViewById(R.id.signup_title_text);

        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        userContact = findViewById(R.id.userContact);
        userPassword = findViewById(R.id.userPassword);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccWithEmailAndPwd();
            }
        });

    }

    private void CreateAccWithEmailAndPwd() {
        final String fullname = userFullName.getEditText().getText().toString();
        final String email = userEmail.getEditText().getText().toString();
        final String phone = userContact.getEditText().getText().toString();
        final String password = userPassword.getEditText().getText().toString();

        if (TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Enter phone...", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length()<10)
        {
            Toast.makeText(this, "Invalid phone no. ...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, RegisterTwoActivity.class);
                        intent.putExtra("fullname",fullname);
                        intent.putExtra("email",email);
                        intent.putExtra("phone",phone);
                        intent.putExtra("password",password);
                        Pair[] pairs = new Pair[4];
                        pairs[0] = new Pair<View,String>(backButton,"transition_back_btn");
                        pairs[1] = new Pair<View,String>(titleText,"transition_title_text");
                        pairs[2] = new Pair<View,String>(next,"transition_next_btn");
                        pairs[3] = new Pair<View,String>(login,"transition_login_btn");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                        startActivity(intent,options.toBundle());
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String msg = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error generated:" + msg, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /*public void CallNextRegisterScreen(View view) {
        Intent intent = new Intent(this, RegisterTwoActivity.class);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View,String>(backButton,"transition_back_btn");
        pairs[1] = new Pair<View,String>(titleText,"transition_title_text");
        pairs[2] = new Pair<View,String>(next,"transition_next_btn");
        pairs[3] = new Pair<View,String>(login,"transition_login_btn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
        startActivity(intent,options.toBundle());
    }*/
}
