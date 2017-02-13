package com.example.demophotoselector;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.demophotoselector.constant.MessageConstant;
import com.example.demophotoselector.model.PhotoFolder;
import com.example.demophotoselector.util.PhotoUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**处理图片的工具类
 * Created by clement on 17/2/11.
 */

public class PhotoHelper {
    /**
     * 所有的图片数
     */
    private int totalCount = 0;
    /**
     * 图片最多的文件夹
     */
    private File maxFile;
    /**
     * 图片最多的文件夹下的图片数量
     */
    private int mPicsSize;
    /**
     * 含有图片的文件夹的集合
     */
    private List<PhotoFolder> photoFolders;
    /**
     * 当前选中的文件夹
     */
    private PhotoFolder currentFolder = new PhotoFolder();
    private WeakReference<Activity> weakReference;
    private Handler mHandler;

    public PhotoHelper(Activity activity, Handler handler){
        this.weakReference = new WeakReference<Activity>(activity);
        this.mHandler = handler;
    }

    /**
     * 执行任务
     */
    public void start(){
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            mHandler.sendEmptyMessage(MessageConstant.MESSAGE_NO_EXTERNAL_STORAGE);
            return;
        }
        //开始扫描图片
        scanPhotos();
    }
    /**
     * 获取图片
     */
    private void scanPhotos(){
        HashSet<String> mDirPaths = new HashSet<String>();
        //第一张图片的路径
        String firstPhotoPath = null;
        photoFolders = new ArrayList<>();
        Uri photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //如果activity实例已经不存在
        if(weakReference.get()==null){
            return;
        }
        ContentResolver mContentResolver = weakReference.get().getContentResolver();
        //只查询JPEG和PNG格式的图片
        Cursor mCursor = mContentResolver.query(photoUri,null,MediaStore.Images.Media.MIME_TYPE+"=? or "
                        + MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/jpeg","image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        if(mCursor==null){
            return;
        }
        while (mCursor.moveToNext()){
            //获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //拿到第一张图片的路径
            if(firstPhotoPath ==null){
                firstPhotoPath =path;
            }
            //获取该路径的父路径名
            File parentFile = new File(path).getParentFile();
            if(parentFile==null){
                continue;
            }
            //获得父级目录的路径
            String folderPath = parentFile.getAbsolutePath();
            PhotoFolder photoFolder = null;
            //利用一个HashSet防止多次扫描同一个文件夹
            if(mDirPaths.contains(folderPath)){
                continue;
            }
            else{
                mDirPaths.add(folderPath);
                //初始化PhotoFolder
                photoFolder = new PhotoFolder();
                photoFolder.setPath(folderPath);
                photoFolder.setName(getNameFromPath(folderPath));
            }
            //忽略某些奇怪的图片
            if(parentFile.list()==null){
                continue;
            }
            //获取该文件夹下，所有图片的数量
            List<String> photos = PhotoUtil.getPhotoNames(parentFile);
            int picSize = photos.size();
            totalCount+=picSize;
            photoFolder.setCount(picSize);
            photoFolder.setPhotos(photos);
            photoFolders.add(photoFolder);
            //获取图片数量最多的文件和该文件夹下的图片数量
            if(picSize>mPicsSize){
                mPicsSize = picSize;
                maxFile = parentFile;
                //当前选中的文件夹
                currentFolder = photoFolder;
            }
            //一个文件夹遍历完成后，清除变量
            firstPhotoPath = null;
        }
        //扫描完成，关闭资源
        mCursor.close();
        //通知刷新
        mHandler.sendEmptyMessage(MessageConstant.MESSAGE_LOAD_FINISH);
    }

    /**根据目录获取文件名
     * @param folderPath
     * @return
     */
    private String getNameFromPath(@NonNull String folderPath){
        String[] temp = folderPath.split("/");
        return temp[temp.length-1] ;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getmPicsSize() {
        return mPicsSize;
    }

    public File getMaxFile() {
        return maxFile;
    }

    public PhotoFolder getCurrentFolder() {
        return currentFolder;
    }

    public List<PhotoFolder> getPhotoFolders() {
        return photoFolders;
    }
}
