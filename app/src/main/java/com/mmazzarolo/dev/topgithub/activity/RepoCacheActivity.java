package com.mmazzarolo.dev.topgithub.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ViewAnimator;

import com.loopeer.directorychooser.FileNod;
import com.loopeer.directorychooser.NavigatorChooser;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.activity.base.BaseActivity;
import com.mmazzarolo.dev.topgithub.adapter.ItemTouchHelperCallback;
import com.mmazzarolo.dev.topgithub.adapter.MainLatestAdapter;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.widget.loader.ILoadHelper;
import com.mmazzarolo.dev.topgithub.widget.loader.RecyclerLoader;
import com.mmazzarolo.dev.topgithub.widget.recycleview.decoration.DividerItemDecoration;
import com.mmazzarolo.dev.topgithub.widget.recycleview.decoration.DividerItemDecorationMainList;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @desc:离线界面
 * @author：Arison on 2016/12/30
 */
public class RepoCacheActivity extends BaseActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1000;
    @BindView(R.id.view_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.animator_recycler_content)
    ViewAnimator mAnimatorRecyclerContent;
    @BindView(R.id.fab_main)
    FloatingActionButton mFabMain;

    private ILoadHelper mRecyclerLoader;
    private MainLatestAdapter mMainLatestAdapter;

    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;

     @OnClick(R.id.fab_main)
     public void doSelectFile(){
         NavigatorChooser.startDirectoryFileChooserActivity(this);
     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult()");
        switch (requestCode) {
            case NavigatorChooser.DIRECTORY_FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    FileNod node = (FileNod) data.getSerializableExtra(NavigatorChooser.EXTRA_FILE_NODE);
                    Repo repo = Repo.parse(node);
                    repo.id = String.valueOf(new RepoDao().insertRepo(repo));
//                    Navigator.startCodeReadActivity(MainActivity.this, repo);
                }
                break;
        }
    }

    protected void initView() {
        initCheckSelfPermission();//检查权限
        mRecyclerLoader = new RecyclerLoader(mAnimatorRecyclerContent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMainLatestAdapter = new MainLatestAdapter(this);
        mRecyclerView.setAdapter(mMainLatestAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecorationMainList(this,
                  DividerItemDecoration.VERTICAL_LIST
                , getResources().getDimensionPixelSize(R.dimen.repo_list_divider_start)
                , -1
                , -1));
        mItemTouchHelper = createItemTouchHelper();
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume()");
        loadLocalData();
    }

    private void loadLocalData() {
        RepoDao repoDao = new RepoDao();
        List<Repo> repos = repoDao.readRepos();
        setUpContent(repos);
    }

    private void setUpContent(List<Repo> repos) {
        mRecyclerLoader.showContent();
        mMainLatestAdapter.updateData(repos);
    }
    
    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        initView();
//        registerSubscription(RxBus.getInstance().toObservable()
//                .filter(o -> o instanceof DownloadFailDeleteEvent)
//                .map(o -> ((DownloadFailDeleteEvent) o).deleteRepo)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(mMainLatestAdapter::deleteRepo)
//                .subscribe()
//        );
    }

    public ItemTouchHelperExtension createItemTouchHelper() {
        mCallback = createCallback();
        return new ItemTouchHelperExtension(mCallback);
    }

    public ItemTouchHelperExtension.Callback createCallback() {
        return new ItemTouchHelperCallback();
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


}
