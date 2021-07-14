package com.example.quitemusic.services;

import com.example.quitemusic.models.SongInfo;

public interface PlayerCallbacks {
    interface MediaPlaybackCallback {

        void onMusicDisturbed(int state, SongInfo song);

        void onSongChanged(int newPosition);

        void onMusicProgress(long position);

        void setUserPlaybackCallback(UserPlaybackCallback callback);

        void onMediaIsPlaying(boolean isPlaying);
    }

    interface UserPlaybackCallback {
        void play();

        void pause();

        void seek(int position);

        void nextTrack();
    }
}

