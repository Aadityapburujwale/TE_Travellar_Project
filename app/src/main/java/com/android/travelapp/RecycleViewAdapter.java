package com.android.travelapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private ArrayList<TourModel> tourList = new ArrayList<>();
    private Context context;

    public RecycleViewAdapter(ArrayList<TourModel> tourList, Context context){
        this.tourList = tourList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desain_dashboard_adapter, parent, false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).asBitmap().load(tourList.get(position).getTourImg()).into(holder.imgTour);
        holder.nameTour.setText(tourList.get(position).getTourName());
        holder.priceTour.setText(tourList.get(position).getTourPrice());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TourDetail.class);
                intent.putExtra("imgTour", tourList.get(position).getTourImg());
                intent.putExtra("nameTour", tourList.get(position).getTourName());
                intent.putExtra("descTour", tourList.get(position).getTourDesc());
                intent.putExtra("locTour", tourList.get(position).getTourLoc());
                intent.putExtra("priceTour", tourList.get(position).getTourPrice());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgTour;
        TextView nameTour, priceTour, locTour;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgTour = itemView.findViewById(R.id.img_tour);
            nameTour = itemView.findViewById(R.id.name_tour);
            locTour = itemView.findViewById(R.id.btn_img_loc);
            priceTour = itemView.findViewById(R.id.price_tour);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
