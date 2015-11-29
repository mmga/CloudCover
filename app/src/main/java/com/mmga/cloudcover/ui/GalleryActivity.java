//package com.mmga.cloudcover.ui;
//
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import com.mmga.cloudcover.R;
//import com.mmga.cloudcover.model.TestModel;
//import com.mmga.cloudcover.model.UniformInfo;
//
//import java.util.ArrayList;
//
//import io.realm.Realm;
//import io.realm.RealmResults;
//
//public class GalleryActivity extends BaseActivity {
//
//    private ArrayList<String> infoList = new ArrayList<>();
//    GalleryAdapter mAdapter;
//    RecyclerView mRecyclerView;
//    private Realm realm;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gallery);
//
////        realm = Realm.getDefaultInstance();
//
//
//        for (int i = 0; i < 5; i++) {
//            ;
//
//            infoList.add(""+i);
//        }
//
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.gallery_recyclerview);
//        mRecyclerView = new RecyclerView(this);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter);
//
////        final RealmResults<UniformInfo> uniformInfos = realm.where(UniformInfo.class).findAll();
////        uniformInfos.addChangeListener(new RealmChangeListener() {
////            @Override
////            public void onChange() {
////                mAdapter.setAdapterData(uniformInfos);
////            }
////        });
//    }
//
//
//    private void loadData() {
//
//        RealmResults<UniformInfo> uniformInfos = realm.where(UniformInfo.class).findAll();
//
////        mAdapter.setAdapterData(uniformInfos);
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        realm.close();
//    }
//}
