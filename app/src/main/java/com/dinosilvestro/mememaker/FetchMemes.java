package com.dinosilvestro.mememaker;


import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class FetchMemes {

    private static final String TAG = FetchMemes.class.getSimpleName();

    static void getMemeData() {
        String pageIndex = "0";
        String url = "http://version1.api.memegenerator.net/Generators_Select_ByPopular?pageIndex="
                + pageIndex + "&pageSize=12&days=7";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }

}
