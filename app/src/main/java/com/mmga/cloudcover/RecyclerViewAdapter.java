package com.mmga.cloudcover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by mmga on 2015/11/22.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<Data> dataList = null;


    ImageLoader imageLoader;

    public RecyclerViewAdapter(ArrayList<Data> dataList) {
        ImageLoader imageLoader = new ImageLoader(MyApplication.getInstance().getRequestQueue(), new BitmapCache());
        this.imageLoader = imageLoader;
        this.dataList = dataList;
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

        holder.title.setText(dataList.get(position).getSongName());
        holder.artistAndAlbum.setText(dataList.get(position).getArtistName() + "--" + dataList.get(position).getAlbumName());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.cardImage, R.drawable.default_bg, R.mipmap.ic_launcher);
        imageLoader.get(dataList.get(position).getCoverUrl(), listener, 200, 200);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
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