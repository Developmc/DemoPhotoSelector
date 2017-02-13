package com.example.demophotoselector.model;

import java.util.List;

/**文件夹实体类
 * Created by clement on 17/2/10.
 */

public class PhotoFolder {
    /**
     * 文件夹路径
     */
    private String path;
    /**
     * 文件夹名
     */
    private String name;
    /**
     * 文件夹下图片的数量
     */
    private int count;
    /**
     * 文件夹下图片的列表
     */
    private List<String> photos;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
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
