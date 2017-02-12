package com.example.demophotoselector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demophotoselector.constant.MessageConstant;
import com.example.demophotoselector.model.Photo;
import com.example.demophotoselector.recycler.GridSpacingItemDecoration;
import com.example.demophotoselector.recycler.PhotoAdapter;
import com.example.demophotoselector.util.PhotoUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoPreviewActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private PhotoAdapter adapter;
    private PhotoThread photoThread;
    private PhotoHelper photoHelper;
    private PhotoHandler photoHandler;
    private final int viewCount = 3;
    //记录当前已经被选择的照片
    private List<Photo> selectedPhotos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        initRecyclerView();
        start2LoadPhotos();
    }
    private void initRecyclerView(){
        //设置布局,一行显示3张图片(高度等于宽度)
        final GridLayoutManager layoutManager = new GridLayoutManager(this,viewCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(viewCount,5, false));
        recyclerView.setLayoutManager(layoutManager);
    }

    /**显示图片
     * @param photos
     */
    private void showRecyclerView(@NonNull List<Photo> photos){
        adapter = new PhotoAdapter(this,photos,selectedPhotos);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置title
     */
    private void setTitle(String title){
        tv_title.setText(title);
    }
    /**
     * 开启线程读取图片
     */
    private void start2LoadPhotos(){
        photoHandler = new PhotoHandler(this);
        photoThread = new PhotoThread();
        photoThread.start();
    }

    @OnClick(R.id.iv_back)
    void onBackPress(){
        finish();
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
                List<String> showList = PhotoUtil.getPhotoPathsFromFolder(photoHelper.getMaxFile());
                List<Photo> photos = new ArrayList<>();
                //将拿到的集合转为需要的对象集合
                for (String path:showList){
                    Photo photo = new Photo();
                    photo.setPath(path);
                    photos.add(photo);
                }
                //显示图片
                showRecyclerView(photos);
                //设置title
                setTitle(photoHelper.getCurrentFolder().getName());
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
