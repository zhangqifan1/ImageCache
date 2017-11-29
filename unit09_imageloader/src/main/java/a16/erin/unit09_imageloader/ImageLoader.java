package a16.erin.unit09_imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by mamiaomiao on 17/8/9.
 */

public class ImageLoader {
    private GetImgFromHttp http;
    private MemerryCache memerryCache;
    private GetImgFromFile fileCache;
    private static ImageLoader instance;

    //单例模式，构造方法私有化；
    private ImageLoader(Context context) {
        http = new GetImgFromHttp();
        memerryCache = new MemerryCache(context);
        fileCache = new GetImgFromFile();

    }

    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoader(context);
        }
        return instance;
    }

    public void setImg(String url, ImageView img) {
        //首先从内存缓存中取图片
        Bitmap bitmap = memerryCache.getImgFromMemery(url);
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
        } else {//内存缓存中没有图片
            bitmap = fileCache.getImage(url);
            if (bitmap != null) {//从文件缓存中查找
                img.setImageBitmap(bitmap);
                //放入内存缓存中
                memerryCache.putBitmapToMemery(url, bitmap);
            } else {//如果文件缓存还没有，直接从网络获取图片
                http.addTask(url, img,memerryCache,fileCache);
            }

        }

    }
}
