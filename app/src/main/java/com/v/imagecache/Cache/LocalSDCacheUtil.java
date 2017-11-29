package com.v.imagecache.Cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/11/28.
 */

public class LocalSDCacheUtil {
    //首先定义文件的保存路径
    private String cachePath;

    public LocalSDCacheUtil(Context context,String uniqueName) {
        cachePath=getCacheDirString(context,uniqueName);
    }

    /**
     * 获取缓存目录的路径
     * @param context
     * @param uniqueName
     * @return
     */
    public  String getCacheDirString (Context context,String uniqueName){
        File file=null;
        //判断SD卡是够挂载
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.isExternalStorageRemovable()){
            file=new File(context.getExternalCacheDir(),uniqueName);
            System.out.println("SDcacheName------------->"+file.getName());
        }else{
            file=new File(context.getCacheDir(),uniqueName);
        }
        //创建文件夹
        if(!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * 设置Bitmap保存到本地SD
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap){
        FileOutputStream fos=null;
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(cachePath, fileName);
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                //如果文件夹不存在,则创建文件夹
                parentFile.mkdirs();
            }
            fos=new FileOutputStream(file);
            //把图片压缩到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            //释放操作
            if(fos!=null){
                try {
                    //关闭流
                    fos.close();
                    fos=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过Url获取Bitmap
     * @param url
     * @return
     */
    public Bitmap getBitmapFromLocal(String url){
        try {
            File file=new File(cachePath,MD5Encoder.encode(url));
            if(file.exists()){
                //如果文件存在 获取到
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
