package com.mmga.cloudcover.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.mmga.cloudcover.MyApplication;
import com.mmga.cloudcover.R;
import com.mmga.cloudcover.model.UniformInfo;

import java.util.ArrayList;


public class StarAdapter extends RecyclerView.Adapter<StarAdapter.MyViewHolder> {

    ArrayList<UniformInfo> list;
    Context context;

    public StarAdapter(ArrayList<UniformInfo> list,Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_star, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position).getImageUrl())
                .centerCrop()
                .fitCenter()
                .override(getQuarterWindowWidth(), getQuarterWindowWidth())
                .placeholder(R.drawable.default_bg)
                .error(R.drawable.error_holder)
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(new ImageViewTarget<GlideDrawable>(holder.imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        holder.imageView.setImageDrawable(resource);
                        holder.imageView.setVisibility(View.VISIBLE);
                    }
                });
//                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.gallery_image);
        }
    }


    private int getQuarterWindowWidth() {
        DisplayMetrics dm = MyApplication.getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
}

