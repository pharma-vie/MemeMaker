package com.dinosilvestro.mememaker.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.dinosilvestro.mememaker.R;
import com.dinosilvestro.mememaker.parcels.SavedMemeParcel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class SavedMemeAdapter extends RecyclerView.Adapter<SavedMemeAdapter.MemeAdapterViewHolder> {

    private List<SavedMemeParcel> mMemes;
    private Context mContext;

    public SavedMemeAdapter(Context context, List<SavedMemeParcel> memes) {
        mContext = context;
        mMemes = memes;
    }

    @Override
    public MemeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meme_grid_item, parent, false);
        return new MemeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemeAdapterViewHolder holder, int position) {
        holder.bindMeme(mMemes.get(position));
    }

    @Override
    public int getItemCount() {
        return mMemes.size();
    }

    public class MemeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMemeImageView;
        private String mMemeUrl;

        public MemeAdapterViewHolder(View itemView) {
            super(itemView);
            mMemeImageView = (ImageView) itemView.findViewById(R.id.meme_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindMeme(SavedMemeParcel memes) {
            mMemeUrl = memes.getMemeImageUrl();
            Picasso.with(mContext).load(mMemeUrl).into(mMemeImageView);
        }

        @Override
        public void onClick(View v) {
            v.startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.context_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        //Open selected meme in the browser
                        case R.id.menu_open_in_browser:
                            Uri webPage = Uri.parse(mMemeUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                                mContext.startActivity(intent);
                            }
                            break;
                        // Use Picasso to download the selected meme into the phone's gallery
                        case R.id.menu_download:
                            Picasso.with(mContext).load(mMemeUrl).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Toast.makeText(mContext, "Saving meme...", Toast.LENGTH_SHORT).show();
                                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                                            bitmap, mMemeUrl, "This is a meme");
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                    Toast.makeText(mContext, "Unable to download meme. Try again later.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                            break;
                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
    }
}