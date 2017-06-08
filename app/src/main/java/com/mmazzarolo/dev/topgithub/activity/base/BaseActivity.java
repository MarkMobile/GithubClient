package com.mmazzarolo.dev.topgithub.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mmazzarolo.dev.topgithub.utils.LogUtil;

import butterknife.ButterKnife;

/**
 * Created by Arison on 2017/2/20.
 * 基类 -最顶层的继承父类，尽量无侵入式设计
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static  String TAG = "BaseActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        LogUtil.d(TAG,"onCreate()");
        initView();
        initEvent();
        initData();
    }
    
    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * add Listener
     */
    protected abstract void initEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();
    
    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(TAG,"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(TAG,"onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d(TAG,"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(TAG,"onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG,"onDestroy()");
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        LogUtil.d(TAG,"onContentChanged()");
    }


    protected abstract int getLayoutResourceId();


}
