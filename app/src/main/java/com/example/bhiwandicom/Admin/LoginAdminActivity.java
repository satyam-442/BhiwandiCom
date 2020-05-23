package com.example.bhiwandicom.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bhiwandicom.LoginPhoneActivity;
import com.example.bhiwandicom.MainActivity;
import com.example.bhiwandicom.Model.User;
import com.example.bhiwandicom.Owner.AddAdminStoreToDatabaseActivity;
import com.example.bhiwandicom.Prevalent.Prevalent;
import com.example.bhiwandicom.R;
import com.example.bhiwandicom.WelcomeActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginAdminActivity extends AppCompatActivity {

    DatabaseReference adminRef;
    Button signup_next_buttonLogin, signup_register_buttonLogin;
    TextInputLayout userPhoneLogin, userPasswordLogin;
    ProgressDialog loadingBar;
    Button loginNotAdminButton, loginAdminButton;
    String parentDBName = "Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        loadingBar = new ProgressDialog(this);

        adminRef = FirebaseDatabase.getInstance().getReference().child("Admins");
        userPhoneLogin = findViewById(R.id.userPhoneLogin);
        userPasswordLogin = findViewById(R.id.userPasswordLogin);
        signup_next_buttonLogin = findViewById(R.id.signup_next_buttonLogin);
        signup_next_buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserWithPhone();
            }
        });

    }

    public void backToWelcome(View view) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void logUserWithPhone() {
        String phone = userPhoneLogin.getEditText().getText().toString();
        String password = userPasswordLogin.getEditText().getText().toString();

        if (TextUtils.isEmpty(phone) && TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setMessage("please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone, password);

        }

    }

    private void AllowAccessToAccount(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDBName).child(phone).exists()) {
                    User usersData = dataSnapshot.child(parentDBName).child(phone).getValue(User.class);
                    //User usersData = dataSnapshot.child(Phone.getText().toString()).getValue(User.class);
                    if (usersData.getPhonee().equals(phone)) {
                        if (usersData.getPasswordd().equals(password)) {
                            if (parentDBName.equals("Admins")) {
                                Toast.makeText(LoginAdminActivity.this, "Logged is successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                //SendUserToAdminActivity();
                                Intent adminIntent = new Intent(LoginAdminActivity.this, AdminCategoryActivity.class);
                                adminIntent.putExtra("shopName",usersData.getShopNamee());
                                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(adminIntent);
                                finish();
                            }
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginAdminActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginAdminActivity.this, "No record found", Toast.LENGTH_SHORT).show();
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
        adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(adminIntent);
        finish();
    }

}
