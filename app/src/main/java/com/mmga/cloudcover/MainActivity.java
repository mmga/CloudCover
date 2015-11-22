package com.mmga.cloudcover;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyApplication helper = MyApplication.getInstance();
    RequestQueue queue = helper.getRequestQueue();

    Gson gson;

    ArrayList<Songs> songsList;

    private RecyclerView mRecyclerView;
    private TextView mTitle;
    private EditText mSearchText;
    private ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = (TextView) findViewById(R.id.title);
        mSearchText = (EditText) findViewById(R.id.edittext);
        mSearchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    startSearching();
                    return true;
                }

                return false;
            }
        });

        searchIcon = (ImageView) findViewById(R.id.search_icon);

        searchIcon.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        gson = new Gson();

        String title = getUrlFromSharedPreferences();
        mTitle.setText(String.format("《%s》", title));
        String url = encodeInputToImgUrl(title);
        parseJson(queue, url);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }


    //网络请求，返回json数据并解析映射到songs
    private ArrayList<Songs> parseJson(RequestQueue queue, String url) {
        StringRequest stringRequest = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mmga", "str = " + response);
                        if (response.contains("[")) {
                            String songString = response.substring(response.indexOf("["), response.lastIndexOf("]") + 1);
                            Log.d("mmga", "songStr = " + songString);
                            Type listType = new TypeToken<ArrayList<Songs>>() {
                            }.getType();
                            songsList = gson.fromJson(songString, listType);

                            ArrayList<Data> dataList = new ArrayList<>();

                            for (int i = songsList.size() - 1; i >= 0; i--) {
                                Data data = new Data();
                                data.setSongName(songsList.get(i).getName());
                                data.setArtistName(songsList.get(i).getArtists().get(0).getName());
                                data.setAlbumName(songsList.get(i).getAlbum().getName());
                                data.setCoverUrl(songsList.get(i).getAlbum().getPicUrl());
                                dataList.add(0, data);
                            }

                            RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(dataList);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            showErrorDialog();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorDialog();
                    }
                });
        queue.add(stringRequest);

        return songsList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.search_icon):
                if (mSearchText.getVisibility() == View.GONE) {
                    mTitle.setVisibility(View.GONE);
                    mSearchText.setVisibility(View.VISIBLE);
                    mSearchText.requestFocus();
                    openKeyboard(MainActivity.this, mSearchText);
                } else {
                    startSearching();
                }
                break;

        }
    }

    //解码，解决中文，空格等问题
    private String encodeInputToImgUrl(String userInput) {
        try {
            String encodeStr = URLEncoder.encode(userInput, "UTF-8");
            return "http://s.music.163.com/search/get/?type=1&limit=30&offset=0&s=" + encodeStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTitleToSharedPreferences(String string) {
        SharedPreferences.Editor editor = getSharedPreferences("init_data", MODE_PRIVATE).edit();
        editor.putString("default_title", string);
        editor.commit();
    }

    private String getUrlFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("init_data", MODE_PRIVATE);
        return sharedPreferences.getString("default_title", "Hello");
    }


    private void showErrorDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("哎呀！");
        builder.setMessage("没搜着啊！换个名试试看");
        builder.setPositiveButton("好吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void openKeyboard(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    private void closeKeyboard(Context context, View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void startSearching() {
        String userInput = mSearchText.getText().toString();
        if (!userInput.equals("")) {
            String url = encodeInputToImgUrl(userInput);
            parseJson(queue, url);
            mTitle.setText(String.format("《%s》", userInput));
            mTitle.setVisibility(View.VISIBLE);
            mSearchText.setVisibility(View.GONE);
            saveTitleToSharedPreferences(userInput);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mSearchText.setVisibility(View.GONE);
        }
        closeKeyboard(MainActivity.this, mSearchText);

    }
}
