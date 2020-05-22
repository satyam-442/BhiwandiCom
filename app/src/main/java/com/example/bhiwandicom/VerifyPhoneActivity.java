package com.example.bhiwandicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    Button verify_btn;
    PinView phoneNoEnteredByUser;
    ProgressBar progressBar;
    String phoneNo, verificationCodeBySystem, currentUserId;
    String gender, age, fullname, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        fullname = getIntent().getStringExtra("fullname");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        gender = getIntent().getStringExtra("gender");
        age = getIntent().getStringExtra("age");

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByUser = findViewById(R.id.verification_send_by_the_user);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        phoneNo = getIntent().getStringExtra("phone");
        sendVerificationCodeToUser(phoneNo);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = phoneNoEnteredByUser.getText().toString();
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(VerifyPhoneActivity.this, "Wrong OTP...", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInUserWithCredential(credential);
    }

    private void signInUserWithCredential(PhoneAuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(VerifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    /*FirebaseUser user = task.getResult().getUser();*/
                    currentUserId = mAuth.getCurrentUser().getUid();
                    HashMap<String, Object> userMap = new HashMap<String, Object>();
                    userMap.put("name", fullname);
                    userMap.put("phone", phone);
                    userMap.put("email", email);
                    userMap.put("image", "default");
                    userMap.put("uid", currentUserId);
                    userMap.put("gender", gender);
                    userMap.put("age", age);
                    userMap.put("Password", password);
                    userRef.child(currentUserId).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                SendUserToMainActivity();
                                Toast.makeText(VerifyPhoneActivity.this, "Account Successfully Created...", Toast.LENGTH_SHORT).show();
                                //loadingBar.dismiss();
                                progressBar.setVisibility(View.VISIBLE);
                            } else {
                                String msg = task.getException().getMessage();
                                Toast.makeText(VerifyPhoneActivity.this, "Error Occurred :" + msg, Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(VerifyPhoneActivity.this, "Error occurred: " + message, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void SendUserToMainActivity() {
        Intent dashboard = new Intent(VerifyPhoneActivity.this, MainActivity.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboard);
        finish();
    }
}
