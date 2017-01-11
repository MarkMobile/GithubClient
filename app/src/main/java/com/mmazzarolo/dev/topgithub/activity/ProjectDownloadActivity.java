package com.mmazzarolo.dev.topgithub.activity;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.utils.LogUtil;

import java.util.List;

/**
  * @desc:离线下载界面
  * @author：Arison on 2016/12/30
  */
public class ProjectDownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RepoDao repoDao=new RepoDao();
        List<Repo> repos=repoDao.readRepos();
        LogUtil.d(JSON.toJSONString(repos));
        
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_project_download;
    }

    @Override
    protected void onTryAgainClick() {

    }

}
