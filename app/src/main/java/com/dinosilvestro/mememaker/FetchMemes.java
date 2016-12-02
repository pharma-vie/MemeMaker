package com.dinosilvestro.mememaker;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchMemes {

    private static final String TAG = FetchMemes.class.getSimpleName();

    static void getMemeData() {
        String url = "https://api.imgflip.com/get_memes";

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
                    fetchMemes(jsonData);
                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });
    }

    private static MemeParcel[] getMemes(String jsonData) throws JSONException {
        JSONObject jsonResults = new JSONObject(jsonData);
        JSONObject data = jsonResults.getJSONObject("data");
        JSONArray memes = data.getJSONArray("memes");
        MemeParcel[] memeParcels = new MemeParcel[memes.length()];

        for (int i = 0; i < memes.length(); i++) {
            JSONObject meme = memes.getJSONObject(i);
            MemeParcel memeParcel = new MemeParcel();
            memeParcel.setMemeImageUrl(meme.getString("url"));
            memeParcels[i] = memeParcel;
            Log.i(TAG, meme.getString("url"));
        }

        return memeParcels;
    }

    private static MemeParcel fetchMemes(String jsonData) throws JSONException {
        MemeParcel memeParcel = new MemeParcel();
        memeParcel.setMemes(getMemes(jsonData));
        return memeParcel;
    }

}
