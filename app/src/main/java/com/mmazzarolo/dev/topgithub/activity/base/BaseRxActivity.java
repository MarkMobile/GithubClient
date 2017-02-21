package com.mmazzarolo.dev.topgithub.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.event.rx.DownloadRepoMessageEvent;
import com.mmazzarolo.dev.topgithub.event.rx.ThemeRecreateEvent;
import com.mmazzarolo.dev.topgithub.utils.RxBus;

import butterknife.BindView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Arison on 2017/2/20.
 * From rxjava的基类
 *      公共组件注册
 */
public abstract class BaseRxActivity extends BaseActivity{

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;
    @Nullable
    @BindView(R.id.view_coordinator_container)
    CoordinatorLayout mCoordinatorContainer;
    //rxjava组件
    private final CompositeSubscription mAllSubscription = new CompositeSubscription();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        
        initRxJava();
    }

    private void initRxJava() {
        registerSubscription(
                RxBus.getInstance()
                        .toObservable()
                        .filter(o -> o instanceof DownloadRepoMessageEvent)
                        .map(o -> (DownloadRepoMessageEvent) o)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(o -> showMessage(o.getMessage()))
                        .subscribe());

        registerSubscription(
                RxBus.getInstance()
                        .toObservable()
                        .filter(o -> o instanceof ThemeRecreateEvent)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(o -> recreate())
                        .subscribe());
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            onSetupActionBar(getSupportActionBar());
        }

        String title = getIntent().getStringExtra(Intent.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @SuppressWarnings("unused")
    protected void registerSubscription(Subscription subscription) {
        mAllSubscription.add(subscription);
    }
    @SuppressWarnings("unused")
    protected void unregisterSubscription(Subscription subscription) {
        mAllSubscription.remove(subscription);
    }

    protected void onSetupActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @SuppressWarnings("unused")
    protected void clearSubscription() {
        mAllSubscription.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSubscription();
    }

    protected void showMessage(String message) {
        if (mCoordinatorContainer != null) {
            Snackbar.make(mCoordinatorContainer, message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_action, view -> {})
                    .show();
        }
    }
}
