package com.mmazzarolo.dev.topgithub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mmazzarolo.dev.topgithub.widget.progress.ProgressLoading;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Arison on 2017/6/9.
 */

public class BaseFragment extends Fragment {
    
    private final CompositeSubscription mAllSubscription = new CompositeSubscription();
    private ProgressLoading mProgressLoading;
    private ProgressLoading mUnBackProgressLoading;
    private boolean progressShow;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    
    protected void registerSubscription(Subscription subscription) {
        mAllSubscription.add(subscription);
    }

    protected void unregisterSubscription(Subscription subscription) {
        mAllSubscription.remove(subscription);
    }

    protected void clearSubscription() {
        mAllSubscription.clear();
    }
    
    
}
