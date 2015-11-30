package com.mmga.cloudcover.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mmga.cloudcover.BaseActivity;
import com.mmga.cloudcover.R;
import com.mmga.cloudcover.model.UniformInfo;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class StarActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<UniformInfo> mDatas = new ArrayList<>();
    private StarAdapter mAdapter;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        realm = Realm.getInstance(this);
        getAllData();
//        for (int i = 0; i < 50; i++) {
//            UniformInfo testModel = new UniformInfo();
//            testModel.setName("name + " + i);
//            mDatas.add(testModel);
//        }

        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(mAdapter = new StarAdapter(mDatas,StarActivity.this));

    }

    private void getAllData() {
        RealmQuery<UniformInfo> query = realm.where(UniformInfo.class);
        RealmResults<UniformInfo> results = query.findAll();
        mDatas.addAll(results);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}