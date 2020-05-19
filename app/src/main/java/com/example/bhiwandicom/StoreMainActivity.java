package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StoreMainActivity extends AppCompatActivity
{

    String StoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);

        StoreName = getIntent().getStringExtra("storeName");

    }
}
