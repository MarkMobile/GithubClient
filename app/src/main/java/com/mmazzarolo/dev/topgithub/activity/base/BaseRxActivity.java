package com.mmazzarolo.dev.topgithub.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

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
 * From 初始化rxjava的基类
 *      这个可以进行项目公共组件初始化的设计
 *      网络，事件总线等
 */
public abstract class BaseRxActivity extends BaseActivity{


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
    }
    
    
    @SuppressWarnings("unused")
    protected void registerSubscription(Subscription subscription) {
        mAllSubscription.add(subscription);
    }
    @SuppressWarnings("unused")
    protected void unregisterSubscription(Subscription subscription) {
        mAllSubscription.remove(subscription);
    }


    protected void showMessage(String message) {
        if (mCoordinatorContainer != null)
//            Snackbar.make(mCoordinatorContainer, message, Snackbar.LENGTH_SHORT)
//                    .setAction(R.string.snackbar_action, view -> {})
//                    .show();
    }
}
