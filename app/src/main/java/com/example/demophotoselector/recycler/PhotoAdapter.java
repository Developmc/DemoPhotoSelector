package com.example.demophotoselector.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.demophotoselector.R;
import com.example.demophotoselector.listener.OnItemSelectListener;
import com.example.demophotoselector.model.Photo;
import com.example.demophotoselector.util.PhotoUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**显示预览图片的adapter
 * Created by clement on 17/2/10.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private Context mContext;
    private List<Photo> list;
    //记录当前已经被选择的照片
    private List<Photo> selectedPhotos;
    private OnItemSelectListener onItemSelectListener;
    public PhotoAdapter(Context context,List<Photo> list,List<Photo> selectedPhotos){
        this.mContext = context;
        this.selectedPhotos = selectedPhotos;
        //更新list
        updatePhotos(list,selectedPhotos);
        this.list = list;
    }
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_preview_layout,parent,false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.PhotoViewHolder holder, int position) {
        //显示图片
        Glide.with(mContext).load(list.get(position).getPath())
                .centerCrop()
                .into(holder.iv_icon);
        //显示itemView的视图
        showItemView(list.get(position),holder);
        //响应点击事件
        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = list.get(holder.getAdapterPosition());
                //更新值
                if(photo.isSelect()){
                    photo.setSelect(false);
                }
                else{
                    photo.setSelect(true);
                }
                //显示itemView
                showItemView(photo,holder);
                //通知外界
                if(onItemSelectListener!=null){
                    onItemSelectListener.onItemSelect(holder.getAdapterPosition(),true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }

    /**
     * 显示itemView
     */
    private void showItemView(@NonNull Photo photo,@NonNull PhotoViewHolder holder){
        //如果已经勾选，则取消勾选
        if(photo.isSelect()){
            //更新"勾选"的颜色
            holder.iv_select.setColorFilter(ContextCompat.getColor(
                    mContext,R.color.colorPrimary));
            //显示遮盖层
            holder.view_shadow.setVisibility(View.VISIBLE);
            //缩小itemView
            PhotoUtil.zoomOut(holder.itemView);
        }
        else{
            //取消"勾选"的颜色
            holder.iv_select.setColorFilter(ContextCompat.getColor(
                    mContext,R.color.white));
            //显示遮盖层
            holder.view_shadow.setVisibility(View.GONE);
            //放大ItemView
            PhotoUtil.zoomIn(holder.itemView);
        }
    }

    /**更新文件夹下照片的状态
     * @param list
     * @param selectedPhotos
     */
    private void updatePhotos(@NonNull List<Photo> list,@NonNull List<Photo> selectedPhotos){
        if(selectedPhotos.isEmpty()){
            return;
        }
        for(Photo photo:list){
            for(Photo selectedPhoto:selectedPhotos){
                //如果该图片已经被选中
                if(photo.getPath().equals(selectedPhoto.getPath())){
                    photo.setSelect(selectedPhoto.isSelect());
                }
            }
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        @BindView(R.id.iv_icon)
        ImageView iv_icon ;
        @BindView(R.id.iv_select)
        ImageView iv_select;
        @BindView(R.id.view_shadow)
        View view_shadow;
        public PhotoViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }
}
