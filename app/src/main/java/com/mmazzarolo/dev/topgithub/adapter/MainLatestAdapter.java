package com.mmazzarolo.dev.topgithub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.model.MainHeaderItem;
import com.mmazzarolo.dev.topgithub.model.Repo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

        @BindView(R.id.grid_main)
        GridView mGridView;
        private MainHeaderAdapter mMainHeaderAdapter;
        
        public MainHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mMainHeaderAdapter = new MainHeaderAdapter(itemView.getContext());
            mGridView.setAdapter(mMainHeaderAdapter);
        }

        public void bind() {
            List<MainHeaderItem> items = new ArrayList<>();
            items.add(new MainHeaderItem(R.drawable.ic_github, R.string.header_item_github_search
                    , itemView.getContext().getString(R.string.header_item_github_search_link)));
            items.add(new MainHeaderItem(R.drawable.ic_trending, R.string.header_item_trending
                    , itemView.getContext().getString(R.string.header_item_trending_link)));
            mMainHeaderAdapter.updateData(items);
        }
        
    }
    
    
    public void clearSubscription() {
        mAllSubscription.clear();
    }
}
