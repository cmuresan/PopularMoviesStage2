package com.example.android.popularmovies.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.networkmodule.model.Video;
import com.example.android.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Video> videos;

    VideosAdapter(Context context) {
        this.context = context;
    }

    public void setVideos(List<Video> videos) {
        this.videos = new ArrayList<>(videos);
    }

    @NonNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {
        holder.bindData(videos.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
        }

        void bindData(String title) {
            this.title.setText(title);
        }
    }
}
