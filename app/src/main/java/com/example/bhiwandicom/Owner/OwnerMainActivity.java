package com.example.bhiwandicom.Owner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bhiwandicom.R;

public class OwnerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);
    }

    public void NavigateToAddStorePage(View view) {
        Intent intent = new Intent(this,AddAdminStoreToDatabaseActivity.class);
        startActivity(intent);
    }
}
