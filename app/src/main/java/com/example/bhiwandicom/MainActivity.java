package com.example.bhiwandicom;

import android.content.Intent;
import android.os.Bundle;

import com.example.bhiwandicom.Adapter.MainAdapter;
import com.example.bhiwandicom.Adapter.SliderAdapter;
import com.example.bhiwandicom.Fragment.AboutUsFragment;
import com.example.bhiwandicom.Fragment.AllStoreFragment;
import com.example.bhiwandicom.Fragment.ContactUsFragment;
import com.example.bhiwandicom.Fragment.FeedbackFragment;
import com.example.bhiwandicom.Fragment.HomeFragment;
import com.example.bhiwandicom.Fragment.ProfileFragment;
import com.example.bhiwandicom.Model.MainModel;
import com.example.bhiwandicom.Model.Products;
import com.example.bhiwandicom.Model.SliderModel;
import com.example.bhiwandicom.Model.Store;
import com.example.bhiwandicom.Prevalent.Prevalent;
import com.example.bhiwandicom.ViewHolder.StoreViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity{

    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseAuth mAuth;

    RecyclerView recyclerViewCategory, recyclerViewStore;
    ArrayList<MainModel> mainModels;
    MainAdapter mainAdapter;
    TextView marqueeText;


    ViewPager bannerSlider;
    List<SliderModel> sliderModelList;
    Integer currentPage = 2;
    Timer timer;
    final long DELAYTIME = 3000;
    final long PERIODTIME = 3000;

    DatabaseReference productRef, storeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //DATABASE REFERENCE's
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");

        //STORE RECYCLER VIEW
        /*recyclerViewStore = (RecyclerView) findViewById(R.id.recyclerViewStore);
        recyclerViewStore.setHasFixedSize(true);
        LinearLayoutManager layoutManagerVer = new LinearLayoutManager(this);
        layoutManagerVer.setReverseLayout(true);
        layoutManagerVer.setStackFromEnd(true);
        recyclerViewStore.setLayoutManager(layoutManagerVer);*/
        //recyclerViewStore.setLayoutManager(layoutManagerVer);

        //HORIZONTAL RECYCLER VIEW
        /*recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        Integer[] horiCategoryRecyclerViewImage = {R.drawable.htshirt, R.drawable.fshirt, R.drawable.trouser,
                R.drawable.jeans, R.drawable.wallet, R.drawable.belts, R.drawable.shoe};
        String[] horiCategoryRecyclerViewText = {"Half Shirt", "Full Shirt", "Trouser", "Jeans", "Wallet", "Belts", "Shoe"};

        mainModels = new ArrayList<>();
        for (int i = 0; i < horiCategoryRecyclerViewImage.length; i++) {
            MainModel model = new MainModel(horiCategoryRecyclerViewImage[i], horiCategoryRecyclerViewText[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManagerHor = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManagerHor);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(MainActivity.this, mainModels);
        recyclerViewCategory.setAdapter(mainAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Your cart product will be display here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView userImage = headerView.findViewById(R.id.user_profile_image);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return true;
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            SendUserToLoginActivity();
        }
        //startListening();
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_store:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new AllStoreFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new AboutUsFragment()).commit();
                break;
            case R.id.nav_contactus:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ContactUsFragment()).commit();
                break;
            case R.id.nav_feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new FeedbackFragment()).commit();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void startListening() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Store").limitToLast(50);
        FirebaseRecyclerOptions<Store> options = new FirebaseRecyclerOptions.Builder<Store>().setQuery(query, Store.class).build();
        FirebaseRecyclerAdapter<Store, StoreViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Store, StoreViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StoreViewHolder storeViewHolder, final int i, @NonNull final Store store) {
                storeViewHolder.txtStoreName.setText("Shop Name: " + store.getShopNamee());
                String time = store.getFromTimee() + " to " + store.getToTimee();
                storeViewHolder.txtStoreTime.setText("Opening Time " + time);
                //storeViewHolder.txtProductPrice.setText(store.getPricee());
                Picasso.with(MainActivity.this).load(store.getImagee()).into(storeViewHolder.storeImage);

                storeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, StoreMainActivity.class);
                        intent.putExtra("storeName", store.getShopNamee());
                        startActivity(intent);
                        /*ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("pid",product.getPidd());
                        bundle.putString("uid",userId);
                        bundle.putString("imageUrl",product.getImagee());
                        productDetailsFragment.setArguments(bundle);
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container,productDetailsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();*/
                    }
                });
            }

            @NonNull
            @Override
            public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_layout, parent, false);
                StoreViewHolder holder = new StoreViewHolder(view);
                return holder;
            }
        };
        recyclerViewStore.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
}
