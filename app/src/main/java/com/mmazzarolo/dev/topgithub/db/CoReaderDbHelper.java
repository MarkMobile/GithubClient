package com.mmazzarolo.dev.topgithub.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mmazzarolo.dev.topgithub.utils.LogUtil;


/**
 * Created by Arison on 2016/12/30.
 */
public class CoReaderDbHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "coreader.db";
    private static final int DATABASE_VERSION = 1;
    private static volatile CoReaderDbHelper sInstance = null;

    private CoReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
   
    public static CoReaderDbHelper getInstance(Context context) {
        CoReaderDbHelper inst = sInstance;
        if (inst == null) {
            synchronized (CoReaderDbHelper.class) {
                inst = sInstance;
                if (inst == null) {
                    inst = new CoReaderDbHelper(context);
                    sInstance = inst;
                }
            }
        }
        return inst;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d("sqlite oncreate()");
        db.execSQL(DbRepoModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;

        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + DbRepoModel.TABLE_NAME);
            onCreate(db);
        }
    }
}
