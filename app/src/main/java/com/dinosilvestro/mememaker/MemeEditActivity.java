package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MemeEditActivity extends AppCompatActivity {

    ImageView mMemeEditImage;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_edit);

        mMemeEditImage = (ImageView) findViewById(R.id.meme_edit_image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Intent intent = getIntent();

        if (intent != null) {
            Picasso.with(this).load(intent.getStringExtra(Keys.GET_MEME)).into(mMemeEditImage, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    Toast.makeText(MemeEditActivity.this, R.string.image_upload_error_toast, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
