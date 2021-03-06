package com.bhavadeep.googleclustering.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhavadeep.googleclustering.models.Result;
import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.ui.activity.MainActivity;
import com.bhavadeep.googleclustering.ui.fragments.ListFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;



public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {


    private Context context;
    private List<Result> resultList;
    OnListItemClickedLister listener;

    public RecyclerAdapter(Context context, List<Result> results, OnListItemClickedLister listener) {
        this.context = context;
        resultList = new ArrayList<>();
        resultList = results;
        this.listener = listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_recycler_view, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Result result = resultList.get(position);
        holder.titleTextView.setText(result.getName());
        holder.lattitudeTextView.setText(String.valueOf(result.getGeometry().getLocation().getLat()));
        holder.longitudeTextView.setText(String.valueOf(result.getGeometry().getLocation().getLng()));
        Glide.with(context).load(result.getIcon())
                    .asBitmap()
                    .placeholder(R.drawable.ic_action_name)
                    .fitCenter()
                    .into(holder.iconImageView);
        holder.addressTextView.setText(result.getAddress());
        String ratings = result.getRating();
        if(ratings == null)
            ratings = "Not Available";
        holder.ratingsTextView.setText(ratings);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView iconImageView;
        TextView lattitudeTextView;
        TextView longitudeTextView;
        TextView addressTextView;
        TextView ratingsTextView;

        CustomViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.title_info);
            iconImageView = view.findViewById(R.id.imageView);
            lattitudeTextView = view.findViewById(R.id.latitude_value);
            longitudeTextView = view.findViewById(R.id.longitude_value);
            addressTextView = view.findViewById(R.id.address_info);
            ratingsTextView = view.findViewById(R.id.rating_value);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListItemClicked(getAdapterPosition());
                }
            });
        }
    }

    public interface OnListItemClickedLister {
        void onListItemClicked(int position);
    }
}
