package com.dinosilvestro.mememaker;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        public MemeAdapterViewHolder(View itemView) {
            super(itemView);
            mMemeImageView = (ImageView) itemView.findViewById(R.id.meme_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindMeme(MemeParcel memes) {
            Picasso.with(mContext).load(memes.getMemeImageUrl()).resize(250, 250).into(mMemeImageView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}