package com.example.android.popularmovies.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.networkmodule.model.Review;
import com.example.android.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Review> reviews;

    ReviewsAdapter(Context context) {
        this.context = context;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        holder.bindData(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.review_content);
        }

        void bindData(String content) {
            this.content.setText(content);
        }
    }
}
