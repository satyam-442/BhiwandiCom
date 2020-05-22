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
import android.widget.Toast;

import com.example.bhiwandicom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class StoreMainProfileFragment extends Fragment {

    DatabaseReference shopRef;
    String storeName;
    TextView shopName, shopTime, shopOwnerName, shopOwnerPhone, shopAddress;
    ImageView shopLogo;
    Toolbar toolbar;

    public StoreMainProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_main_profile, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Store Main Page");

        shopRef = FirebaseDatabase.getInstance().getReference().child("Store");

        storeName = getArguments().getString("storeName");
        //Toast.makeText(getActivity(), storeName, Toast.LENGTH_LONG).show();

        shopName = view.findViewById(R.id.shopName);
        shopTime = view.findViewById(R.id.shopTime);
        shopOwnerName = view.findViewById(R.id.shopOwnerName);
        shopOwnerPhone = view.findViewById(R.id.shopOwnerPhone);
        shopAddress = view.findViewById(R.id.shopAddress);
        shopLogo = view.findViewById(R.id.shopLogo);

        shopRef.child(storeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("ShopName").getValue().toString();
                    String address = dataSnapshot.child("ShopAddress").getValue().toString();
                    String phone = dataSnapshot.child("OwnerPhone").getValue().toString();
                    String ownerName = dataSnapshot.child("OwnerName").getValue().toString();
                    String timeFrom = dataSnapshot.child("fromTime").getValue().toString();
                    String timeTo = dataSnapshot.child("toTime").getValue().toString();
                    String time = timeFrom + " to " + timeTo;
                    shopName.setText("Shop: " + name);
                    shopTime.setText("Time: " + time);
                    shopOwnerName.setText("Phone: " + phone);
                    shopOwnerPhone.setText("Owner N: " + ownerName);
                    shopAddress.setText("Address: " + address);
                    final String image = dataSnapshot.child("image").getValue().toString();
                    if (!image.equals("default")) {
                        Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(shopLogo);
                        Picasso.with(getActivity()).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(shopLogo, new Callback() {
                            @Override
                            public void onSuccess() {
                            }
                            @Override
                            public void onError() {
                                Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(shopLogo);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return view;
    }
}
