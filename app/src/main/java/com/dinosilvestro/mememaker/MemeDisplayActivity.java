package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

public class MemeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_display);

        RecyclerView mMemeRecyclerView = (RecyclerView) findViewById(R.id.meme_grid_recycler_view);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.GET_MEMES);
        MemeParcel[] memeParcel = Arrays.copyOf(parcelables, parcelables.length, MemeParcel[].class);

        MemeAdapter adapter = new MemeAdapter(this, memeParcel);
        mMemeRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMemeRecyclerView.setLayoutManager(layoutManager);

        mMemeRecyclerView.setHasFixedSize(true);
    }
}
