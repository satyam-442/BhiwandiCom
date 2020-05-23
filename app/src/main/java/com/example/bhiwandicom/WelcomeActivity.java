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
import android.widget.Toast;

import com.example.bhiwandicom.Admin.LoginAdminActivity;
import com.example.bhiwandicom.Model.User;
import com.example.bhiwandicom.Prevalent.Prevalent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    Button login, register, welcomeLoginButtonTwo;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();

        Paper.init(this);

        login = (Button) findViewById(R.id.welcomeLoginButton);
        register = (Button) findViewById(R.id.welcomeRegisterButton);
        welcomeLoginButtonTwo = findViewById(R.id.welcomeLoginButtonTwo);
        welcomeLoginButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPg = new Intent(WelcomeActivity.this, LoginAdminActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(login,"transition_login");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
                startActivity(loginPg,options.toBundle());
            }
        });

        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPg = new Intent(WelcomeActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(login,"transition_login");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
                startActivity(loginPg,options.toBundle());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regisPg = new Intent(WelcomeActivity.this, RegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(login,"transition_register");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(WelcomeActivity.this,pairs);
                startActivity(regisPg,options.toBundle());
            }
        });


        String UserPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.userPasswordKey);
        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccessToAccount(UserPhoneKey,UserPasswordKey);
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            SendUserToMainActivity();
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        loadingBar.setMessage("please wait");
        loadingBar.dismiss();
        loadingBar.show();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    User usersData = dataSnapshot.child("Users").child(phone).getValue(User.class);
                    //User usersData = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                    if (usersData.getPhonee().equals(phone))
                    {
                        if (usersData.getPasswordd().equals(password))
                        {
                            /*Toast.makeText(WelcomeActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            SendUserToMainActivity();*/

                            Intent loginIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finish();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(WelcomeActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(WelcomeActivity.this, "No record found", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(this,MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
