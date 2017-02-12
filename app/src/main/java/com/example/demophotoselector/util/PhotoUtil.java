package com.example.demophotoselector.util;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**工具类
 * Created by clement on 17/2/11.
 */

public class PhotoUtil {
    /**获取文件夹下，所有图片的数量
     * @param parentFile
     * @return
     */
    public static int getPhotoCountFromFolder(@NonNull File parentFile){
        int picSize = parentFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                if(fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith("jpeg"))
                    return true;
                return false;
            }
        }).length;
        return picSize;
    }

    /**获取文件夹下图片的路径列表
     * @param parentFile
     * @return
     */
    public static List<String> getPhotoPathsFromFolder(@NonNull File parentFile){
        String parentFilePath = parentFile.getAbsolutePath();
        //去除非图片文件
        String[] photoNames = parentFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                if(fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith("jpeg"))
                    return true;
                return false;
            }
        });
        List<String> photoPaths = new ArrayList<>();
        //获取每张图片的绝对路径
        for(String name:photoNames){
            photoPaths.add(parentFilePath+"/"+name);
        }
        return photoPaths;
    }

    /*************属性动画部分*************/
    /**
     * 放大view
     * @param view
     */
    public static void zoomIn(View view){
        ViewCompat.animate(view).setDuration(200).scaleX(1.0f).scaleY(1.0f).start();
    }

    /**缩小view
     * @param view
     */
    public static void zoomOut(View view){
        ViewCompat.animate(view).setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
    }
}
