package com.v.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.v.imagecache.Cache.LocalSDCacheUtil;
import com.v.imagecache.Cache.MemoryCacheUtil;
import com.v.imagecache.Cache.NetCacheUtil;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MyBitmapUtils {
    private MemoryCacheUtil memoryCacheUtil=null;
    private LocalSDCacheUtil localSDCacheUtil=null;
    private NetCacheUtil netCacheUtil=null;

    public MyBitmapUtils(Context context,String uniqueName) {
        memoryCacheUtil=new MemoryCacheUtil();
        localSDCacheUtil=new LocalSDCacheUtil(context,uniqueName);
        netCacheUtil=new NetCacheUtil(memoryCacheUtil,localSDCacheUtil);
    }

    public void display(String url, ImageView imageView){
        Bitmap bitmap=null;
        //1.判断内存中是否有缓存
        bitmap = memoryCacheUtil.getBitmapFromMemory(url);//从内存中获取
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);//设置图片
            return;
        }

        //2.判断本地是否有缓存
        bitmap=localSDCacheUtil.getBitmapFromLocal(url);//从本地SD中取出
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            memoryCacheUtil.setBitmapToMemory(url,bitmap);
            return;
        }

        //从网络获取
        netCacheUtil.getBitmapFromInternet(imageView,url);


    }
}
