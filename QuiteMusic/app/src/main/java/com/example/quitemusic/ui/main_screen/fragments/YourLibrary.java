package com.example.quitemusic.ui.main_screen.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quitemusic.R;
import com.example.quitemusic.databinding.FragmentYourLibraryBinding;
import com.example.quitemusic.ui.main_screen.viewmodel.MainViewModel;

public class YourLibrary extends Fragment {

    private MainViewModel sharedViewModel;
    private FragmentYourLibraryBinding mBinding;

    public YourLibrary(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentYourLibraryBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

    }
}