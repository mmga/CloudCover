package com.mmga.cloudcover;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

public class InfoDialog extends Dialog {

    private TextView mSongName,mArtistName, mAlbumName,publishTime;
    private String songName,artistName, albumName;
    private Context context;

    String[] entryName = new String[]{"音乐名：","歌手：","所属专辑：","发行时间: "};

    protected InfoDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected InfoDialog(Context context, int theme) {
        super(context, theme);
    }

    protected InfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_dialog);
        mSongName = (TextView) findViewById(R.id.song_name);
        mArtistName = (TextView) findViewById(R.id.artist_name);
        mAlbumName = (TextView) findViewById(R.id.album_name);
        publishTime = (TextView) findViewById(R.id.publish_time);


        SpannableString spanSongName = new SpannableString(entryName[0] + songName);
        SpannableString spanArtistName = new SpannableString(entryName[1] + artistName);
        SpannableString spanAlbumName = new SpannableString(entryName[2] + albumName);

        spanSongName.setSpan(new ForegroundColorSpan(
                Color.parseColor("#70f4f4f4"))
                ,0,entryName[0].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanArtistName.setSpan(new ForegroundColorSpan(
                Color.parseColor("#70f4f4f4"))
                ,0,entryName[1].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanAlbumName.setSpan(new ForegroundColorSpan(
                Color.parseColor("#70f4f4f4"))
                ,0,entryName[2].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSongName.setText(spanSongName);
        mArtistName.setText(spanArtistName);
        mAlbumName.setText(spanAlbumName);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.height = (int) (display.getHeight() * 0.4);
        layoutParams.width = (int) (display.getWidth() * 0.6);
        this.getWindow().setAttributes(layoutParams);
        Log.d("mmga", "windowManager");

    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
}
