package com.example.demophotoselector.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.demophotoselector.R;
import com.example.demophotoselector.listener.OnItemViewClickListener;
import com.example.demophotoselector.listener.OnSelectedListener;
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
    private List<Photo> photos;
    //记录当前已经被选择的照片
    private List<Photo> selectedPhotos;
    private OnSelectedListener onSelectedListener;
    private OnItemViewClickListener onItemViewClickListener;
    public PhotoAdapter(Context context,List<Photo> photos,List<Photo> selectedPhotos){
        this.mContext = context;
        this.selectedPhotos = selectedPhotos;
        this.photos = photos;
    }
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_select_layout,parent,false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PhotoAdapter.PhotoViewHolder holder, int position) {
        //显示图片
        Glide.with(mContext).load(photos.get(position).getPath())
                .centerCrop()
                .into(holder.iv_icon);
        //显示itemView的视图
        showItemView(photos.get(position),holder);
        //响应点击事件
        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Photo photo = photos.get(holder.getAdapterPosition());
                //更新值
                if(photo.isSelect()){
                    photo.setSelect(false);
                }
                else{
                    photo.setSelect(true);
                }
                //显示itemView
                showItemView(photo,holder);
                //更新已选择的图片列表
                updateSelectedPhotos(photo,selectedPhotos);
                //通知外界
                if(onSelectedListener!=null){
                    onSelectedListener.onSelectedPhotoUpdate(selectedPhotos);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemViewClickListener!=null){
                    onItemViewClickListener.onItemViewClick(view,holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(photos !=null){
            return photos.size();
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
            PhotoUtil.zoom(holder.itemView);
        }
    }

    /**更新文件夹下照片的状态
     * @param photos
     * @param selectedPhotos
     */
    private void updatePhotos(@NonNull List<Photo> photos,@NonNull List<Photo> selectedPhotos){
        if(selectedPhotos.isEmpty()){
            return;
        }
        for(Photo photo:photos){
            for(Photo selectedPhoto:selectedPhotos){
                //如果该图片已经被选中
                if(photo.getPath().equals(selectedPhoto.getPath())){
                    photo.setSelect(selectedPhoto.isSelect());
                }
            }
        }
    }

    /**
     * 更新
     * @param photo
     * @param selectedPhotos
     * @return
     */
    private void updateSelectedPhotos(@NonNull Photo photo,@NonNull List<Photo> selectedPhotos){
        if(photo.isSelect()){
            selectedPhotos.add(photo);
            return;
        }
        //如果是取消"勾选"
        for(Photo tempPhoto:selectedPhotos){
            if(photo.getPath().equals(tempPhoto.getPath())){
                selectedPhotos.remove(tempPhoto);
                break;
            }
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView iv_icon ;
        @BindView(R.id.iv_select)
        ImageView iv_select;
        @BindView(R.id.view_shadow)
        View view_shadow;
        public PhotoViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }

    public void setPhotos(List<Photo> photos) {
        //更新photos,判断是否有照片已经勾选
        updatePhotos(photos,selectedPhotos);
        this.photos = photos;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
