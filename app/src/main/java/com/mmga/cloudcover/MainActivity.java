package com.mmga.cloudcover;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
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

    private static final int MSG_LOADMORE = 0;
    private static final int MSG_REFRESH = 1;
    private static final long REFRESH_DELAY = 2000;
    MyApplication helper = MyApplication.getInstance();
    RequestQueue queue = helper.getRequestQueue();

    Gson gson;

    ArrayList<Songs> songsList = new ArrayList<Songs>();
//    ArrayList<Songs> songsListOfAll = new ArrayList<Songs>();

    private GridRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerViewAdapter mAdapter;
    private TextView mTitle;
    private EditText mSearchText;
    private ImageView searchIcon;
    private int offset =0;

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
        mRecyclerView = (GridRecyclerView) findViewById(R.id.recycler_view);
        gson = new Gson();

        String title = getUrlFromSharedPreferences();
        mTitle.setText(String.format("《%s》", title));

        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(songsList);
        mRecyclerView.setAdapter(mAdapter);



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(MSG_REFRESH, REFRESH_DELAY);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            GridLayoutManager layoutManager = ((GridLayoutManager) mRecyclerView.getLayoutManager());
            int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(MSG_LOADMORE, 0);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            }
        });
    }


    //网络请求，返回json数据并解析映射到songs,这里的offset参数用来判断是刷新页面还是加载更多，决定是否运行动画
    private ArrayList<Songs> parseJson(final String url, final int offset) {
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
//                            songsListOfAll.addAll(songsList);
//                            mAdapter.setAdapterData(songsListOfAll);
                            mAdapter.addAdapterData(songsList);
                            if (offset == 0) {
                                mRecyclerView.scheduleLayoutAnimation();
                            }

                        } else {
                            showNoResultDialog();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showNoConnectionDialog();
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
    private String encodeInputToImgUrl(String userInput,int offset) {
        try {
            String encodeStr = URLEncoder.encode(userInput, "UTF-8");
            return "http://s.music.163.com/search/get/?type=1&limit=10&offset=" + offset + "&s=" + encodeStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveTitleToSharedPreferences(String string) {
        SharedPreferences.Editor editor = getSharedPreferences("init_data", MODE_PRIVATE).edit();
        editor.putString("default_title", string);
        editor.apply();
    }

    private String getUrlFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("init_data", MODE_PRIVATE);
        return sharedPreferences.getString("default_title", "Hello");
    }


    private void showNoResultDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("哎呀！");
        builder.setMessage("搜不到啊！换个名试试看");
        builder.setPositiveButton("好吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void showNoConnectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("哎呀！");
        builder.setMessage("网路不给力啊");
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
//        songsListOfAll.clear();
        mAdapter.clearAdapterData();
        offset = 0;
        String userInput = mSearchText.getText().toString();
        if (!userInput.equals("")) {
            String url = encodeInputToImgUrl(userInput,0);
            parseJson(url,offset);
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

    @Override
    protected void onStart() {
        super.onStart();
        offset = 0;
        String url = encodeInputToImgUrl(getUrlFromSharedPreferences(),offset);
        parseJson(url,offset);

    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOADMORE:
                    offset += 10;
                    String url = encodeInputToImgUrl(getUrlFromSharedPreferences(), offset);
                    parseJson(url,offset);
                    break;
                case MSG_REFRESH:
                    offset = 0;
//                    mAdapter.setAdapterData(new ArrayList<Songs>());
//                    songsListOfAll.clear();
                    mAdapter.clearAdapterData();
                    String urlRefresh = encodeInputToImgUrl(getUrlFromSharedPreferences(), offset);
                    parseJson(urlRefresh,offset);
                    mSwipeRefreshLayout.setRefreshing(false);
            }
            return false;
        }
    });

}