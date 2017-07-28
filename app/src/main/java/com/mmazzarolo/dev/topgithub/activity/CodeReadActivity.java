package com.mmazzarolo.dev.topgithub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.mmazzarolo.dev.topgithub.Navigator;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.activity.base.BaseActivity;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.model.DirectoryNode;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.widget.drawer.DrawerLayout;
import com.mmazzarolo.dev.topgithub.widget.tree.DirectoryNavDelegate;

import butterknife.BindView;
 
 /**
   * @desc:源码阅读
   * @author:Arison 2017/7/26
   */
public class CodeReadActivity extends BaseActivity implements DirectoryNavDelegate.FileClickListener, DirectoryNavDelegate.LoadFileCallback {

    @BindView(R.id.directory_view)
    RecyclerView mDirectoryRecyclerView;//源码目录
    @BindView(R.id.left_sheet)
    View mLeftSheet;
    @BindView(R.id.container_code_read)
    FrameLayout mContainer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private DirectoryNode mDirectoryNode;
    private DirectoryNode mSelectedNode;
     private DirectoryNavDelegate mDirectoryNavDelegate;
     
    @Override
    protected void initView() {


        mDirectoryNavDelegate = new DirectoryNavDelegate(mDirectoryRecyclerView, this);
        mDirectoryNavDelegate.setLoadFileCallback(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    /**
      * @desc:解析参数
      */
    private void parseIntent(Bundle savedInstanceState) {
        if (savedInstanceState!=null){
            
            
            return;
        }
        Intent intent = getIntent();
        Repo repo = (Repo) intent.getSerializableExtra(Navigator.EXTRA_REPO);
        if (repo!=null){
            String openFilePath = intent.getData().getPath();
            String[] mids = openFilePath.split("/");
            String name = mids[mids.length - 1];
            repo = new Repo(name, openFilePath, false);
            repo.id = String.valueOf( RepoDao.getInstance().insertRepo(repo));
        }
        RepoDao.getInstance().updateRepoLastModify(Long.valueOf(repo.id)
                , System.currentTimeMillis());
        mDirectoryNode=repo.toDirectoryNode();


        mDirectoryNavDelegate.updateData(mDirectoryNode);
    }
    
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_code_read;
    }

     @Override
     public void doOpenFile(DirectoryNode node) {
         
     }

     @Override
     public void onFileOpenStart() {

     }

     @Override
     public void onFileOpenEnd() {

     }
 }
