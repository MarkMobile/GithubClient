package com.mmazzarolo.dev.topgithub.model;

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
}
