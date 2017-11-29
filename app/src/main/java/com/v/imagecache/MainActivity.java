package com.v.imagecache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private String url = "http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg";
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        MyBitmapUtils zqf = new MyBitmapUtils(this, "zqf");
        zqf.display(url, image);
    }

    private void initView() {
        image = (ImageView) findViewById(R.id.image);
    }
}
