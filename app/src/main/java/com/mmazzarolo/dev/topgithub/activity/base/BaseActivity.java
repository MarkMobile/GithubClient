package com.mmazzarolo.dev.topgithub.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mmazzarolo.dev.topgithub.utils.LogUtil;

/**
 * Created by Arison on 2017/2/20.
 * 基类 -最顶层的继承父类，尽量无侵入式设计
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        LogUtil.d();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.d();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        LogUtil.d();
    }


    protected abstract int getLayoutResourceId();


}
