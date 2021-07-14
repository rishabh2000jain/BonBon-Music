package com.example.quitemusic.ui.main_screen.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.quitemusic.databinding.FragmentSearchBinding;
import com.example.quitemusic.models.SearchResultItem;
import com.example.quitemusic.models.SongInfo;
import com.example.quitemusic.retrofit.Callbacks;
import com.example.quitemusic.ui.main_screen.MainActivity;
import com.example.quitemusic.ui.main_screen.SongListAdapter;
import com.example.quitemusic.ui.main_screen.viewmodel.MainViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchFragment extends Fragment implements SongListAdapter.OnSongClicked {
    private static final String TAG = "SearchFragment";
    private MainViewModel sharedViewModel;
    private FragmentSearchBinding mBinding;
    private CompositeDisposable disposable;
    private SongListAdapter sogsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSearchBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        disposable = new CompositeDisposable();
        init();
        subscribe();
        initSearchBar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        sogsAdapter = new SongListAdapter(getContext(), this::onClicked);
        mBinding.recyclerView.setAdapter(sogsAdapter);
        mBinding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sharedViewModel.searchSongList("", callbacks);
                mBinding.swipeToRefresh.setRefreshing(false);
            }
        });
    }

    private void subscribe() {
        sharedViewModel.getTracks().observe(getActivity(), searchResultItems -> {
            if (searchResultItems != null) {
                sogsAdapter.setTrackList(searchResultItems);
            }
        });
    }


    private void initSearchBar() {
        disposable.add(RxSearchObservable.fromView(mBinding.searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                        sharedViewModel.searchSongList(s, callbacks);
                    }
                }, throwable -> {

                }));
    }

    Callbacks callbacks = new Callbacks() {
        @Override
        public void loadingData() {
            showProgress();
        }

        @Override
        public void error() {
            hideProgress();
        }

        @Override
        public void result() {
            hideProgress();
        }

        @Override
        public void emptyResult() {
            hideProgress();
        }

    };

    static class RxSearchObservable {
        public static Observable<String> fromView(SearchView searchView) {
            final PublishSubject<String> subject = PublishSubject.create();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
//                    subject.onNext(s);
                    subject.onComplete();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    subject.onNext(s);
                    return false;
                }
            });
            return subject;
        }
    }

    private void showProgress() {
        if (mBinding.progressCircular.getVisibility() != View.VISIBLE) {
            mBinding.progressCircular.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        if (mBinding.progressCircular.getVisibility() != View.GONE) {
            mBinding.progressCircular.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClicked(SearchResultItem track) {
        MainActivity mainActivity = (MainActivity) getActivity();
        sharedViewModel.getSongInfoFromApi(track.getUrl(), callbacks);
        sharedViewModel.observeSongInfo().observe(getActivity(), new Observer<SongInfo>() {
            @Override
            public void onChanged(SongInfo songInfo) {
                if(songInfo!=null)
                mainActivity.addMusicViaActivity(songInfo);
            }
        });
    }

    @Override
    public void onDestroyView() {
        disposable.clear();
        disposable.dispose();
        super.onDestroyView();
    }
}