package com.example.demophotoselector.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.demophotoselector.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**显示预览图片的adapter
 * Created by clement on 17/2/10.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private Context mContext;
    private List<Integer> list;
    public PhotoAdapter(Context context,List<Integer> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public PhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_preview_layout,parent,false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder holder, int position) {
        holder.iv_icon.setImageResource(list.get(position));
    }

    @Override
    public int getItemCount() {
        if(list!=null){
            return list.size();
        }
        return 0;
    }
    class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView iv_icon ;
        public PhotoViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}