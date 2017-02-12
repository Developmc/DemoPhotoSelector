package com.example.demophotoselector.model;

/**图片实例
 * Created by developmc on 17/2/12.
 */

public class Photo {
    private String path;
    private boolean isSelect;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
