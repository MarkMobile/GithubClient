package com.mmazzarolo.dev.topgithub.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mmazzarolo.dev.topgithub.Navigator;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.activity.base.BaseRxActivity;
import com.mmazzarolo.dev.topgithub.utils.DownloadUrlParser;
import com.mmazzarolo.dev.topgithub.widget.webview.NestedScrollWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Arison on 2017/1/12.
 */
public class SimpleWebActivity extends BaseRxActivity implements SearchView.OnQueryTextListener {

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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem inputView = menu.findItem(R.id.action_web_input);
        mSearchView= (SearchView) inputView.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_GO);
        mSearchView.setQueryHint(getString(R.string.web_url_input_hint));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);
        if (mUrl != null && mSearchView != null) mSearchView.setQuery(mUrl, true);
        MenuItemCompat.setOnActionExpandListener(inputView, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mSearchView.post(() -> mSearchView.setQuery(mUrl, false));
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        
        getMenuInflater().inflate(R.menu.menu_web_save, menu);
        getMenuInflater().inflate(R.menu.menu_web_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save://下载
                if (!TextUtils.isEmpty(mUrl)
                        && !DownloadUrlParser.parseGithubUrlAndDownload(SimpleWebActivity.this, mUrl)) {
                    showMessage(getString(R.string.repo_download_url_parse_error));
                }
                return true;
            case R.id.menu_action_open_by_browser://用浏览器打开
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(mUrl));
                startActivity(i);
                return true;
            case android.R.id.home:
                finish();
                return true;
            
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onSetupActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            onSetupActionBar(getSupportActionBar());
        }
        initWeb();
        parseIntent();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Navigator.EXTRA_WEB_URL);
        String htmlString = intent.getStringExtra(Navigator.EXTRA_HTML_STRING);
        if (mUrl!=null)getSupportActionBar().setTitle(mUrl.split("/")[mUrl.split("/").length-1]);
        if (mUrl == null) mUrl = intent.getDataString();
        if (htmlString != null) loadData(htmlString);
    }

    private void initWeb() {
        mWebContent.getSettings().setJavaScriptEnabled(true);
        mWebContent.getSettings().setDomStorageEnabled(true);
        mWebContent.getSettings().setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebContent.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mSearchView.setQuery(url, true);
                return true;
            }

//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                mSearchView.setQuery(String.valueOf(request.getUrl()), true);
//                return true;
//            }
        });
        mWebContent.setWebChromeClient(new WebChromeClient());
    }

    private void loadData(String htmlString) {
        mWebContent.loadData(htmlString, "text/html", "utf-8");
    }

    private void loadUrl(String webUrl) {
        mWebContent.loadUrl(webUrl);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)) {
            mUrl = query;
            loadUrl(mUrl);
            mSearchView.clearFocus();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    protected int getLayoutResourceId() {
        return R.layout.activity_simple_web;
    }
    
    /**
     * @desc:webview 内部链接返回
     * @author：Arison on 2017/2/8
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebContent.canGoBack()) {
                        mWebContent.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @desc:进度条
     * @author：Arison on 2017/2/8
     */
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() == View.GONE)
                    mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebContent.destroy();
    }
}
