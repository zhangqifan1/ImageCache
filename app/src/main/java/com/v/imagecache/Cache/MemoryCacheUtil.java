package com.v.imagecache.Cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MemoryCacheUtil {
    private LruCache<String,Bitmap> mLruCache;
    public MemoryCacheUtil() {
        //获取最大的可用内存
        long maxMemory = Runtime.getRuntime().maxMemory()/8;

        mLruCache=new LruCache<String, Bitmap>((int) maxMemory){

            @Override
            protected int sizeOf(String key, Bitmap value) {
                //重写此方法来衡量每张图片的大小 默认返回图片数量
                int byteCount = value.getByteCount();
                return byteCount;
            }
        };

    }

    /**
     * 通过url从内存中获取图片
     * @param url
     */
    public Bitmap getBitmapFromMemory(String url){
        Bitmap bitmap = mLruCache.get(url);
        return bitmap;
    }

    /**
     * 设置Bitmap到内存
     * @param url
     * @param bitmap
     */
    public  void setBitmapToMemory(String url,Bitmap bitmap){
        //集合中没有 那就放进去
        if(getBitmapFromMemory(url)==null){
            mLruCache.put(url,bitmap);
        }
    }

    /**
     * 从缓存中删除指定的Key
     * @param key
     */
    public void removeBitmapFromMemory(String key){
        mLruCache.remove(key);
    }
}
