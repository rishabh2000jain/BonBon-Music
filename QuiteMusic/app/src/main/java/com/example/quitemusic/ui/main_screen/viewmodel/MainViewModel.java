package com.example.quitemusic.ui.main_screen.viewmodel;

import android.util.Log;

import com.example.quitemusic.models.SearchResult;
import com.example.quitemusic.models.SearchResultItem;
import com.example.quitemusic.models.SongInfo;
import com.example.quitemusic.retrofit.Callbacks;
import com.example.quitemusic.retrofit.RetrofitSingleton;
import com.example.quitemusic.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private RetrofitSingleton.Api api;
    private final MediatorLiveData<List<SearchResultItem>> tracksMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<SongInfo> songInfoMediatorLiveData = new MediatorLiveData<>();
    private Map<String, String> map = new HashMap<>();
    private CompositeDisposable container;

    public MainViewModel() {
        api = RetrofitSingleton.getInstance();
        map.put(Constants.CLIENT_ID_KEY_KEY, Constants.CLIENT_ID);
        map.put(Constants.CLIENT_HOST_KEY, Constants.CLIENT_HOST_VALUE);
        Log.d(TAG, "MainViewModel: created");
        container = new CompositeDisposable();
    }

    public void searchSongList(String query, final Callbacks callbacks) {
        callbacks.loadingData();
        final LiveData<List<SearchResultItem>> responseLiveData = LiveDataReactiveStreams.fromPublisher(
                api.getTracksByQuery(map, query, "all")
                        .subscribeOn(Schedulers.io())
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .onErrorReturn(throwable -> {
                            return new ArrayList<>();
                        }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callbacks.error();
                    }
                })
        );

        tracksMediatorLiveData.addSource(responseLiveData, response -> {
            if (response != null) {
                tracksMediatorLiveData.setValue(response);
                tracksMediatorLiveData.removeSource(responseLiveData);
                callbacks.result();
            } else {
                callbacks.emptyResult();
            }
        });
    }

    public LiveData<List<SearchResultItem>> getTracks() {
        return tracksMediatorLiveData;
    }

    public void getSongInfoFromApi(String url, Callbacks callbacks) {
        callbacks.loadingData();
        Observable<SongInfo> songInfoObservable = api.getTracksDetails(map, url);
        Observable<String> songUrl = api.getStreamUrl(map, url);

        Observable<SongInfo> observable = Observable.zip(songInfoObservable, songUrl, new BiFunction<SongInfo, String, SongInfo>() {
            @NonNull
            @Override
            public SongInfo apply(@NonNull SongInfo songInfo, @NonNull String s) throws Exception {
                songInfo.setStreamURL(s);
                return songInfo;
            }
        });
        container.add(observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<SongInfo>() {
            @Override
            public void accept(SongInfo songInfo) throws Exception {
                songInfoMediatorLiveData.setValue(songInfo);
            }
        }));

    }

    public LiveData<SongInfo> observeSongInfo() {
        return songInfoMediatorLiveData;
    }

    @Override
    protected void onCleared() {
        container.clear();
        super.onCleared();
    }
}
