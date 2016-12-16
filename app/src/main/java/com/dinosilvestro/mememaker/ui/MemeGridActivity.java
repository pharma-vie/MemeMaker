package com.dinosilvestro.mememaker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dinosilvestro.mememaker.R;
import com.dinosilvestro.mememaker.adapters.MemeAdapter;
import com.dinosilvestro.mememaker.misc.Keys;
import com.dinosilvestro.mememaker.parcels.MemeParcel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import static com.dinosilvestro.mememaker.misc.Keys.GET_MEME;

public class MemeGridActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_grid);

        RecyclerView memeRecyclerView = (RecyclerView) findViewById(R.id.meme_grid_recycler_view);
        FloatingActionButton uploadActionButton = (FloatingActionButton) findViewById(R.id.upload_meme_action_button);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Keys.GET_MEMES);
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

        // Create new navigation drawer
        new NavigationDrawer(this, mFirebaseAuth);
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Keys.REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Keys.REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            String fullPhotoString = data.getDataString();
            Intent intent = new Intent(this, MemeEditActivity.class);
            intent.putExtra(GET_MEME, fullPhotoString);
            startActivity(intent);
        }
    }
}
