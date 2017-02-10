package com.example.demophotoselector.recycler;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**设置间距
 * Created by clement on 17/2/10.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    //间隔大小
    private int space;
    //每行view的数量
    private int viewCount;

    public SpaceItemDecoration(int space,int viewCount) {
        this.space = space;
        this.viewCount = viewCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % viewCount==0) {
            outRect.left = 0;
        }
    }
}
