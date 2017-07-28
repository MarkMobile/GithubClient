package com.mmazzarolo.dev.topgithub.widget.tree;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.mmazzarolo.dev.topgithub.adapter.DirectoryAdapter;
import com.mmazzarolo.dev.topgithub.model.DirectoryNode;
import com.mmazzarolo.dev.topgithub.utils.FileCache;
import com.mmazzarolo.dev.topgithub.utils.FileTypeUtils;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
   * @desc:树形展示
   * @author:Arison 2017/7/26
   */
public class DirectoryNavDelegate {
    
     private static final String TAG = "DirectoryNavDelegate";
     private RecyclerView mRecyclerView;
     private DirectoryAdapter mDirectoryAdapter;
     private Context mContext;
     private FileClickListener mFileClickListener;
     private LoadFileCallback mLoadFileCallback;
     private final CompositeSubscription mAllSubscription = new CompositeSubscription();

    public interface FileClickListener {
        void doOpenFile(DirectoryNode node);
    }

    public interface LoadFileCallback{
        void onFileOpenStart();
        void onFileOpenEnd();
    }

    public DirectoryNavDelegate(RecyclerView recyclerView, FileClickListener listener) {
        mRecyclerView = recyclerView;
        mContext = recyclerView.getContext();
        mFileClickListener = listener;
        mDirectoryAdapter = new DirectoryAdapter(recyclerView.getContext(), listener);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mDirectoryAdapter);
    }

    public void updateData(DirectoryNode directoryNode) {
        mLoadFileCallback.onFileOpenStart();
        mAllSubscription.add(
                Observable.fromCallable(()->{
                    DirectoryNode node;
                    if (directoryNode.isDirectory) {
                        node = FileCache.getFileDirectory(new File(directoryNode.absolutePath));
                    } else {
                        node = directoryNode;
                    }
                    return node;
                    
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(mDirectoryAdapter::setNodeRoot)
                .doOnNext(this::checkOpenFirstFile)
                .doOnError(e -> Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show())
                .onErrorResumeNext(Observable.empty())
                .doOnCompleted(() -> mLoadFileCallback.onFileOpenEnd())
                .subscribe());
        
    }

    private void checkOpenFirstFile(DirectoryNode node) {
        if (node.isDirectory && node.pathNodes != null) {
            boolean haveOpen = false;
            for (DirectoryNode n : node.pathNodes) {
                if (FileTypeUtils.isMdFileType(n.name) && n.name.equalsIgnoreCase("readme.md")) {
                    mFileClickListener.doOpenFile(n);
                    haveOpen = true;
                }
            }
            if (!haveOpen) {
                mFileClickListener.doOpenFile(null);
            }
        } else if (!node.isDirectory) {
            mFileClickListener.doOpenFile(node);
        } else {
            mFileClickListener.doOpenFile(null);
        }
    }
    
    public void setLoadFileCallback(LoadFileCallback loadFileCallback) {
        mLoadFileCallback = loadFileCallback;
    }

    public void clearSubscription() {
        mAllSubscription.clear();
    }

    public void resumeDirectoryState(DirectoryNode node) {
        mDirectoryAdapter.setNodeRoot(node);
    }

    public DirectoryNode getDirectoryNodeInstance() {
        return mDirectoryAdapter.getNodeRoot();
    }
}
