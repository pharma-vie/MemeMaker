package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Arrays;

import static com.dinosilvestro.mememaker.MemeAdapter.GET_MEME_URL;

public class MemeGridActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_grid);

        RecyclerView memeRecyclerView = (RecyclerView) findViewById(R.id.meme_grid_recycler_view);
        FloatingActionButton uploadActionButton = (FloatingActionButton) findViewById(R.id.upload_meme_action_button);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.GET_MEMES);
        MemeParcel[] memeParcel = Arrays.copyOf(parcelables, parcelables.length, MemeParcel[].class);

        MemeAdapter adapter = new MemeAdapter(this, memeParcel);
        memeRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        memeRecyclerView.setLayoutManager(layoutManager);

        memeRecyclerView.setHasFixedSize(true);

        uploadActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            String fullPhotoString = data.getDataString();
            Intent intent = new Intent(this, MemeEditActivity.class);
            intent.putExtra(GET_MEME_URL, fullPhotoString);
            startActivity(intent);
        }
    }
}
