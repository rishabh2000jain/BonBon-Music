package com.example.quitemusic.retrofit;


import com.example.quitemusic.models.SearchResult;
import com.example.quitemusic.models.SearchResultItem;
import com.example.quitemusic.models.SongInfo;
import com.example.quitemusic.util.Constants;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableZip;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;


public class RetrofitSingleton {
    private static Api api = null;
    private static Retrofit retrofit = null;

    private RetrofitSingleton() {
    }

    public static Api getInstance() {
        if (api == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            api = retrofit.create(Api.class);
        }
        return api;
    }

    public interface Api {
        @GET("search")
        Flowable<List<SearchResultItem>> getTracksByQuery(@HeaderMap Map<String, String> map, @Query("query") String query, @Query("type") String type);

        @GET("song/info")
        Observable<SongInfo> getTracksDetails(@HeaderMap Map<String, String> map, @Query("track_url") String url);

        @GET("song/streamurl")
        Observable<String> getStreamUrl(@HeaderMap Map<String, String> map, @Query("track_url") String url);


    }
}
