package com.example.bhiwandicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.example.bhiwandicom.Adapter.SliderAdapter;
import com.example.bhiwandicom.Model.SliderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DemoActivity extends AppCompatActivity {

    ViewPager bannerSlider;
    List<SliderModel> sliderModelList;
    Integer currentPage = 2;
    Timer timer;
    final long DELAYTIME = 3000;
    final long PERIODTIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        bannerSlider = (ViewPager) findViewById(R.id.moving_text_advertisement);
        sliderModelList = new ArrayList<SliderModel>();

        sliderModelList.add(new SliderModel(R.drawable.style));
        sliderModelList.add(new SliderModel(R.drawable.pay));

        sliderModelList.add(new SliderModel(R.drawable.del));
        sliderModelList.add(new SliderModel(R.drawable.img1));
        sliderModelList.add(new SliderModel(R.drawable.style));
        sliderModelList.add(new SliderModel(R.drawable.pay));

        sliderModelList.add(new SliderModel(R.drawable.del));
        sliderModelList.add(new SliderModel(R.drawable.img1));

        SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
        bannerSlider.setAdapter(sliderAdapter);
        bannerSlider.setClipToPadding(false);
        bannerSlider.setPageMargin(20);

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                if (state == ViewPager.SCROLL_STATE_IDLE)
                {
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
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    startBannerAnimation();
                }
                return false;
            }
        });
    }

    private void pageLooper()
    {
        if (currentPage == sliderModelList.size() - 2)
        {
            currentPage = 2;
            bannerSlider.setCurrentItem(currentPage,false);
        }
        if (currentPage == 1)
        {
            currentPage = sliderModelList.size() - 3;
            bannerSlider.setCurrentItem(currentPage,false);
        }
    }

    private void startBannerAnimation()
    {
        final Handler handler  = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage >= sliderModelList.size())
                {
                    currentPage = 1;
                }
                bannerSlider.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(update);
                //40:43
            }
        },DELAYTIME,PERIODTIME);
    }

    private void stopBannerAnimation()
    {
        timer.cancel();
    }
}
