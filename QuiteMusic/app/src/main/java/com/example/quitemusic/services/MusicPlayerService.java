package com.example.quitemusic.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.EventLogTags;
import android.util.Log;


import com.example.quitemusic.R;
import com.example.quitemusic.models.SongInfo;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import androidx.annotation.Nullable;
import androidx.media2.exoplayer.external.ExoPlayerFactory;

public class MusicPlayerService extends Service implements PlayerCallbacks.UserPlaybackCallback {
    private static final String TAG = "MusicPlayerService";
    private SimpleExoPlayer exoPlayer;
    private PlayerCallbacks.MediaPlaybackCallback callbacks;
    private final IBinder binder = new LocalBinder();
    private Handler musicTimeHandler;
    private PlayerNotificationManager playerNotificationManager;

    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            Log.d(TAG, "getService: ");
            // Return this instance of LocalService so clients can call public methods
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        playerNotificationManager = new PlayerNotificationManager.Builder(this, 100, "music", new PlayerNotificationManager.MediaDescriptionAdapter() {
            @Override
            public CharSequence getCurrentContentTitle(Player player) {
                return "music";
            }

            @Nullable
            @Override
            public PendingIntent createCurrentContentIntent(Player player) {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getCurrentContentText(Player player) {
                return "music hello";
            }

            @Nullable
            @Override
            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                return null;
            }
        }).build();
        exoPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        playerNotificationManager.setPlayer(exoPlayer);
        initListener();
    }

    private void initListener() {
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onTimelineChanged(Timeline timeline, int reason) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d(TAG, "onPlayerError: " + error.getMessage());
                Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());

            }


            @Override
            public void onPlaybackStateChanged(int state) {
                callbacks.onMusicDisturbed(state, null);
                switch (state) {
                    //when player finished playing song

                    case Player.STATE_ENDED:

                        if (musicTimeHandler != null) {
                            musicTimeHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                    default:

                        break;

                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                callbacks.onMediaIsPlaying(isPlaying);
            }
        });


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return binder;
    }


    public void addMediaItem(SongInfo songInfo) {
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(songInfo.getStreamURL()));
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.setPlayWhenReady(true);
        sendTimeChanges();
        exoPlayer.prepare();
        exoPlayer.play();
        Log.d(TAG, "addMediaItem: " + songInfo.getStreamURL());
    }

    public void sendTimeChanges() {
        musicTimeHandler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                callbacks.onMusicProgress(exoPlayer.getCurrentPosition() == 0 ? 0 : (exoPlayer.getCurrentPosition() * 100) / exoPlayer.getDuration());
                musicTimeHandler.postDelayed(this, 1000);
            }
        };
        musicTimeHandler.postDelayed(runnable, 0);

    }

    public void setMediaPlayerCallback(PlayerCallbacks.MediaPlaybackCallback callbacks) {
        this.callbacks = callbacks;
        callbacks.setUserPlaybackCallback(this);
    }

    //when we start service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        exoPlayer.release();
        playerNotificationManager.setPlayer(null);
        playerNotificationManager = null;
    }

    @Override
    public void play() {
        if (exoPlayer != null) {
            if (exoPlayer.getPlaybackState() == Player.STATE_ENDED) {
                exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(true);
                callbacks.onMusicProgress(0);
                sendTimeChanges();
            } else {
                exoPlayer.play();
            }
        }
    }

    @Override
    public void pause() {
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    public void seek(int position) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(position * exoPlayer.getDuration() / 100);
        }
    }

    @Override
    public void nextTrack() {
        exoPlayer.next();
    }
}
