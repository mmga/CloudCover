package com.mmga.cloudcover.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mmga.cloudcover.MyApplication;
import com.mmga.cloudcover.R;
import com.mmga.cloudcover.model.Songs;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener{



    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, Songs songs);
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener = null;

    ArrayList<Songs> newSongList;
    ArrayList<Songs> allSongList = null;

    public RecyclerViewAdapter(ArrayList<Songs> songList) {
        this.allSongList = songList;
    }

    public void setAdapterData(ArrayList<Songs> songList) {
        this.allSongList = songList;
        notifyDataSetChanged();
    }

    public void addAdapterData(ArrayList<Songs> songList) {
        newSongList = songList;
        allSongList.addAll(newSongList);
        notifyDataSetChanged();
    }

    public void clearAdapterData() {
        allSongList.clear();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.title.setText(allSongList.get(position).getName());
        if (allSongList.get(position).getArtists().size() != 0) {
            holder.artistAndAlbum.setText(allSongList.get(position).getArtists().get(0).getName()
                    + "--" + allSongList.get(position).getAlbum().getName());
        }

        String imageUrl = allSongList.get(position).getAlbum().getPicUrl();
        holder.card.setTag(allSongList.get(position));

        Glide.with(MyApplication.getContext())
                .load(imageUrl)
                .crossFade(500)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.default_bg)
                .centerCrop()
                .fitCenter()
                .into(holder.cardImage);

    }


    @Override
    public int getItemCount() {
        return allSongList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnRecyclerViewItemClickListener != null) {
            mOnRecyclerViewItemClickListener.onItemClick(v, (Songs) v.getTag());
            Log.d("mmga", "getTag = " + v.getTag());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnRecyclerViewItemClickListener = listener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView cardImage;
        public TextView title, artistAndAlbum;
        private View card;


        public MyViewHolder(View itemView) {
            super(itemView);

            card = itemView;
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            title = (TextView) itemView.findViewById(R.id.title);
            artistAndAlbum = (TextView) itemView.findViewById(R.id.artist_album);

        }
    }





}