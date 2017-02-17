package com.mmazzarolo.dev.topgithub.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.mmazzarolo.dev.topgithub.Navigator;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.event.rx.DownloadRepoMessageEvent;
import com.mmazzarolo.dev.topgithub.model.RemoteRepoFetcher;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.utils.RxBus;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
  * @desc:下载github项目服务类
 *         可以拓展成下载文件的公共服务类
  * @author：Arison on 2017/2/14
  */
public class DownloadRepoService extends Service {

    public static final int DOWNLOAD_COMPLETE = 0;
    public static final int DOWNLOAD_REPO = 1;
    public static final int DOWNLOAD_PROGRESS = 2;
    public static final int DOWNLOAD_REMOVE_DOWNLOAD = 3;
    
    private static final String TAG = "DownloadRepoService";
    public static final Uri DOWNLOAD_CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    public static final String MEDIA_TYPE_ZIP = "application/zip";
    private HashMap<Long, Repo> mDownloadingRepos;
    private DownloadChangeObserver mDownloadChangeObserver;//内部类

    private Subscription mProgressSubscription;
    
    public DownloadRepoService() {
        
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadingRepos=new HashMap<>();
        mDownloadChangeObserver = new DownloadChangeObserver();
        getContentResolver().registerContentObserver(DOWNLOAD_CONTENT_URI,true,mDownloadChangeObserver);
    }

    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void parseIntent(Intent intent) { 
        Intent in=intent;
        int type=in.getIntExtra(Navigator.EXTRA_DOWNLOAD_SERVICE_TYPE,0);
        Repo repo = (Repo) in.getSerializableExtra(Navigator.EXTRA_REPO);
        long id = in.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        switch (type) {
            case DOWNLOAD_COMPLETE://下载完成
               doRepoDownloadComplete(id);
                break;
            case DOWNLOAD_REPO://下载文件
                downloadFile(repo);
                break;
            case DOWNLOAD_PROGRESS://检查下载进度
                checkDownloadProgress();
                break;
            case DOWNLOAD_REMOVE_DOWNLOAD://移除下载
                removeDownloadingRepo(id);
                break;
        }
    }

   
    private void downloadFile(Repo repo) {
        RepoDao dao=  new RepoDao();
        RemoteRepoFetcher dataFetcher=new RemoteRepoFetcher(this,repo.netDownloadUrl,repo.name);
        long downloadId=dataFetcher.download();//下载
        if(downloadId<0){
            dao.deleteRepo(Long.parseLong(repo.id));//删除项目
        }
        repo.downloadId=downloadId;
        mDownloadingRepos.put(downloadId, repo);//放入缓存
        dao.updateRepoDownloadId(downloadId, repo.id);
        //项目开始下载  
        RxBus.getInstance().send(new DownloadRepoMessageEvent(getString(R.string.repo_download_start, repo.name)));
        checkDownloadProgress();
    }
   
    /**
      * @desc:下载完成
      * @author：Arison on 2017/2/17
      */
    private void doRepoDownloadComplete(long id){
        
        
    }
    
    
    /**
      * @desc:移除下载
      * @author：Arison on 2017/2/17
      */
    private void removeDownloadingRepo(long id) {
        
        
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
   
    
    /**
      * @desc:实现接口 ContentObserver
      * @author：Arison on 2017/2/14
      */
    class  DownloadChangeObserver extends  ContentObserver{

        public DownloadChangeObserver(){
            super(new Handler());
        }
        
        @Override
        public void onChange(boolean selfChange) {
            
            checkDownloadProgress();
            
        }
    }
    
    /**
      * @desc:检查下载进度
      * @author：Arison on 2017/2/16
      */
    private void checkDownloadProgress() {
        RepoDao dao=  new RepoDao();
        if(mDownloadingRepos.isEmpty()){
            List<Repo> repos = dao.readRepos();
            for (Repo repo : repos) {
                if (repo.isDownloading()) {
                    mDownloadingRepos.put(repo.downloadId, repo);
                }
            }
        }
        
        if (mDownloadingRepos.isEmpty()){
            stopSelf();
            return;
        }
        if (mProgressSubscription != null && !mProgressSubscription.isUnsubscribed()) {
            mProgressSubscription.unsubscribe();
        }

        mProgressSubscription = checkDownloadingProgress(this);
        
    }

   public Subscription checkDownloadingProgress(Context context){
        
        return Observable.create(new Observable.OnSubscribe<List<Repo>>() {
            @Override
            public void call(Subscriber<? super List<Repo>> subscriber) {

            }
        })    .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
              .subscribe();
    }

}
