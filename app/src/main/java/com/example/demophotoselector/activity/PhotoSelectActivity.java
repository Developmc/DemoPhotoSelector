package com.example.demophotoselector.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demophotoselector.PhotoHelper;
import com.example.demophotoselector.R;
import com.example.demophotoselector.constant.MessageConstant;
import com.example.demophotoselector.listener.OnItemViewClickListener;
import com.example.demophotoselector.listener.OnSelectedListener;
import com.example.demophotoselector.model.Photo;
import com.example.demophotoselector.recycler.DragCallBack;
import com.example.demophotoselector.recycler.FolderAdapter;
import com.example.demophotoselector.recycler.GridSpacingItemDecoration;
import com.example.demophotoselector.recycler.PhotoAdapter;
import com.example.demophotoselector.util.PhotoUtil;
import com.example.demophotoselector.view.BottomSheetDialogEx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoSelectActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_folder)
    TextView tv_folder;
    @BindView(R.id.tv_num)
    TextView tv_num;
    @BindView(R.id.tv_preview)
    TextView tv_preview;
    private PhotoAdapter adapter;
    private PhotoThread photoThread;
    private PhotoHelper photoHelper;
    private PhotoHandler photoHandler;
    private final int viewCount = 3;
    //记录当前已经被选择的照片
    private List<Photo> selectedPhotos = new ArrayList<>();
    private BottomSheetDialogEx folderDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
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
        adapter = new PhotoAdapter(this,new ArrayList<Photo>(),selectedPhotos);
        recyclerView.setAdapter(adapter);
        //监听选中图片更新事件
        adapter.setOnSelectedListener(new OnSelectedListener() {
            @Override
            public void onSelectedPhotoUpdate(List<Photo> selectedPhotos) {
                if(selectedPhotos!=null && !selectedPhotos.isEmpty()){
                    tv_preview.setVisibility(View.VISIBLE);
                    tv_num.setVisibility(View.VISIBLE);
                    tv_num.setText(String.valueOf(selectedPhotos.size()));
                }
                else{
                    tv_preview.setVisibility(View.INVISIBLE);
                    tv_num.setVisibility(View.INVISIBLE);
                }
            }
        });
        //监听图片被点击事件
        adapter.setOnItemViewClickListener(new OnItemViewClickListener() {
            @Override
            public void onItemViewClick(View itemView, int position) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_icon);

                Intent intent = new Intent(PhotoSelectActivity.this, PhotoPreviewActivity.class);
                Rect rect = new Rect();
                imageView.getGlobalVisibleRect(rect);
                intent.putExtra(PhotoPreviewActivity.IMAGE_ORIGIN_RECT, rect);
                intent.putExtra(PhotoPreviewActivity.IMAGE_SCALE_TYPE, imageView.getScaleType());
                intent.putExtra(PhotoPreviewActivity.IMAGE_RES_ID, adapter.getPhotos().get(position).getPath());
                // 打开第二个界面，要屏蔽Activity的默认转场效果
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        //支持拖拽
        DragCallBack callBack = new DragCallBack(this,adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callBack);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**更新数据,显示图片
     * @param photos
     */
    private void showPhotos(@NonNull List<Photo> photos){
        if(adapter!=null){
            adapter.setPhotos(photos);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置title
     */
    private void setContent(String folderName){
        tv_title.setText(folderName);
        tv_folder.setText(folderName);
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
    @OnClick(R.id.tv_folder)
    void onFolderClick(){
        showFolderListDialog();
    }
    /**
     * 声明一个静态的handler处理消息
     */
    private static class PhotoHandler extends Handler{
        private WeakReference<PhotoSelectActivity> weakReference;
        public PhotoHandler(PhotoSelectActivity activity) {
            weakReference = new WeakReference<PhotoSelectActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //将消息传递到activity
            PhotoSelectActivity tempActivity = weakReference.get();
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
                List<Photo> photos = PhotoUtil.getPhotos(photoHelper.getCurrentFolder().getPath());
                //显示图片
                showPhotos(photos);
                //设置title
                setContent(photoHelper.getCurrentFolder().getName());
                break;
            case MessageConstant.MESSAGE_NO_EXTERNAL_STORAGE:
                Toast.makeText(PhotoSelectActivity.this,getString(R.string.no_storage),Toast.LENGTH_SHORT).show();
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
            photoHelper = new PhotoHelper(PhotoSelectActivity.this,photoHandler);
            photoHelper.start();
        }
    }

    /**
     * 显示文件夹列表的dialog
     */
    private void showFolderListDialog(){
        folderDialog = new BottomSheetDialogEx(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet,null);
        //初始化RecyclerView
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FolderAdapter folderAdapter = new FolderAdapter(this,photoHelper.getPhotoFolders());
        recyclerView.setAdapter(folderAdapter);
        folderAdapter.setOnItemViewClickListener(new OnItemViewClickListener() {
            @Override
            public void onItemViewClick(View itemView, int position) {
                //更新显示的图片
                List<Photo> photos = PhotoUtil.getPhotos(photoHelper.getPhotoFolders().get(position).getPath());
                //显示图片
                showPhotos(photos);
                //设置title
                setContent(photoHelper.getPhotoFolders().get(position).getName());
                //关闭dialog
                folderDialog.dismiss();
            }
        });
        folderDialog.setContentView(view);
        folderDialog.show();
    }
}
