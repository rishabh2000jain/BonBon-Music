package com.example.quitemusic.ui.main_screen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.quitemusic.R;
import com.example.quitemusic.databinding.SearchViewMusicListItemBinding;
import com.example.quitemusic.models.SearchResultItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private List<SearchResultItem> trackList;
    LayoutInflater inflate;
    Gson gson;
    private OnSongClicked onSongClicked;

    public SongListAdapter(Context context, OnSongClicked onSongClicked) {
        inflate = LayoutInflater.from(context);
        trackList = new ArrayList<>();
        gson = new Gson();
        this.onSongClicked = onSongClicked;
    }

    public void setTrackList(List<SearchResultItem> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchViewMusicListItemBinding binding = SearchViewMusicListItemBinding.inflate(inflate, parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResultItem resultItem = trackList.get(position);
        holder.binding.type.setSelected(true);
        holder.binding.songNameTxt.setSelected(true);
        holder.binding.songNameTxt.setText("" + resultItem.getTitle());
        holder.binding.type.setText("" + resultItem.getType());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongClicked.onClicked(resultItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SearchViewMusicListItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SearchViewMusicListItemBinding.bind(itemView);
        }
    }

    public interface OnSongClicked {
        public void onClicked(SearchResultItem track);
    }
}
