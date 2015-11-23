package com.mmga.cloudcover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Songs> songList = null;

    public RecyclerViewAdapter(ArrayList<Songs> songList) {
        this.songList = songList;
    }

    public void setAdapterData(ArrayList<Songs> songList) {
        this.songList = songList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        holder.title.setText(dataList.get(position).getSongName());
//        holder.artistAndAlbum.setText(dataList.get(position).getArtistName() + "--" + dataList.get(position).getAlbumName());


        holder.title.setText(songList.get(position).getName());
        holder.artistAndAlbum.setText(songList.get(position).getArtists().get(0).getName() + "--" + songList.get(position).getAlbum().getName());

        Glide.with(MyApplication.getContext())
                .load(songList.get(position).getAlbum().getPicUrl())
                .asBitmap()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.drawable.default_bg)
                .centerCrop()
                .fitCenter()
                .into(holder.cardImage);



    }


    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView cardImage;
        public TextView title, artistAndAlbum;


        public MyViewHolder(View itemView) {
            super(itemView);

            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            title = (TextView) itemView.findViewById(R.id.title);
            artistAndAlbum = (TextView) itemView.findViewById(R.id.artist_album);


        }
    }


}