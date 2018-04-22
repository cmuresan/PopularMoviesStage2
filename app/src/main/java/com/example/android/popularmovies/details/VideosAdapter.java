package com.example.android.popularmovies.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {
        holder.bindData(videos.get(position));
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView share;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            share = itemView.findViewById(R.id.video_share);
        }

        void bindData(final Video video) {
            title.setText(video.getName());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, getVideoUrl(video));
                    context.startActivity(webIntent);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getVideoUrl(video).toString());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
        }

        private Uri getVideoUrl(Video video) {
            return Uri.parse("http://www.youtube.com/watch?v=" + video.getKey());
        }
    }
}
