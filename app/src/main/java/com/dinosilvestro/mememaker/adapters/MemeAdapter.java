package com.dinosilvestro.mememaker.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dinosilvestro.mememaker.R;
import com.dinosilvestro.mememaker.misc.Keys;
import com.dinosilvestro.mememaker.parcels.MemeParcel;
import com.dinosilvestro.mememaker.ui.MemeEditActivity;
import com.squareup.picasso.Picasso;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.MemeAdapterViewHolder> {

    private MemeParcel[] mMemes;
    private Context mContext;

    public MemeAdapter(Context context, MemeParcel[] memes) {
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
        holder.bindMeme(mMemes[position]);
    }

    @Override
    public int getItemCount() {
        return mMemes.length;
    }

    public class MemeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMemeImageView;
        private String mMemeUrl;

        public MemeAdapterViewHolder(View itemView) {
            super(itemView);
            mMemeImageView = (ImageView) itemView.findViewById(R.id.meme_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindMeme(MemeParcel memes) {
            mMemeUrl = memes.getMemeImageUrl();
            Picasso.with(mContext).load(mMemeUrl).into(mMemeImageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MemeEditActivity.class);
            intent.putExtra(Keys.GET_MEME, mMemeUrl);
            mContext.startActivity(intent);
        }
    }
}