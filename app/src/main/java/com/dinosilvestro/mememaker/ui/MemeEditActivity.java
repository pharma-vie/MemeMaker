package com.dinosilvestro.mememaker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinosilvestro.mememaker.R;
import com.dinosilvestro.mememaker.misc.InputTextWatcher;
import com.dinosilvestro.mememaker.misc.Keys;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class MemeEditActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private RelativeLayout mMemeContainer;
    private FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_edit);

        ImageView memeEditImage = (ImageView) findViewById(R.id.meme_edit_image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        FloatingActionButton saveMemeActionButton = (FloatingActionButton) findViewById(R.id.save_meme_action_button);
        mMemeContainer = (RelativeLayout) findViewById(R.id.meme_container);

        // TextViews that will be used for the top and bottom sections the meme
        // Invisible until user enters text into the matching EditTextView
        TextView topTextView = (TextView) findViewById(R.id.top_text_view);
        topTextView.setText("");
        topTextView.setVisibility(View.INVISIBLE);
        TextView bottomTextView = (TextView) findViewById(R.id.bottom_text_view);
        bottomTextView.setText("");
        bottomTextView.setVisibility(View.INVISIBLE);

        EditText topEditText = (EditText) findViewById(R.id.top_edit_text);
        topEditText.addTextChangedListener(new InputTextWatcher(topTextView));

        EditText bottomEditText = (EditText) findViewById(R.id.bottom_edit_text);
        bottomEditText.addTextChangedListener(new InputTextWatcher(bottomTextView));

        Intent intent = getIntent();

        if (intent != null) {
            Picasso.with(this).load(intent.getStringExtra(Keys.GET_MEME)).into(memeEditImage, new Callback() {
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

        saveMemeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data = compressMeme();

                // Check to make sure user is still logged in, save edited meme to Firebase Storage
                // and Firebase Realtime Database
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String path = FirebaseAuth.getInstance().getCurrentUser().getEmail() + "/" + UUID.randomUUID() + ".png";
                    StorageReference storageReference = mFirebaseStorage.getReference(path);


                    UploadTask uploadTask = storageReference.putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Create path in the realtime database if it doesn't exist
                            // or add to it if it does
                            mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("memes").push()
                                    .setValue(taskSnapshot.getDownloadUrl().toString());

                            Toast.makeText(MemeEditActivity.this, R.string.save_meme_success_toast, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MemeEditActivity.this, R.string.save_meme_failure_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Compress meme with user-generated text into a byte array
    private byte[] compressMeme() {
        mMemeContainer.setDrawingCacheEnabled(true);
        mMemeContainer.buildDrawingCache();
        Bitmap bitmap = mMemeContainer.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        mMemeContainer.setDrawingCacheEnabled(false);
        return byteArrayOutputStream.toByteArray();
    }
}