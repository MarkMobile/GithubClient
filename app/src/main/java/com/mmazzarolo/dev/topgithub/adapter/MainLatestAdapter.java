package com.mmazzarolo.dev.topgithub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopeer.itemtouchhelperextension.Extension;
import com.mmazzarolo.dev.topgithub.R;
import com.mmazzarolo.dev.topgithub.model.MainHeaderItem;
import com.mmazzarolo.dev.topgithub.model.Repo;
import com.mmazzarolo.dev.topgithub.widget.view.ForegroundProgressRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
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
    public int getItemViewType(int position) {
        if (position == 0) return R.layout.list_item_main_top_header;
        return R.layout.list_item_repo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        View view;
        switch (viewType) {
            case R.layout.list_item_main_top_header:
                view = inflater.inflate(R.layout.list_item_main_top_header, parent, false);
                return new MainHeaderHolder(view);
            default:
                view = inflater.inflate(R.layout.list_item_repo, parent, false);
                return new RepoViewHolder(view);
        }
    }

   
    /**
      * @desc:离线列表
      * @author：Arison on 2017/2/24
      */
    public class RepoViewHolder extends RecyclerView.ViewHolder implements Extension {

        @BindView(R.id.img_repo_type)
        ImageView mImgRepoType;
        @BindView(R.id.text_repo_name)
        TextView mTextRepoName;
        @BindView(R.id.text_repo_time)
        TextView mTextRepoTime;
        @BindView(R.id.view_progress_list_repo)
        ForegroundProgressRelativeLayout mProgressRelativeLayout;
        @BindView(R.id.view_list_repo_action_delete)
        View mActionDeleteView;
        @BindView(R.id.view_list_repo_action_update)
        View mActionSyncView;
        @BindView(R.id.view_list_repo_action_container)
        View mActionContainer;
        @BindView(R.id.img_list_repo_cloud)
        View mCloud;
        @BindView(R.id.img_list_repo_phone)
        View mLocalPhone;

        Subscription mSubscription;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public float getActionWidth() {
            return 0;
        }

        public Subscription bind(Repo repo) {
            
            
            return mSubscription;
        }

        private void resetSubscription(Repo repo) {
            
        }
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
