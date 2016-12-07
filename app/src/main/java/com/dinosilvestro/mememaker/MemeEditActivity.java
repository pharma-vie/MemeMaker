package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MemeEditActivity extends AppCompatActivity {

    ImageView mMemeEditImage;
    ProgressBar mProgressBar;
    TextView mTopTextView;
    TextView mBottomTextView;
    EditText mTopEditText;
    EditText mBottomEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_edit);

        mMemeEditImage = (ImageView) findViewById(R.id.meme_edit_image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        // TextViews that will be used for the top and bottom sections the meme
        // Invisible until user enters text into the matching EditTextView
        mTopTextView = (TextView) findViewById(R.id.top_text_view);
        mTopTextView.setText("");
        mTopTextView.setVisibility(View.INVISIBLE);
        mBottomTextView = (TextView) findViewById(R.id.bottom_text_view);
        mBottomTextView.setText("");
        mBottomTextView.setVisibility(View.INVISIBLE);

        mTopEditText = (EditText) findViewById(R.id.top_edit_text);
        mTopEditText.addTextChangedListener(new InputTextWatcher(mTopTextView));

        mBottomEditText = (EditText) findViewById(R.id.bottom_edit_text);
        mBottomEditText.addTextChangedListener(new InputTextWatcher(mBottomTextView));

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
