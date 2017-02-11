package com.example.demophotoselector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.example.demophotoselector.constant.MessageConstant;
import com.example.demophotoselector.recycler.PhotoAdapter;
import com.example.demophotoselector.recycler.SpaceItemDecoration;
import com.example.demophotoselector.util.PhotoUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoPreviewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private PhotoAdapter adapter;
    private PhotoThread photoThread;
    private PhotoHelper photoHelper;
    private PhotoHandler photoHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        initRecyclerView();
        start2LoadPhotos();
    }
    private void initRecyclerView(){
        //设置布局,一行显示3张图片(高度自适应)
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        //设置分割线
        recyclerView.addItemDecoration(new SpaceItemDecoration(8,3));
    }

    /**显示图片
     * @param showList
     */
    private void showRecyclerView(@NonNull List<String> showList){
        adapter = new PhotoAdapter(this,showList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 开启线程读取图片
     */
    private void start2LoadPhotos(){
        photoHandler = new PhotoHandler(this);
        photoThread = new PhotoThread();
        photoThread.start();
    }

    /**
     * 声明一个静态的handler处理消息
     */
    private static class PhotoHandler extends Handler{
        private WeakReference<PhotoPreviewActivity> weakReference;
        public PhotoHandler(PhotoPreviewActivity activity) {
            weakReference = new WeakReference<PhotoPreviewActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //将消息传递到activity
            PhotoPreviewActivity tempActivity = weakReference.get();
            if(tempActivity!=null){
                tempActivity.handleMessage(msg);
            }
        }
    }

    /**处理接收到的消息
     * @param msg
     */
    public void handleMessage(Message msg){
        switch (msg.what){
            case MessageConstant.MESSAGE_LOAD_FINISH:
                List<String> showList = PhotoUtil.getPhotoPathsFromFolder(photoHelper.getmImgDir());
                //显示图片
                showRecyclerView(showList);
                break;
            case MessageConstant.MESSAGE_NO_EXTERNAL_STORAGE:
                Toast.makeText(PhotoPreviewActivity.this,getString(R.string.no_storage),Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 读取图片详细的线程
     */
    private class PhotoThread extends Thread{
        @Override
        public void run() {
            super.run();
            photoHelper = new PhotoHelper(PhotoPreviewActivity.this,photoHandler);
            photoHelper.start();
        }
    }

}
