package com.example.quitemusic.ui.main_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.media2.exoplayer.external.Player;
import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.example.quitemusic.R;
import com.example.quitemusic.databinding.ActivityMainBinding;
import com.example.quitemusic.models.SongInfo;
import com.example.quitemusic.services.PlayerCallbacks;
import com.example.quitemusic.services.MusicPlayerService;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mBinding;
    private NavHostFragment navHostFragment;
    private NavController navController;
    private MusicPlayerService mService;
    private boolean mBound = false;
    private SongInfo currentSongInfo;
    private boolean isPlaying = false;
    private PlayerCallbacks.UserPlaybackCallback userPlaybackCallback;
    String url = "https://cdns-preview-d.dzcdn.net/stream/c-d28ee67c24d60e740866c7709d772f55-12.mp3 ";
    Intent serviceIntent = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "onServiceConnected: ");
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;

            mService.setMediaPlayerCallback(new PlayerCallbacks.MediaPlaybackCallback() {
                @Override
                public void onMusicDisturbed(int state, SongInfo song) {
                    switch (state) {
                        case Player.STATE_ENDED:
                            isPlaying = false;
                            mBinding.exoPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                            break;
                        case Player.STATE_BUFFERING:
                            mBinding.exoBuffering.setVisibility(View.VISIBLE);
                            break;
                        case Player.STATE_READY:
                            mBinding.exoBuffering.setVisibility(View.GONE);
                            isPlaying = true;
                            mBinding.exoPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                            break;
                    }
                }

                @Override
                public void onSongChanged(int newPosition) {

                }

                @Override
                public void onMusicProgress(long position) {
                    mBinding.songProgress.setProgress((int) position);
                    Log.d(TAG, "onMusicProgress: " + position);
                }

                @Override
                public void setUserPlaybackCallback(PlayerCallbacks.UserPlaybackCallback callback) {
                    userPlaybackCallback = callback;
                }

                @Override
                public void onMediaIsPlaying(boolean isPlaying) {
                    (MainActivity.this).isPlaying = isPlaying;
                    if(!isPlaying){
                        mBinding.exoPlayPause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    }else {
                        mBinding.exoPlayPause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    }

                }

            });
            SongInfo info = new SongInfo();
            info.setStreamURL(url);
            addMusicViaActivity(info);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        serviceIntent = new Intent(this, MusicPlayerService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        mBinding.songNameTxt.setSelected(true);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, navController);
        mBinding.exoPlayPause.setOnClickListener(this::onClick);
        mBinding.nextTrack.setOnClickListener(this::onClick);
    }


    public void addMusicViaActivity(SongInfo songInfo) {
        if (mService != null) {
            mService.addMediaItem(songInfo);
            Log.d(TAG, "addMusicViaActivity:service");

        } else {
            Log.d(TAG, "addMusicViaActivity: null service");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exo_play_pause:
                if (isPlaying) {
                    userPlaybackCallback.pause();
                } else {
                    userPlaybackCallback.play();
                }
                break;
            case R.id.next_track:
                userPlaybackCallback.nextTrack();
                break;
            default:
                break;
        }
    }
}