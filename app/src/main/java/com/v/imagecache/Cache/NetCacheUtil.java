package com.v.imagecache.Cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/11/28.
 */

public class NetCacheUtil {
    private MemoryCacheUtil memoryCacheUtil;
    private LocalSDCacheUtil localSDCacheUtil;

    public NetCacheUtil(MemoryCacheUtil memoryCacheUtil, LocalSDCacheUtil localSDCacheUtil) {
        this.memoryCacheUtil = memoryCacheUtil;
        this.localSDCacheUtil = localSDCacheUtil;
    }

    public void getBitmapFromInternet(ImageView imageView,String url){
        new AsyncTask<Object,Void,Bitmap>(){
            private ImageView imageView;
            private String url;

            @Override
            protected Bitmap doInBackground(Object... objects) {
                //获取俩个参数
                imageView= (ImageView) objects[0];
                url= (String) objects[1];

                Bitmap bitmap = downloadBitmap(url);

                return bitmap;
            }

            /**
             * 运行在主线程
             * @param bitmap
             */
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if(bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }
                //将获取到的图片放到本地
                localSDCacheUtil.setBitmapToLocal(url,bitmap);
                //将获取到的图片放到内存
                memoryCacheUtil.setBitmapToMemory(url,bitmap);
            }
        }.execute(imageView,url);
    }


    private Bitmap downloadBitmap(String imageUrl){
        try {
            Response response = OkHttpUtils.get().url(imageUrl).build().execute();
            InputStream inputStream = response.body().byteStream();
            BitmapFactory.Options options=new BitmapFactory.Options();
            int inSampleSize=2;//将图片匡高设置为一半   压缩
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            return bitmap;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
