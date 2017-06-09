package com.mmazzarolo.dev.topgithub.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.activity.base.BaseActivity;
import com.mmazzarolo.dev.topgithub.widget.drawer.DrawerLayout;

import butterknife.BindView;

/**
 * Created by Arison on 2017/6/9.
 */
public class CodeReadActivity extends BaseActivity {

    @BindView(R.id.directory_view)
    RecyclerView mDirectoryRecyclerView;
    @BindView(R.id.left_sheet)
    View mLeftSheet;
    @BindView(R.id.container_code_read)
    FrameLayout mContainer;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    
    @Override
    protected void initView() {
        
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_code_read;
    }
}
