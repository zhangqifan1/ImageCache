package a16.erin.unit09_imageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
   // private ImageLoader imageLoader;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView=(GridView)findViewById(R.id.mygrid);
        //imageLoader=ImageLoader.getInstance(MainActivity.this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        MyAdapter adapter=new MyAdapter();
        gridView.setAdapter(adapter);
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Constants.images.length;
        }

        @Override
        public Object getItem(int position) {
            return Constants.images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=View.inflate(MainActivity.this, R.layout.item,null);
            }
            final ImageView img=(ImageView)convertView.findViewById(R.id.item_img);
            String url=Constants.images[position];
           // imageLoader.setImg(url,img);
            DisplayImageOptions options=DisplayImageOptions.createSimple();
            imageLoader.loadImage(url, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
img.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
           // imageLoader.displayImage(url,img);
            return convertView;
        }
    }
}
