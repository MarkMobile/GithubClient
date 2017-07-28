package com.mmazzarolo.dev.topgithub.model;

import android.text.TextUtils;

import com.loopeer.directorychooser.FileNod;


 /**
   * @desc:实体类-项目地址
   * @author:Arison 2017/7/26
   */
public class Repo extends BaseModel{
    
    public String name;//项目名
    public long lastModify;//最后更新
    public String absolutePath;//绝对路径
    public String netDownloadUrl;//网络路径
    public boolean isFolder;//是否是目录
    public long downloadId;//下载id
    public float factor;
    public boolean isUnzip;//是否压缩

    public Repo() {
        
    }

    public Repo(String name, String absolutePath, boolean isFolder) {
        this.name = name;
        this.absolutePath = absolutePath;
        this.isFolder = isFolder;
    }
    public Repo(String name, String absolutePath, String netDownloadUrl, boolean isFolder, long downloadId) {
        this.name = name;
        this.absolutePath = absolutePath;
        this.netDownloadUrl = netDownloadUrl;
        this.isFolder = isFolder;
        this.downloadId = downloadId;
    }


    public static Repo parse(FileNod node) {
        Repo result = new Repo();
        result.name = node.name;
        result.absolutePath = node.absolutePath;
        result.isFolder = node.isFolder;
        return result;
    }

    public DirectoryNode toDirectoryNode() {
        DirectoryNode node = new DirectoryNode();
        node.name = name;
        node.absolutePath = absolutePath;
        node.isDirectory = isFolder;
        return node;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", lastModify=" + lastModify +
                ", absolutePath='" + absolutePath + '\'' +
                ", netDownloadUrl='" + netDownloadUrl + '\'' +
                ", isFolder=" + isFolder +
                ", downloadId=" + downloadId +
                ", factor=" + factor +
                ", isUnzip=" + isUnzip +
                '}';
    }

    public boolean isDownloading() {
        return downloadId > 0;
    }

    public boolean isNetRepo() {
        return !TextUtils.isEmpty(netDownloadUrl);
    }

    public boolean isLocalRepo() {
        return !TextUtils.isEmpty(absolutePath);
    }
  
}
