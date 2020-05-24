package com.example.bhiwandicom.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bhiwandicom.Model.Products;
import com.example.bhiwandicom.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetailsFragment extends Fragment {

    ImageView productImage;
    ElegantNumberButton numberBtn;
    TextView productName, productPrice, productDescription;
    CollapsingToolbarLayout collasping;
    FloatingActionButton btnCart;
    String productId, uid, imageUrl, storeName, category;

    DatabaseReference productRef;
    Toolbar toolbar;

    public ProductDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productId = getArguments().getString("pid");
        uid = getArguments().getString("uid");
        imageUrl = getArguments().getString("imageUrl");
        storeName = getArguments().getString("storeName");
        category = getArguments().getString("category");

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Product Details");

        productImage = view.findViewById(R.id.productImage);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productDescription = view.findViewById(R.id.productDescription);
        numberBtn = view.findViewById(R.id.numberBtn);
        btnCart = view.findViewById(R.id.btnCart);
        collasping = view.findViewById(R.id.collasping);

        getProductDetails(productId);

        return view;
    }

    private void getProductDetails(String productId) {
        productRef.child(storeName).child(category).child(productId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products product = dataSnapshot.getValue(Products.class);
                    productName.setText(product.getPnamee());
                    productDescription.setText(product.getDescriptionn());
                    productPrice.setText(product.getPricee());
                    Picasso.with(getActivity()).load(product.getImagee()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }
}
