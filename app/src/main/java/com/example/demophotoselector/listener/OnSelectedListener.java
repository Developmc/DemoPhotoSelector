package com.example.demophotoselector.listener;

import com.example.demophotoselector.model.Photo;
import com.example.demophotoselector.model.PhotoFolder;

import java.util.List;

/**选择监听器
 * Created by developmc on 17/2/12.
 */

public interface OnSelectedListener {
    void onSelectedPhotoUpdate(List<Photo> selectedPhotos);
}
