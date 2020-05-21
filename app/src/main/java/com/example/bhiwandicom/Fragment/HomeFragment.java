package com.example.bhiwandicom.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bhiwandicom.Adapter.MainAdapter;
import com.example.bhiwandicom.Adapter.SliderAdapter;
import com.example.bhiwandicom.LoginActivity;
import com.example.bhiwandicom.MainActivity;
import com.example.bhiwandicom.Model.MainModel;
import com.example.bhiwandicom.Model.SliderModel;
import com.example.bhiwandicom.Model.Store;
import com.example.bhiwandicom.R;
import com.example.bhiwandicom.StoreMainActivity;
import com.example.bhiwandicom.ViewHolder.StoreViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class HomeFragment extends Fragment {

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
    Toolbar toolbar;

    DatabaseReference productRef, storeRef;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        toolbar = getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");

        //DATABASE REFERENCE's
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        storeRef = FirebaseDatabase.getInstance().getReference().child("Store");

        //STORE RECYCLER VIEW
        recyclerViewStore = (RecyclerView) view.findViewById(R.id.recyclerViewStore);
        recyclerViewStore.setHasFixedSize(true);
        LinearLayoutManager layoutManagerVer = new LinearLayoutManager(getActivity());
        layoutManagerVer.setReverseLayout(true);
        layoutManagerVer.setStackFromEnd(true);
        recyclerViewStore.setLayoutManager(layoutManagerVer);
        //recyclerViewStore.setLayoutManager(layoutManagerVer);

        //HORIZONTAL RECYCLER VIEW
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        Integer[] horiCategoryRecyclerViewImage = {R.drawable.htshirt, R.drawable.fshirt, R.drawable.trouser,
                R.drawable.jeans, R.drawable.wallet, R.drawable.belts, R.drawable.shoe};
        String[] horiCategoryRecyclerViewText = {"Half Shirt", "Full Shirt", "Trouser", "Jeans", "Wallet", "Belts", "Shoe"};

        mainModels = new ArrayList<>();
        for (int i = 0; i < horiCategoryRecyclerViewImage.length; i++) {
            MainModel model = new MainModel(horiCategoryRecyclerViewImage[i], horiCategoryRecyclerViewText[i]);
            mainModels.add(model);
        }

        LinearLayoutManager layoutManagerHor = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManagerHor);
        recyclerViewCategory.setItemAnimator(new DefaultItemAnimator());
        mainAdapter = new MainAdapter(getActivity(), mainModels);
        recyclerViewCategory.setAdapter(mainAdapter);

        /*FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Your cart product will be display here", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        marqueeText = (TextView) view.findViewById(R.id.moving_text_notification);
        marqueeText.setSelected(true);

        //////////BANNER SLIDER ACTIVITY FOR ADVERTISEMENT
        bannerSlider = (ViewPager) view.findViewById(R.id.moving_text_advertisement);
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

        return view;
    }

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
    public void onStart() {
        super.onStart();
        /*FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            SendUserToLoginActivity();
        }*/
        //startListening();
    }

    private void SendUserToLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
                Picasso.with(getActivity()).load(store.getImagee()).into(storeViewHolder.storeImage);

                storeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), StoreMainActivity.class);
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
