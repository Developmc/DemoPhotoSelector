package com.example.demophotoselector.model;

/**文件夹实体类
 * Created by clement on 17/2/10.
 */

public class PhotoFolder {
    /**
     * 文件夹路径
     */
    private String dir;
    /**
     * 第一张照片的路径
     */
    private String firstPhotoPath;
    /**
     * 文件夹名
     */
    private String name;
    /**
     * 文件夹下图片的数量
     */
    private int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getFirstPhotoPath() {
        return firstPhotoPath;
    }

    public void setFirstPhotoPath(String firstPhotoPath) {
        this.firstPhotoPath = firstPhotoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
