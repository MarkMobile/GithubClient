package com.mmazzarolo.dev.topgithub.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mmazzarolo.dev.topgithub.db.DatabaseManager;
import com.mmazzarolo.dev.topgithub.model.DbRepo;
import com.mmazzarolo.dev.topgithub.model.Repo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arison on 2017/1/9.
 */
public class RepoDao {
    
    SQLiteDatabase db;
    
    public RepoDao(Context ct){
        db=  DatabaseManager.getInstance().openDatabase();
    }


    public List<Repo> readRepos() {
       // SQLiteDatabase db = getReadableDatabase();
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
}
