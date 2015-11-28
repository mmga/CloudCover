package com.mmga.cloudcover.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.mmga.cloudcover.MyApplication;
import com.mmga.cloudcover.R;
import com.mmga.cloudcover.util.ShareUtils;
import com.mmga.cloudcover.util.ToastUtil;
import com.mmga.cloudcover.wigdet.InfoDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int BAR_INVISIBLE = 1000;
    private static final int BAR_VISIBLE = 1001;
    private static final int FLAG_SAVE = 0;
    private static final int FLAG_SHARE = 1;
    private int isBarVisible = BAR_INVISIBLE;
    private LinearLayout bottomButtonBar;
    private RelativeLayout topButtonBar;
    private ImageView fullSizeImage,backButton, infoButton;
    private LinearLayout saveButton,shareButton, starButton;
    private PhotoViewAttacher mPhotoViewAttacher;
    private String imageUrl,albumName,artistName,songName;
    private Uri uri = null;
    private Context context = MyApplication.getContext();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();

        Intent intent = getIntent();
        this.imageUrl = intent.getStringExtra("imageUrl");
        this.albumName = intent.getStringExtra("albumName");
        this.artistName = intent.getStringExtra("artistName");
        this.songName = intent.getStringExtra("songName");


        Glide.with(context)
                .load(imageUrl)
                .crossFade(500)
                .error(R.drawable.error_holder)
                .listener(listener)
                .into(fullSizeImage);

    }



    private void initView() {

        bottomButtonBar = (LinearLayout) findViewById(R.id.bottom_bar);
        topButtonBar = (RelativeLayout) findViewById(R.id.top_bar);
        fullSizeImage = (ImageView) findViewById(R.id.fullsize_image);
        saveButton = (LinearLayout) findViewById(R.id.save_button);
        shareButton = (LinearLayout) findViewById(R.id.share_button);
        starButton = (LinearLayout) findViewById(R.id.star_button);
        backButton = (ImageView) findViewById(R.id.back_button);
        infoButton = (ImageView) findViewById(R.id.detail_info_button);
        saveButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        starButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        infoButton.setOnClickListener(this);
    }

    private RequestListener listener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            fullSizeImage.setImageDrawable(resource);
            setupPhotoViewAttacher();
            return true;
        }
    };




    private void setupPhotoViewAttacher() {
        mPhotoViewAttacher = new PhotoViewAttacher(fullSizeImage);
        mPhotoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                switchButtonBar();
            }
        });

        //自动放大图片至全屏
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPhotoViewAttacher.setScale(mPhotoViewAttacher.getMediumScale(), true);
//                mPhotoViewAttacher.update();
//            }
//        }, 500);
    }

    private void switchButtonBar() {
        if (isBarVisible == BAR_INVISIBLE) {
            showButtonBar();
            isBarVisible = BAR_VISIBLE;
        } else {
            hideButtonBar();
            isBarVisible = BAR_INVISIBLE;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            switchButtonBar();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.detail_info_button:
                showDetailInfo();
                break;
            case R.id.save_button:
                saveOrShareImageToGallery(FLAG_SAVE);
                break;
            case R.id.star_button:
                ToastUtil.showLong("人家还没准备好..");
                break;
            case R.id.share_button:
                saveOrShareImageToGallery(FLAG_SHARE);
                break;
        }
    }



    private void showDetailInfo() {
        InfoDialog infoDialog = new InfoDialog(ImageActivity.this,R.style.infoDialog);
        infoDialog.setSongName(songName);
        infoDialog.setAlbumName(albumName);
        infoDialog.setArtistName(artistName);
        infoDialog.show();

    }

    private void saveOrShareImageToGallery(final int flag) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .toBytes()
                .into(new SimpleTarget<byte[]>() {
                    @Override
                    public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        File fileDir = new File(Environment.getExternalStorageDirectory(), "MyCovers");
                        if (!fileDir.exists()) {
                            fileDir.mkdir();
                        }
                        String fileName = songName + "-" + artistName + "-" + albumName + ".jpg";
                        File file = new File(fileDir, fileName);
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            fos.write(resource);
                            fos.close();

                            //通知图库更新
                            String path = file.getAbsolutePath();
                            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

                            uri = Uri.fromFile(file);
                            if (flag == FLAG_SHARE) {
                                ShareUtils.shareImage(ImageActivity.this, uri);
                            } else {
                                ToastUtil.showShort("图片已保存" + fileDir);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }



    private void showButtonBar() {
        topButtonBar.setVisibility(View.VISIBLE);
        bottomButtonBar.setVisibility(View.VISIBLE);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(topButtonBar, "translationY", -300, 0);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(bottomButtonBar, "translationY", 300, 0);
        animatorSet.playTogether(anim1, anim2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void hideButtonBar() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(topButtonBar, "translationY", 0, -300);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(bottomButtonBar, "translationY", 0, 300);
        animatorSet.playTogether(anim1, anim2);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(500);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                topButtonBar.setVisibility(View.GONE);
                bottomButtonBar.setVisibility(View.GONE);
            }
        });
    }


}
