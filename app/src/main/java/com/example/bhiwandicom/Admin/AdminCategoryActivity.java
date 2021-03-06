package com.example.bhiwandicom.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bhiwandicom.R;

public class AdminCategoryActivity extends AppCompatActivity
{

    ImageView HalfShirt, FullShirt, Trouser, Jeans, Wallets, Shoes, Belts;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        shopName = getIntent().getStringExtra("shopName");
        //Toast.makeText(this, shopName, Toast.LENGTH_SHORT).show();

        HalfShirt = (ImageView) findViewById(R.id.htshirt);
        FullShirt = (ImageView) findViewById(R.id.fshirt);
        Trouser = (ImageView) findViewById(R.id.trouser);
        Jeans = (ImageView) findViewById(R.id.jeans);
        Wallets = (ImageView) findViewById(R.id.wallet);
        Shoes = (ImageView) findViewById(R.id.shoe);
        Belts = (ImageView) findViewById(R.id.belts);

        HalfShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","HalfShirt");
                intent.putExtra("shopName",shopName);
                startActivity(intent);
            }
        });

        FullShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                fullintent.putExtra("category","FullShirt");
                fullintent.putExtra("shopName",shopName);
                startActivity(fullintent);
            }
        });

        Trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trouserintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                trouserintent.putExtra("category","Trouser");
                trouserintent.putExtra("shopName",shopName);
                startActivity(trouserintent);
            }
        });

        Jeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jeansintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                jeansintent.putExtra("category","Jeans");
                jeansintent.putExtra("shopName",shopName);
                startActivity(jeansintent);
            }
        });

        Wallets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletsintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                walletsintent.putExtra("category","Wallets");
                walletsintent.putExtra("shopName",shopName);
                startActivity(walletsintent);
            }
        });

        Belts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Beltsintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Beltsintent.putExtra("category","Belts");
                Beltsintent.putExtra("shopName",shopName);
                startActivity(Beltsintent);
            }
        });

        Shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Shoesintent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                Shoesintent.putExtra("category","Shoes");
                Shoesintent.putExtra("shopName",shopName);
                startActivity(Shoesintent);
            }
        });
    }
}
