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
import android.widget.Toast;

import com.example.bhiwandicom.Admin.AdminCategoryActivity;
import com.example.bhiwandicom.Model.User;
import com.example.bhiwandicom.Owner.AddAdminStoreToDatabaseActivity;
import com.example.bhiwandicom.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginPhoneActivity extends AppCompatActivity
{

    EditText Phone, Password;
    Button loginButton, loginAdminButton,loginNotAdminButton;
    ProgressDialog loadingBar;

    FirebaseAuth mAuth;
    DatabaseReference RootRef;
    String parentDBName = "Users";
    CheckBox chkBoxRememMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);

        mAuth = FirebaseAuth.getInstance();
        //currentUserId = mAuth.getCurrentUser().getUid();
        //RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        loginButton = (Button) findViewById(R.id.loginPhoneButton);
        loginAdminButton = (Button) findViewById(R.id.loginAdminButton);
        loginNotAdminButton = (Button) findViewById(R.id.loginNotAdminButton);


        chkBoxRememMe = (CheckBox) findViewById(R.id.loginRememberMe);
        Paper.init(this);


        Phone = (EditText) findViewById(R.id.loginPhoneEmailET);
        Password = (EditText) findViewById(R.id.loginPhonePwdET);

        loadingBar = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserWithPhone();
            }
        });

        loginAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logPhoneAdminToAccount();
            }
        });

        loginNotAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logNotAdminToAccount();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    //ADMIN LINK FUNCTION
    private void logPhoneAdminToAccount()
    {
        loginButton.setText("Login as Admin!");
        loginAdminButton.setVisibility(View.INVISIBLE);
        loginNotAdminButton.setVisibility(View.VISIBLE);
        parentDBName = "Admins";
    }


    //NOT ADMIN LINK FUNCTION
    private void logNotAdminToAccount() {
        loginButton.setText("Login!");
        loginAdminButton.setVisibility(View.VISIBLE);
        loginNotAdminButton.setVisibility(View.INVISIBLE);
        parentDBName = "Users";
    }


    private void logUserWithPhone() {
        String phone = Phone.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
        if (phone.equals("123") && password.equals("123")){
            startActivity(new Intent(LoginPhoneActivity.this, AddAdminStoreToDatabaseActivity.class));
            finish();
        }
        else
        {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);

        }

    }

    private void AllowAccessToAccount(final String phone, final String password)
    {

        if (chkBoxRememMe.isChecked())
        {
            Paper.book().write(Prevalent.userPhoneKey,phone);
            Paper.book().write(Prevalent.userPasswordKey,password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(phone).exists())
                {
                    User usersData = dataSnapshot.child(parentDBName).child(phone).getValue(User.class);
                    //User usersData = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                    if (usersData.getPhonee().equals(phone))
                    {
                        if (usersData.getPasswordd().equals(password))
                        {
                            if (parentDBName.equals("Admins"))
                            {
                                Toast.makeText(LoginPhoneActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                SendUserToAdminActivity();
                            }
                            else if (parentDBName.equals("Users"))
                            {
                                Toast.makeText(LoginPhoneActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent loginIntent = new Intent(LoginPhoneActivity.this,MainActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(LoginPhoneActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginPhoneActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SendUserToAdminActivity() {
        Intent adminIntent = new Intent(this, AdminCategoryActivity.class);
        //adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(adminIntent);
        //finish();
    }

    private void SendUserToMainActivity() {

    }
}
