package com.example.quitemusic.ui.main_screen.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quitemusic.databinding.FragmentHomeBinding;
import com.example.quitemusic.models.SearchResultItem;
import com.example.quitemusic.ui.main_screen.viewmodel.MainViewModel;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding mBinding;
    private MainViewModel sharedViewModel;
    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);


    }

    private void subscribe() {
        sharedViewModel.getTracks().observe(getViewLifecycleOwner(), new Observer<List<SearchResultItem>>() {
            @Override
            public void onChanged(List<SearchResultItem> tracks) {

            }
        });
    }
}

