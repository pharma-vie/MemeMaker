package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MemeEditActivity extends AppCompatActivity {

    ImageView mMemeEditImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_edit);

        mMemeEditImage = (ImageView) findViewById(R.id.meme_edit_image_view);

        Intent intent = getIntent();

        if (intent != null) {
            Picasso.with(this).load(intent.getStringExtra(Keys.GET_MEME)).into(mMemeEditImage);
        }
    }
}
