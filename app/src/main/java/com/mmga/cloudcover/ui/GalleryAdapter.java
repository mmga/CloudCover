//package com.mmga.cloudcover.ui;
//
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.mmga.cloudcover.MyApplication;
//import com.mmga.cloudcover.R;
//import com.mmga.cloudcover.model.TestModel;
//
//import java.util.ArrayList;
//
//import io.realm.Realm;
//
//public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
//
//    private Realm realm;
//    ArrayList<TestModel> mInfoList = null;
//
//    public GalleryAdapter() {
//    }
//
//    public GalleryAdapter(ArrayList<TestModel> mInfoList) {
//        this.mInfoList = mInfoList;
//    }
//
//    public void setAdapterData(ArrayList<TestModel> infoList) {
//        this.mInfoList = infoList;
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////        realm = Realm.getDefaultInstance();
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
//        return new GalleryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final GalleryViewHolder holder, int position) {
//
//        holder.textView.setText(mInfoList.get(position).getName());
//
//        Glide.with(MyApplication.getContext())
//                .load("http://p1.music.126.net/br3IrdCvT7-GjCyUVNONiA==/3388694837506899.jpg")
//                .crossFade(500)
//                .centerCrop()
//                .fitCenter()
//                .placeholder(R.drawable.default_bg)
//                .error(R.drawable.error_holder)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        holder.thumbnail.setImageDrawable(resource);
//                    }
//                });
//    }
//
//    @Override
//    public int getItemCount() {
//
//            return mInfoList.size();
//
//    }
//
//
//
//
//    public class GalleryViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView thumbnail;
//        public TextView textView;
//
//        public GalleryViewHolder(View itemView) {
//            super(itemView);
//
//            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
//            textView = (TextView) itemView.findViewById(R.id.textview);
//        }
//    }
//}
