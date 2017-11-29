package a16.erin.unit09_imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mamiaomiao on 17/8/9.
 */

public class GetImgFromHttp {
    //管理线程池
    private ExecutorService executorService;
    private MyHandler handler;
    private MyTask task;
    private MemerryCache memerryCache;
    private GetImgFromFile filecache;
    //构造方法
    public GetImgFromHttp(){
        //获取CPU数量
        int cpuNum=Runtime.getRuntime().availableProcessors();

        //实例化线程池管理类
        executorService= Executors.newFixedThreadPool(cpuNum+1);
    }
    public void addTask(String url,ImageView img,MemerryCache memerryCache,GetImgFromFile filecache){

        handler=new MyHandler(img);
        task=new MyTask(url,handler);
        executorService.submit(task);
        this.memerryCache=memerryCache;
        this.filecache=filecache;
    }
    //hander更新UI，给imageview赋值
    class MyHandler extends Handler {
        private ImageView img;

        public MyHandler(ImageView img) {
            this.img = img;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //给imageview加载图片
            Bitmap bitmap = (Bitmap) msg.obj;
            img.setImageBitmap(bitmap);
        }
    }

    class MyTask extends  Thread{
        private String url;
        private MyHandler handler;

        public MyTask(String url, MyHandler handler) {
            this.url = url;
            this.handler = handler;
        }

        //从网络获取图片的子线程；
        @Override
        public void run() {
            super.run();
            try {
                URL myurl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
                //获取网络请求状态吗
                int code = connection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if(bitmap!=null){
                        memerryCache.putBitmapToMemery(url,bitmap);
                        filecache.putImage(url,bitmap);
                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
