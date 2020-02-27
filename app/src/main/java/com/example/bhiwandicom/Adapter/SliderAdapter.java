package com.example.bhiwandicom.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.bhiwandicom.Model.SliderModel;
import com.example.bhiwandicom.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter
{
    private List<SliderModel> sliderModelList ;

    public SliderAdapter() {
    }

    public SliderAdapter(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false);
        ImageView banner = view.findViewById(R.id.banner_slide);
        banner.setImageResource(sliderModelList.get(position).getBannerr());
        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount()
    {
        return sliderModelList.size();
    }
}
