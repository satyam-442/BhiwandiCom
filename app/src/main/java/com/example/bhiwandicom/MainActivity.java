package com.example.bhiwandicom;

import android.content.Intent;
import android.os.Bundle;

import com.example.bhiwandicom.Adapter.MainAdapter;
import com.example.bhiwandicom.Adapter.SliderAdapter;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //DATABASE REFERENCE's
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");

        //STORE RECYCLER VIEW
        recyclerViewStore = (RecyclerView) findViewById(R.id.recyclerViewStore);
        recyclerViewStore.setHasFixedSize(true);
        LinearLayoutManager layoutManagerVer = new LinearLayoutManager(this);
        layoutManagerVer.setReverseLayout(true);
        layoutManagerVer.setStackFromEnd(true);
        recyclerViewStore.setLayoutManager(layoutManagerVer);
        //recyclerViewStore.setLayoutManager(layoutManagerVer);

        //HORIZONTAL RECYCLER VIEW
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
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
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView userImage = headerView.findViewById(R.id.user_profile_image);

        //userNameTextView.setText(Prevalent.currentOnlineUser.getNamee());

        marqueeText = (TextView) findViewById(R.id.moving_text_notification);
        marqueeText.setSelected(true);

        //////////BANNER SLIDER ACTIVITY FOR ADVERTISEMENT
        bannerSlider = (ViewPager) findViewById(R.id.moving_text_advertisement);
        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.computer_lab));
        sliderModelList.add(new SliderModel(R.drawable.cart));

        sliderModelList.add(new SliderModel(R.drawable.bhiwandicomlogo));
        sliderModelList.add(new SliderModel(R.drawable.bhiwandi));
        sliderModelList.add(new SliderModel(R.drawable.orders));
        sliderModelList.add(new SliderModel(R.drawable.computer_lab));
        sliderModelList.add(new SliderModel(R.drawable.cart));

        sliderModelList.add(new SliderModel(R.drawable.bhiwandicomlogo));
        sliderModelList.add(new SliderModel(R.drawable.bhiwandi));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSlider.setAdapter(sliderAdapter);
        bannerSlider.setClipToPadding(false);
        bannerSlider.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };
        bannerSlider.addOnPageChangeListener(onPageChangeListener);

        startBannerAnimation();
        bannerSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerAnimation();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    startBannerAnimation();
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            SendUserToLoginActivity();
        }
        startListening();
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /////////PAGE LOOPER METHOD
    private void pageLooper() {
        if (currentPage == sliderModelList.size() - 2) {
            currentPage = 2;
            bannerSlider.setCurrentItem(currentPage, false);
        }
        if (currentPage == 1) {
            currentPage = sliderModelList.size() - 3;
            bannerSlider.setCurrentItem(currentPage, false);
        }
    }

    private void startBannerAnimation() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size()) {
                    currentPage = 1;
                }
                bannerSlider.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
                //40:43
            }
        }, DELAYTIME, PERIODTIME);
    }

    private void stopBannerAnimation() {
        timer.cancel();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if (id==R.id.action_settings)
//        {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_category) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent logIn = new Intent(MainActivity.this, LoginPhoneActivity.class);
            logIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logIn);
            finish();
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //    @Override
    //    public boolean onSupportNavigateUp()
    //    {
    //        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    //        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
    //                || super.onSupportNavigateUp();
    //    }

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
