package com.example.demophotoselector;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.demophotoselector.model.PhotoFolder;
import com.example.demophotoselector.recycler.PhotoAdapter;
import com.example.demophotoselector.recycler.SpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoPreviewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Integer> photos;
    private PhotoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        initData();
        initRecyclerView();
    }
    private void initData(){
        photos = new ArrayList<>();
        for (int i=0;i<100;i++){
            photos.add(R.mipmap.ic_launcher);
        }
    }
    private void initRecyclerView(){
        //设置布局,一行显示3张图片(高度自适应)
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //设置分割线
        recyclerView.addItemDecoration(new SpaceItemDecoration(8,3));
        adapter = new PhotoAdapter(this,photos);
        recyclerView.setAdapter(adapter);
    }

    private void getPhotos(){
        HashSet mDirPaths = new HashSet();
        //第一张图片的路径
        String firstPhotoPath = null;
        Uri photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = PhotoPreviewActivity.this.getContentResolver();
        //只查询JPEG和PNG格式的图片
        Cursor mCursor = mContentResolver.query(photoUri,null,MediaStore.Images.Media.MIME_TYPE+"=? or "
        + MediaStore.Images.Media.MIME_TYPE+"=?",new String[]{"image/jpeg","image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
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
            String dirPath = parentFile.getAbsolutePath();
            PhotoFolder folder = null;
            //利用一个HashSet防止多次扫描同一个文件夹
            if(mDirPaths.contains(dirPath)){
                continue;
            }
            //开始扫描该目录下的所有图片

        }
    }
}
