package com.mmazzarolo.dev.topgithub.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.mmazzarolo.dev.topgithub.Navigator;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.db.dao.RepoDao;
import com.mmazzarolo.dev.topgithub.event.rx.DownloadFailDeleteEvent;
import com.mmazzarolo.dev.topgithub.event.rx.DownloadProgressEvent;
import com.mmazzarolo.dev.topgithub.event.rx.DownloadRepoMessageEvent;
import com.mmazzarolo.dev.topgithub.model.RemoteRepoFetcher;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.utils.FileCache;
import com.mmazzarolo.dev.topgithub.utils.LogUtil;
import com.mmazzarolo.dev.topgithub.utils.RxBus;
import com.mmazzarolo.dev.topgithub.utils.Unzip;

import java.io.File;
import java.util.ArrayList;
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
                LogUtil.d("下载完成");
                break;
            case DOWNLOAD_REPO://下载文件
                downloadFile(repo);
                LogUtil.d("下载文件 开始");
                break;
            case DOWNLOAD_PROGRESS://检查下载进度
                checkDownloadProgress();
                LogUtil.d("检查下载进度");
                break;
            case DOWNLOAD_REMOVE_DOWNLOAD://移除下载
                removeDownloadingRepo(id);
                LogUtil.d("移除下载");
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
        new RepoDao().updateRepoUnzipProgress(id, 1, true);
        RxBus.getInstance().send(new DownloadProgressEvent(id, true));

        Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
            Cursor cursor = null;
            try {
                DownloadManager manager =
                        (DownloadManager) DownloadRepoService.this.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Query baseQuery = new DownloadManager.Query()
                        .setFilterById(id);
                cursor = manager.query(baseQuery);

                final int statusColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);
                final int localFilenameColumnId = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_FILENAME);
                final int descName = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_DESCRIPTION);

                if (cursor.moveToFirst()) {
                    final long status = cursor.getLong(statusColumnId);
                    final String path = cursor.getString(localFilenameColumnId);
                    final String name = cursor.getString(descName);
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //下载成功之后的步骤
                        File zipFile = new File(path);
                        FileCache fileCache = FileCache.getInstance();
                        fileCache.deleteFilesByDirectory(new File(fileCache.getCacheDir().getPath() + File.separator + name));
                        //获取decomp对象
                        Unzip decomp = new Unzip(zipFile.getPath()
                                , fileCache.getCacheDir().getPath() + File.separator + name, getApplicationContext());
                        decomp.DecompressZip();
                        
                        if (zipFile.exists()) zipFile.delete();
                        new RepoDao().updateRepoUnzipProgress(id, 1, false);
                        new RepoDao().resetRepoDownloadId(mDownloadingRepos.get(id).downloadId);
                        RxBus.getInstance().send(new DownloadProgressEvent(id, false));
                        RxBus.getInstance().send(new DownloadRepoMessageEvent(
                                getString(R.string.repo_download_complete, mDownloadingRepos.get(id).name)));
                    }
                }
                mDownloadingRepos.remove(id);
                subscriber.onCompleted();

            } catch (Exception e) {
                subscriber.onError(e);
            } finally {
                cursor.close();
            }

            }).onErrorResumeNext(Observable.empty())
                    .subscribeOn(Schedulers.io())
                    .doOnError(e -> Log.d(TAG, e.toString()))
                    .doOnCompleted(this::checkTaskEmptyToFinish)
                    .subscribe();
        }


    /**
     * @desc:移除下载
     * @author：Arison on 2017/2/17
     */
    private void removeDownloadingRepo(long id) {
        DownloadManager downloadManager =
                (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
        int i = downloadManager.remove(id);
        if (i > 0) mDownloadingRepos.remove(id);
        
    }



    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearDownloadProgressSubscription();
        getContentResolver().unregisterContentObserver(mDownloadChangeObserver);
    }

    private void clearDownloadProgressSubscription() {
        if (mProgressSubscription != null && !mProgressSubscription.isUnsubscribed()) {
            mProgressSubscription.unsubscribe();
        }
    }

    private void checkTaskEmptyToFinish() {
        if (mDownloadingRepos.isEmpty()) {
            stopSelf();
        }
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
                DownloadManager downloadManager =
                        (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                List<Repo> repos = new ArrayList(mDownloadingRepos.values());//缓存中的下载任务
                for (Repo repo:repos){
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(repo.downloadId);//根据id来查询
                    
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();//Move the cursor to the first row.

                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(
                            cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    String mediaType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                    int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status == DownloadManager.STATUS_FAILED
                            && !MEDIA_TYPE_ZIP.equals(mediaType)
                            && reason == DownloadManager.ERROR_UNKNOWN) {
                        RxBus.getInstance()
                                .send(new DownloadRepoMessageEvent(
                                        getString(R.string.repo_download_fail, repo.name)));
                        RxBus.getInstance()
                                .send(new DownloadFailDeleteEvent(repo));
                        new RepoDao().deleteRepo(Long.parseLong(repo.id)); //删除
                    } else if (status != DownloadManager.STATUS_SUCCESSFUL) {
                        final float dl_progress = 1f * bytes_downloaded / bytes_total;
                        repo.factor = dl_progress;
                    } else if (status == DownloadManager.STATUS_SUCCESSFUL){
                        repo.factor = 1;
                    }

                    if (repo.factor < 0) repo.factor = 0;
                    if (repo.factor>=0){
                        new RepoDao().updateRepoDownloadProgress(repo.downloadId, repo.factor);
                        RxBus.getInstance().send(new DownloadProgressEvent(repo.id,
                                repo.downloadId, repo.factor, repo.isUnzip));
                    }
                    
                    cursor.close();
                    
                }
                subscriber.onCompleted();
            }
        })    .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe();
    }

}
