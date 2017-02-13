package com.example.demophotoselector.util;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.example.demophotoselector.model.Photo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**工具类
 * Created by clement on 17/2/11.
 */

public class PhotoUtil {
    public static List<String> getPhotoNames(@NonNull File parentFile){
        String[] photoNames = parentFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String fileName) {
                if(fileName.endsWith(".jpg")||fileName.endsWith(".png")||fileName.endsWith("jpeg"))
                    return true;
                return false;
            }
        });
        return Arrays.asList(photoNames);
    }
    /**获取文件夹下，所有图片的数量
     * @param parentFile
     * @return
     */
    public static int getPhotoCountFromFolder(@NonNull File parentFile){
        return getPhotoNames(parentFile).size();
    }

    /**获取文件夹下图片的路径列表
     * @param parentFile
     * @return
     */
    public static List<String> getPhotoPathsFromFolder(@NonNull File parentFile){
        String parentFilePath = parentFile.getAbsolutePath();
        //去除非图片文件
        List<String> photoNames = getPhotoNames(parentFile);
        List<String> photoPaths = new ArrayList<>();
        //获取每张图片的绝对路径
        for(String name:photoNames){
            photoPaths.add(parentFilePath+"/"+name);
        }
        return photoPaths;
    }

    /**获取文件夹下图片的路径列表
     * @param folderPath
     * @return
     */
    public static List<String> getPhotoPathsFromFolder(@NonNull String folderPath){
        File file = new File(folderPath);
        return getPhotoPathsFromFolder(file);
    }

    /**获取图片列表
     * @param folderPath
     * @return
     */
    public static List<Photo> getPhotos(@NonNull String folderPath){
        List<String> showList = getPhotoPathsFromFolder(folderPath);
        List<Photo> photos = new ArrayList<>();
        //将拿到的集合转为需要的对象集合
        for (String path:showList){
            Photo photo = new Photo();
            photo.setPath(path);
            photos.add(photo);
        }
        return photos;
    }

    /*************属性动画部分*************/
    /**
     * 恢复原来view
     * @param view
     */
    public static void zoom(View view){
        ViewCompat.animate(view).setDuration(200).scaleX(1.0f).scaleY(1.0f).start();
    }

    /**缩小view
     * @param view
     */
    public static void zoomOut(View view){
        ViewCompat.animate(view).setDuration(200).scaleX(0.9f).scaleY(0.9f).start();
    }

    /**
     * 放大view
     * @param view
     */
    public static void zoomIn(View view){
        ViewCompat.animate(view).setDuration(200).scaleX(1.1f).scaleY(1.1f).start();
    }
}
