package com.mmazzarolo.dev.topgithub.activity;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ProgressBar;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.widget.webview.NestedScrollWebView;

import butterknife.BindView;

/**
 * Created by Arison on 2017/1/12.
 */
public class SimpleWebActivity extends BaseActivity implements SearchView.OnQueryTextListener{

    private static final String TAG = "SimpleWebActivity";

    @BindView(R.id.web_content)
    NestedScrollWebView mWebContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progress_bar_web)
    ProgressBar mProgressBar;
    private SearchView mSearchView;

    private String mUrl;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_input, menu);
        getMenuInflater().inflate(R.menu.menu_web_save, menu);
        getMenuInflater().inflate(R.menu.menu_web_actions, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
    }
   
    
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_simple_web;
    }

    @Override
    protected void onTryAgainClick() {

    }
}
