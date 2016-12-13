package com.dinosilvestro.mememaker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Get reference to Firebase Realtime Database
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton createActionButton = (FloatingActionButton) findViewById(R.id.create_meme_action_button);
        final CardView defaultCardView = (CardView) findViewById(R.id.default_card_view);
        final RecyclerView savedMemeRecyclerView = (RecyclerView) findViewById(R.id.saved_meme_grid_recycler_view);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);


        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // Already signed in

            // Get reference to Firebase Realtime Database
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            // If the user has already created memes and saved them to the database...
            if (database.child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {

                // Fill RecyclerView with memes from database
                refreshSavedMemes(defaultCardView, savedMemeRecyclerView, database);
            }

            // Get memes from API
            FetchMemes.getMemeData();

            createActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MemeGridActivity.class);
                    intent.putExtra(Keys.GET_MEMES, MemeParcel.getMemes());
                    startActivity(intent);
                }
            });
        } else {
            // Not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.AppTheme)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    Keys.REQUEST_SIGN_IN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent, getTheme()));
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSavedMemes(defaultCardView, savedMemeRecyclerView, mDatabase);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshSavedMemes(CardView defaultCardView, final RecyclerView savedMemeRecyclerView, DatabaseReference mDatabase) {
        // Hide CardView saying they haven't created any memes yet
        defaultCardView.setVisibility(View.GONE);

        DatabaseReference user = mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Get the memes for this user...
        user.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List<SavedMemeParcel> mSavedMemeParcel = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    SavedMemeParcel savedMemeParcel = new SavedMemeParcel();
                    savedMemeParcel.setMemeImageUrl(String.valueOf(child.getValue()));
                    mSavedMemeParcel.add(savedMemeParcel);
                }

                // Load them into an adapter and display them in a RecyclerView
                SavedMemeAdapter adapter = new SavedMemeAdapter(getApplicationContext(), mSavedMemeParcel);
                savedMemeRecyclerView.setAdapter(adapter);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                savedMemeRecyclerView.setLayoutManager(layoutManager);

                savedMemeRecyclerView.setHasFixedSize(true);
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
