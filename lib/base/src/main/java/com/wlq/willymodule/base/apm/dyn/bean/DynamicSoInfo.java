package com.wlq.willymodule.base.apm.dyn.bean;

import android.text.TextUtils;

import com.wlq.willymodule.base.apm.dyn.annotation.AbiInfo;

public class DynamicSoInfo {

    /**
     * 下载地址
     */
    public String url;

    /**
     * so资源来源于网络
     */
    public boolean fromRemote;

    /**
     * 架构类型
     */
    public String abi = AbiInfo.ARM32;

    /**
     * 纯so名
     * 去掉lib前缀，和.so后缀
     * ex: ffmpeg
     */
    public String name;

    /**
     * so文件全名
     * ex: libffmpeg.so
     */
    public String libName;

    /**
     * so文件哈希值，用于校验so是否正确
     */
    public String hash;

    /**
     * so下载解压后的路径
     * 注意：path不参与hashcode、equal校验
     */
    public String path;

    /**
     * so压缩文件大小，用于校验zip正确性
     */
    public long size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicSoInfo that = (DynamicSoInfo) o;
        return size == that.size &&
                TextUtils.equals(url, that.url) &&
                TextUtils.equals(abi, that.abi) &&
                TextUtils.equals(name, that.name) &&
                TextUtils.equals(libName, that.libName) &&
                TextUtils.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return (url + abi + name + libName + hash + size).hashCode();
    }

    @Override
    public String toString() {
        return "DynamicSoInfo{" +
                "url='" + url + '\'' +
                ", abi='" + abi + '\'' +
                ", name='" + name + '\'' +
                ", libName='" + libName + '\'' +
                ", hash='" + hash + '\'' +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }
}
