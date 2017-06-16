package com.mmazzarolo.dev.topgithub.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.widget.progress.ProgressLoading;

import butterknife.ButterKnife;
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
        ButterKnife.bind(this, view);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearSubscription();
        if (isProgressShow() && mProgressLoading != null) {
            dismissProgressLoading();
            mProgressLoading = null;
        }
    }


    public boolean isProgressShow() {
        return progressShow;
    }

    public void showProgressLoading(String message) {
        if (mProgressLoading == null) {
            mProgressLoading = new ProgressLoading(getActivity(), R.style.ProgressLoadingTheme);
            mProgressLoading.setCanceledOnTouchOutside(true);
            mProgressLoading.setOnCancelListener(dialog -> progressShow = false);
        }
        if (!TextUtils.isEmpty(message)) {
            mProgressLoading.setMessage(message);
        } else {
            mProgressLoading.setMessage(null);
        }
        progressShow = true;
        mProgressLoading.show();
    }
    

    public void dismissProgressLoading() {
        if (mProgressLoading != null && isVisible()) {
            progressShow = false;
            mProgressLoading.dismiss();
        }
    }
}
