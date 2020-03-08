package com.example.bhiwandicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterPhoneActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    DatabaseReference userRef;
    ProgressDialog loadingBar;
    Button registerPhoneAcc, registerAdminButton,loginNotAdminButton;
    EditText Name, Phone, Password;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);

        mAuth = FirebaseAuth.getInstance();
        //currentUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Name = (EditText) findViewById(R.id.registerPhoneNameET);
        Phone = (EditText) findViewById(R.id.registerPhoneET);
        Password = (EditText) findViewById(R.id.registerPhonePwdET);


        registerAdminButton = (Button) findViewById(R.id.registerAdminButton);
        //loginNotAdminButton = (Button) findViewById(R.id.loginNotAdminButton);
        registerAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisAdmin = new Intent(RegisterPhoneActivity.this,RegisterAdminActivity.class);
                startActivity(regisAdmin);
            }
        });

        loadingBar = new ProgressDialog(this);

        registerPhoneAcc = (Button) findViewById(R.id.registerPhoneButton);
        registerPhoneAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CreateUsersAccount();
            }
        });

    }

    private void CreateUsersAccount()
    {
        final String name = Name.getText().toString();
        final String phone = Phone.getText().toString();
        final String password = Password.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Fields are empty...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,password);

        }
    }

    private void ValidatePhoneNumber(final String name,final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userMap = new HashMap<String, Object>();
                    userMap.put("Name",name);
                    userMap.put("Phone",phone);
                    userMap.put("Password",password);
                    RootRef.child("Users").child(phone).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(RegisterPhoneActivity.this, "Account Created...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String msg = task.getException().getMessage();
                                Toast.makeText(RegisterPhoneActivity.this, "Error Occurred :" + msg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterPhoneActivity.this, "The number already exist...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent welIntent = new Intent(RegisterPhoneActivity.this,LoginPhoneActivity.class);
                    startActivity(welIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(this,LoginPhoneActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
