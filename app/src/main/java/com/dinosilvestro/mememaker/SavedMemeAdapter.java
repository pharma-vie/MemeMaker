package com.dinosilvestro.mememaker;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
        }
    }
}