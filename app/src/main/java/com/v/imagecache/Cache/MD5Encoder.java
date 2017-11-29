package com.v.imagecache.Cache;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/11/28.
 */

public class MD5Encoder {
    public static String encode(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuffer sb=new StringBuffer();

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] bytes = md5.digest(pwd.getBytes("UTF-8"));
        for (int i = 0; i < bytes.length; i++) {
            String s = Integer.toHexString(0xff & bytes[i]);
            if(s.length()==1){
                sb.append("0"+s);
            }else{
                sb.append(s);
            }
        }

        return sb.toString();
    }

}
