package com.mmazzarolo.dev.topgithub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mmazzarolo.dev.topgithub.model.Repo;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Arison on 2017/2/23.
 * 离线项目列表适配器
 */                                      
public class MainLatestAdapter extends RecyclerViewAdapter<Repo> {

    private static final String TAG = "MainLatestAdapter";
    
    //rxbus
    private final CompositeSubscription mAllSubscription = new CompositeSubscription();
    
    public MainLatestAdapter(Context context) {
        super(context);
    }

    @Override
    public void setData(List<Repo> data) {
        ArrayList list = new ArrayList();
        list.add(null);
        list.addAll(data);
        super.setData(list);
    }
    
    @Override
    public void bindView(Repo var1, int var2, RecyclerView.ViewHolder var3) {
        
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    
    /**
      * @desc:类型1-头布局
      * @author：Arison on 2017/2/23
      */
    class MainHeaderHolder extends RecyclerView.ViewHolder{


        public MainHeaderHolder(View itemView) {
            super(itemView);
        } 
        
    }
    
    
    public void clearSubscription() {
        mAllSubscription.clear();
    }
}
