package com.mmazzarolo.dev.topgithub.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mmazzarolo.dev.topgithub.db.DatabaseManager;
import com.mmazzarolo.dev.topgithub.db.DbRepoModel;
import com.mmazzarolo.dev.topgithub.model.DbRepo;
import com.mmazzarolo.dev.topgithub.model.Repo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arison on 2017/1/9.
 * 这个类有没有必要写成单例，还有待考证
 */
public class RepoDao {
    
    SQLiteDatabase db;
    
    public RepoDao(){
        db=  DatabaseManager.getInstance().openDatabase();
    }


    public List<Repo> readRepos() {
        List<Repo> repos = new ArrayList<>();
        Cursor cursor = db.rawQuery(DbRepo.SELECT_ALL, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Repo repo = parseRepo(cursor);
                if (repo != null) {
                    repos.add(repo);
                }
            }
        }
        return repos;
    }

    private Repo parseRepo(Cursor cursor) {
        DbRepo dbRepo = DbRepo.FOR_TEAM_MAPPER.map(cursor);
        Repo repo = new Repo();
        repo.id = String.valueOf(dbRepo._id());
        repo.name = dbRepo.name();
        repo.absolutePath = dbRepo.absolute_path();
        repo.netDownloadUrl = dbRepo.net_url();
        repo.isFolder = dbRepo.is_folder();
        repo.lastModify = dbRepo.last_modify();
        repo.downloadId = dbRepo.download_id();
        repo.factor = dbRepo.factor();
        repo.isUnzip = dbRepo.is_unzip();
        return repo;
    }

    /**
      * @desc:更新项目的下载id
      * @author：Arison on 2017/2/16
      */
    public void updateRepoDownloadId(long downloadId, String repoId) {
        db.execSQL(DbRepoModel.UPDATE_DOWNLOAD_ID, new String[]{String.valueOf(downloadId), String.valueOf(repoId)});
    }
    /**
      * @desc:删除下载的项目
      * @author：Arison on 2017/2/16
      */
    public void deleteRepo(long id) {
        db.execSQL(DbRepoModel.DELETE_REPO
                , new String[]{String.valueOf(id)});
    }
     
    /**
      * @desc:更新项目下载id
      * @author：Arison on 2017/2/21
      */
    public void updateRepoDownloadProgress(long downloadId, float factor) {
        db.execSQL(DbRepoModel.UPDATE_DOWNLOAD_PROGRESS
                , new String[]{String.valueOf(factor), String.valueOf(downloadId)});
    }

    public void updateRepoUnzipProgress(long downloadId, float factor, boolean isUnzip) {
        db.execSQL(DbRepoModel.UPDATE_UNZIP_PROGRESS
                , new String[]{String.valueOf(factor),  String.valueOf(isUnzip ? 1 : 0), String.valueOf(downloadId)});
    }

    public void resetRepoDownloadId(long downloadId) {
        db.execSQL(DbRepoModel.RESET_DOWNLOAD_ID, new String[]{String.valueOf(downloadId)});
    }

    public Repo readSameRepo(Repo repo) {
        Repo result = null;
        Cursor cursor = db.rawQuery(
                DbRepo.CHECK_SAME_REPO,
                new String[]{
                        repo.name,
                        repo.absolutePath
                });
        if (cursor.moveToFirst()) {
            result = parseRepo(cursor);
        }
        return result;
    }

    public long insertRepo(Repo repo) {
        Repo same = readSameRepo(repo);
        if (same != null) return Long.valueOf(same.id);
        if (repo.lastModify == 0) repo.lastModify = System.currentTimeMillis();
        return db.insert(DbRepoModel.TABLE_NAME, null, DbRepo.FACTORY.marshal()
                .name(repo.name)
                .absolute_path(repo.absolutePath)
                .last_modify(repo.lastModify)
                .net_url(repo.netDownloadUrl)
                .is_folder(repo.isFolder)
                .download_id(repo.downloadId)
                .factor(repo.factor)
                .is_unzip(repo.isUnzip)
                .asContentValues());
    }
}
