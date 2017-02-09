package com.mmazzarolo.dev.topgithub.model;

import com.loopeer.directorychooser.FileNod;

/**
 * Created by Arison on 2017/1/9.
 * 离线下载repo
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
}
