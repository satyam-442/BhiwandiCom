package com.example.bhiwandicom.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bhiwandicom.Model.Products;
import com.example.bhiwandicom.R;
import com.example.bhiwandicom.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ProductListFragment extends Fragment {

    FirebaseAuth mAuth;
    String storeName, category, currentUserId;
    RecyclerView productsRec;
    RecyclerView.LayoutManager layoutManager;

    public ProductListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        storeName = getArguments().getString("storeName");
        category = getArguments().getString("category");

        productsRec = view.findViewById(R.id.productList);
        productsRec.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        productsRec.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        startListening();
    }

    private void startListening() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products").child(storeName).child(category).limitToLast(50);
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(query, Products.class).build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int i, @NonNull final Products product) {
                productViewHolder.txtProductName.setText(product.getPnamee());
                productViewHolder.txtProductDescription.setText(product.getDescriptionn());
                productViewHolder.txtProductCost.setText(product.getPricee());

                Picasso.with(getActivity()).load(product.getImagee()).into(productViewHolder.productImage);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
                        intent.putExtra("pid",product.getPidd());
                        startActivity(intent);*/
                        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid", product.getPidd());
                        bundle.putString("uid", currentUserId);
                        bundle.putString("imageUrl", product.getImagee());
                        productDetailsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, productDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        productsRec.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

}
