package com.example.bhiwandicom.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhiwandicom.Model.MainModel;
import com.example.bhiwandicom.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>
{
    ArrayList<MainModel> mainModels;
    Context context;

    public MainAdapter(Context context,ArrayList<MainModel> mainModels )
    {
        this.context = context;
        this.mainModels= mainModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.imageView.setImageResource(mainModels.get(position).getHoriCategoryRecyclerViewImagee());
        holder.textView.setText(mainModels.get(position).getHoriCategoryRecyclerViewTextt());
    }

    @Override
    public int getItemCount()
    {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
