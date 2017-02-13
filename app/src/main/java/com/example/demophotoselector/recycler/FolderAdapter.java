package com.example.demophotoselector.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demophotoselector.R;
import com.example.demophotoselector.listener.OnItemViewClickListener;
import com.example.demophotoselector.model.PhotoFolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**FolderAdapter
 * Created by clement on 17/2/13.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private Context mContext;
    private List<PhotoFolder> photoFolders;
    private OnItemViewClickListener onItemViewClickListener;
    public FolderAdapter(Context context,List<PhotoFolder> photoFolders) {
        this.mContext = context;
        this.photoFolders = photoFolders;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_folder_layout,parent,false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FolderViewHolder holder, int position) {
        String firstPhotoPath = photoFolders.get(position).getPath()+"/"+photoFolders.get(position).getPhotos().get(0);
        //显示图片
        Glide.with(mContext).load(firstPhotoPath)
                .centerCrop()
                .into(holder.iv_icon);
        holder.tv_count.setText(String.valueOf(photoFolders.get(position).getCount()));
        holder.tv_name.setText(photoFolders.get(position).getName());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
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
        if(photoFolders==null){
            return 0;
        }
        return photoFolders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.rootView)
        RelativeLayout rootView;
        @BindView(R.id.iv_icon)
        ImageView iv_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_count)
        TextView tv_count;
        public FolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener;
    }
}
