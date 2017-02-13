package com.example.demophotoselector.recycler;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.demophotoselector.util.PhotoUtil;

import java.util.Collections;

/**实现拖拽滑动
 * Created by clement on 17/2/13.
 */

public class DragCallBack extends ItemTouchHelper.Callback {
    private PhotoAdapter mAdapter;
    private Context mContext;

    public DragCallBack(Context context, PhotoAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags ;
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT;
        }
        else{
            dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        }
        return makeMovementFlags(dragFlags,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
        if (fromPosition < toPosition) {
            //分别把中间所有的item的位置重新交换
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mAdapter.getPhotos(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mAdapter.getPhotos(), i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        //返回true表示执行拖动
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //不需要滑动
    }

    /**当长按选中时回调
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE){
            //放大view
            PhotoUtil.zoomIn(viewHolder.itemView);
            //震动70毫秒
            Vibrator vibrator = (Vibrator)mContext.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(70);
        }
    }

    /**手指松开时回调
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        int position = viewHolder.getLayoutPosition();
        //获取当前view的状态，恢复到相应的状态
        if(mAdapter.getPhotos().get(position).isSelect()){
            PhotoUtil.zoomOut(viewHolder.itemView);
        }
        else{
            PhotoUtil.zoom(viewHolder.itemView);
        }
    }
}
