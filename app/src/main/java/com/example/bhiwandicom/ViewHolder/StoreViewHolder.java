package com.example.bhiwandicom.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhiwandicom.Interfaces.ItemClickListener;
import com.example.bhiwandicom.R;

public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtStoreName, txtStoreTime;
    public ImageView storeImage;
    public ItemClickListener listener;

    public StoreViewHolder(@NonNull View itemView) {
        super(itemView);
        storeImage = (ImageView) itemView.findViewById(R.id.store_image_layout);
        txtStoreName = (TextView) itemView.findViewById(R.id.store_name_layout);
        txtStoreTime = (TextView) itemView.findViewById(R.id.store_time_layout);
    }

    public void setItemClickedListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
