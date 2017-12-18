package com.bhavadeep.googleclustering.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bhavadeep.googleclustering.R;
import com.bhavadeep.googleclustering.models.Result;

import java.util.ArrayList;
import java.util.List;


public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.CustomHViewHolder> {

    private final Context context;
    private ArrayList<Result> resultList;

    public HorizontalRecyclerAdapter(Context context, List<Result> resultList) {
        this.context = context;
        this.resultList = new ArrayList<>();
        this.resultList.addAll(resultList);
    }

    @Override
    public CustomHViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_horizontal_recycler_view, null, false);
        return new CustomHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomHViewHolder holder, int position) {
        holder.titleTextView.setText(resultList.get(position).getName());
        String ratingsString = resultList.get(position).getRating();
        if (ratingsString != null) {
            Float ratingsFloat = Float.parseFloat(ratingsString);
            holder.ratingBar.setRating(ratingsFloat);
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    class CustomHViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        RatingBar ratingBar;

        CustomHViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_circle);
            ratingBar = itemView.findViewById(R.id.ratingBar_circle);
        }
    }
}
