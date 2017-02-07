package com.dinosilvestro.mememaker.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dinosilvestro.mememaker.R;
import com.dinosilvestro.mememaker.adapters.SavedMemeAdapter;
import com.dinosilvestro.mememaker.api.FetchMemes;
import com.dinosilvestro.mememaker.misc.Keys;
import com.dinosilvestro.mememaker.parcels.MemeParcel;
import com.dinosilvestro.mememaker.parcels.SavedMemeParcel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dinosilvestro.mememaker.anims.Animations.fabButtonAnimate;

public class MainActivity extends AppCompatActivity {

    // Get reference to Firebase Realtime Database
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    CardView mDefaultCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionButton createActionButton = (FloatingActionButton) findViewById(R.id.create_meme_action_button);
        final RecyclerView savedMemeRecyclerView = (RecyclerView) findViewById(R.id.saved_meme_grid_recycler_view);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mDefaultCardView = (CardView) findViewById(R.id.default_card_view);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Already signed in

            // Get reference to Firebase Realtime Database
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            // If the user has already created memes and saved them to the database...
            if (database.child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {

                // Fill RecyclerView with memes from database
                refreshSavedMemes(savedMemeRecyclerView, database);

                // Check to see if the write to external storage permission has been granted
                // so that the user is able to download saved memes to their device
                storagePermissionCheck();
            }

            // Get memes from API
            FetchMemes.getMemeData();

            createActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fabButtonAnimate(createActionButton);
                    Intent intent = new Intent(getApplicationContext(), MemeGridActivity.class);
                    intent.putExtra(Keys.GET_MEMES, MemeParcel.getMemes());
                    startActivity(intent);
                }
            });

            // Use app's accent color for the swipe refresh layout if
            // the user is using a Marshmallow or later device
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent, getTheme()));
            }

            // Refresh the RecyclerView
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshSavedMemes(savedMemeRecyclerView, mDatabase);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            // Not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.SplashScreenTheme)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    Keys.REQUEST_SIGN_IN);
        }
    }

    private void storagePermissionCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Keys.PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Keys.PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // TODO: Permission was granted. Enable download functionality.

                } else {

                    // TODO: Permission denied. Disable download functionality.
                }
            }
        }
    }

    private void refreshSavedMemes(final RecyclerView savedMemeRecyclerView, DatabaseReference mDatabase) {

        DatabaseReference user = mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Get the memes for this user...
        user.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {

                    // Hide CardView saying they haven't created any memes yet
                    mDefaultCardView.setVisibility(View.GONE);

                    // Create an array with a size equal to the amount of children in the dataSnapshot
                    String[] dataSnapshotData = new String[(int) dataSnapshot.getChildrenCount()];

                    // Iterate through the dataSnapshot and place the data into the array
                    int index = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        dataSnapshotData[index] = String.valueOf(child.getValue());
                        index++;
                    }

                    // Loop backwards through the array containing the data from the dataSnapshot
                    // so most recently created memes appear first in the RecyclerView later
                    List<SavedMemeParcel> mSavedMemeParcel = new ArrayList<>();
                    for (int i = dataSnapshotData.length - 1; i >= 0; i--) {
                        SavedMemeParcel savedMemeParcel = new SavedMemeParcel();
                        savedMemeParcel.setMemeImageUrl(dataSnapshotData[i]);
                        mSavedMemeParcel.add(savedMemeParcel);
                    }

                    // Load saved memes into an adapter and display them in a RecyclerView
                    SavedMemeAdapter adapter = new SavedMemeAdapter(getApplicationContext(), mSavedMemeParcel);
                    savedMemeRecyclerView.setAdapter(adapter);

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    savedMemeRecyclerView.setLayoutManager(layoutManager);

                    savedMemeRecyclerView.setHasFixedSize(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // user is signed in!
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Sign in canceled
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, R.string.sign_in_cancelled_text, Toast.LENGTH_SHORT).show();
            return;
        }

        // No network
        if (resultCode == ResultCodes.RESULT_NO_NETWORK) {
            Toast.makeText(this, R.string.no_internet_connection_text, Toast.LENGTH_SHORT).show();

        }

        // User is not signed in. Maybe just wait for the user to press
        // "sign in" again, or show a message.
    }


}
