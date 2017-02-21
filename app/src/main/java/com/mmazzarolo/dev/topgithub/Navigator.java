package com.mmazzarolo.dev.topgithub;

import android.content.Context;
import android.content.Intent;

import com.mmazzarolo.dev.topgithub.activity.SimpleWebActivity;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.service.DownloadRepoService;

/**
 * Created by Arison on 2017/2/8.
 * app 常量以及界面调转公共方法
 */
public class Navigator {

    public final static String EXTRA_WEB_URL = "extra_web_url";
    public final static String EXTRA_HTML_STRING = "extra_html_string";
    //下载
    public final static String EXTRA_REPO = "extra_repo";
    public final static String EXTRA_DOWNLOAD_SERVICE_TYPE = "extra_download_service_type";

    public static void startWebActivity(Context context, String url) {
        Intent intent = new Intent(context, SimpleWebActivity.class);
        intent.putExtra(EXTRA_WEB_URL, url);
        context.startActivity(intent);
    }

    
    /**
      * @desc:启动下载服务类
      * @author：Arison on 2017/2/21
      */
    public static void startDownloadNewRepoService(Context context, Repo repo) {
        Repo sameRepo = new RepoDao().readSameRepo(repo);
        long repoId;
        if (sameRepo != null) {
            repoId = Long.parseLong(sameRepo.id);
        } else {
            repoId = new RepoDao().insertRepo(repo);
        }
        repo.id = String.valueOf(repoId);
        Navigator.startDownloadRepoService(context, repo);
    }

    public static void startDownloadRepoService(Context context, Repo repo) {
        Intent intent = new Intent(context, DownloadRepoService.class);
        intent.putExtra(EXTRA_REPO, repo);
        intent.putExtra(EXTRA_DOWNLOAD_SERVICE_TYPE, DownloadRepoService.DOWNLOAD_REPO);
        context.startService(intent);
    }
}
