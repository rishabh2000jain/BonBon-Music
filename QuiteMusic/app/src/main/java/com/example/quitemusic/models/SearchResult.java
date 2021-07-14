package com.example.quitemusic.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

//    @SerializedName("SearchResult")
    private List<SearchResultItem> searchResult;



    public List<SearchResultItem> getSearchResult() {
        return searchResult;
    }
}