package com.mmazzarolo.dev.topgithub.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.ViewAnimator;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.activity.base.BaseViewActivity;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.utils.LogUtil;
import com.mmazzarolo.dev.topgithub.widget.loader.ILoadHelper;

import java.util.List;

import butterknife.BindView;

/**
  * @desc:离线下载界面
  * @author：Arison on 2016/12/30
  */
public class ProjectCacheActivity extends BaseViewActivity {
    
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1000;
    @BindView(R.id.view_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.animator_recycler_content)
    ViewAnimator mAnimatorRecyclerContent;
    @BindView(R.id.fab_main)
    FloatingActionButton mFabMain;

    private ILoadHelper mRecyclerLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate()");
        initCheckSelfPermission();//检查权限
        
        RepoDao repoDao=new RepoDao();//创建数据操作DAO
        List<Repo> repos=repoDao.readRepos();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.d("onPostCreate()");
        
        initView();
        
//        registerSubscription(RxBus.getInstance().toObservable()
//                .filter(o -> o instanceof DownloadFailDeleteEvent)
//                .map(o -> ((DownloadFailDeleteEvent)o).deleteRepo)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext()
//                .subscribe()
//        );
    }

    private void initView() {
        
    }

    private void initCheckSelfPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_project_download;
    }

    @Override
    protected void onTryAgainClick() {

    }

}
