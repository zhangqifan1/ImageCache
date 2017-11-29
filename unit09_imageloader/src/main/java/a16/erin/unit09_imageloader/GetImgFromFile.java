package a16.erin.unit09_imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by mamiaomiao on 17/8/9.
 */

public class GetImgFromFile {
    private static final String folderName = "ImgCache";
    private static final String file_whole = ".cache";
    private int MB = 1024 * 1024;
    private int free = 30;

    public GetImgFromFile() {
        //初始化文件缓存时，清理缓存文件大小；
        removeCache();
    }
//当文件夹大小超出预设值时，清理文件；
    private void removeCache() {
        File dir = new File(getDir());
        //如果ImgCache存在
        if (dir != null && dir.exists()) {
            StatFs statFs = new StatFs(getDir());
            //SDK版本小于18
            int size = 0;
            if (Build.VERSION.SDK_INT < 18) {
                size = statFs.getBlockSize() * statFs.getAvailableBlocks() / MB;
            } else {
                size = (int) (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong() / MB);
            }
            if (size - 20 < free) {
                //清理缓存
                File[] files = dir.listFiles();//获取文件数组；
                if (files == null) {
                    return;
                }
                //按修改时间排序
                Arrays.sort(files, new FileCompare());
                //删除文件总数的1/4
                for (int i = 0; i < files.length / 4; i++) {
                    if (files[i] != null) {
                        files[i].delete();
                    }
                }
            }
        }
    }

    //根据文件修改地时间排序
    class FileCompare implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.lastModified() > o2.lastModified()) {
                return 1;
            } else if (o1.lastModified() < o2.lastModified()) {
                return -1;
            }
            return 0;
        }
    }

    //将bitmap放入文件缓存中，以URL命名；
    public void putImage(String url, Bitmap bitmap) {
        //缓存图片的文件路径；
        String path = getDir();
        File dir = new File(path);
        //ImgCache文件夹不存在时，创建文件夹；
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //文件缓存
        File file = new File(path + File.separator + cvtUrlToFileName(url));
        try {
            //以JPEG的格式保存入文件；
            OutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    //从本地文件中读取缓存图片
    public Bitmap getImage(String url) {
        //缓存图片的文件路径；
        String path = getDir() + File.separator + cvtUrlToFileName(url);
        File file = new File(path);
        if (file.exists()) {
            try {
                updateTime(file);
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //命中文件缓存时，更新文件修改时间；
    private void updateTime(File file) {
        //更新文件的修改时间，以当前操作时间为准；System.currentTimeMillis()获取当前时间戳；
        file.setLastModified(System.currentTimeMillis());
    }

    //将图片的网络地址转换为文件名；
    private String cvtUrlToFileName(String url) {
//        String[] names=url.split("/");
//        if(names!=null){
//            return names[names.length-1]+file_whole;
//        }
        try {
            return MD5Encoder.encode(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取缓存文件夹目录
    private String getDir() {
        return getSdPath() + File.separator + folderName;
    }

    //获取SD卡路径
    private String getSdPath() {
        //获取SD卡挂载状态
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {//正常挂载
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }

}
