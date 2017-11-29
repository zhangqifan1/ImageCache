package a16.erin.unit09_imageloader;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by mamiaomiao on 17/8/9.
 */

public class MemerryCache {

    private Context context;
    //硬引用
    private LruCache<String, Bitmap> lruCache;
    //软引用
    private LinkedHashMap<String, SoftReference<Bitmap>> softReferenceLinkedHashMap;
    //软引用占用内存
    private static final int CACHESIZE = 15;

    //构造方法
    public MemerryCache(Context context) {
        this.context = context;
        //内存管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //给硬引用分配1/4的内存
       int memsize= manager.getMemoryClass()/4;
        //实例化硬引用
        lruCache=new LruCache<String, Bitmap>(memsize){

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                    //回收资源时调用；硬引用里的图片被移除以后，放到软引用中
                    softReferenceLinkedHashMap.put(key,new SoftReference<Bitmap>(oldValue));
                }



            @Override
            protected int sizeOf(String key, Bitmap value) {
                if(value!=null){
                    //一张图片的大小
                    return value.getHeight()*value.getRowBytes();
                }
                return 0;
            }
        };
        //软引用实例化，默认给16个内存；第二个参数0.75f负载因子，等到添加到极限的时候，根据负载因子来扩容；
        softReferenceLinkedHashMap=new LinkedHashMap<String, SoftReference<Bitmap>>(CACHESIZE,0.75f,false){
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                return super.removeEldestEntry(eldest);
            }
        };

    }

    //将从网络获取的bitmap放入内存缓存中
    public void putBitmapToMemery(String url,Bitmap bitmap){
        if(bitmap!=null){
            synchronized (bitmap){
                lruCache.put(url,bitmap);
            }
        }
    }
//通过URL从内存缓存中取出图片
public Bitmap getImgFromMemery(String url){
    if(url==null||url.equals("")){
        return  null;
    }
    synchronized (lruCache){
        //从硬引用中取出bitmap
        Bitmap bitmap=lruCache.get(url);
        if(bitmap!=null){
            //命中硬引用中的图片，将资源放到队列开始；
            lruCache.remove(url);
            lruCache.put(url,bitmap);
            return  bitmap;
        }else {
            lruCache.remove(url);
        }
    }
    synchronized (softReferenceLinkedHashMap){
        //从软引用中取出缓存图片
        SoftReference<Bitmap> softReference=softReferenceLinkedHashMap.get(url);
        if(softReference!=null){
            Bitmap bitmap=softReference.get();
            if(bitmap!=null){
                lruCache.put(url,bitmap);
                return bitmap;
            }else{
                softReferenceLinkedHashMap.remove(url);
            }
        }
    }
    return  null;
}
}
