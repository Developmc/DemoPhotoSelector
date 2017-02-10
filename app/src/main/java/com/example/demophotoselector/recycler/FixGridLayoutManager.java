package com.example.demophotoselector.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**GridLayoutManager高度自适应，保持高度==宽度
 * Created by clement on 17/2/10.
 */

public class FixGridLayoutManager extends GridLayoutManager {
    private int[] mMeasuredDimension = new int[2];
    private int mChildPerLines;

    public FixGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        this.mChildPerLines = spanCount;
    }

    public FixGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public FixGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
//        int height = 0;
//        int childCount = getItemCount();
//        for(int i=0;i<childCount;i++){
//            View childView = getChildAt(i);
//            if(childView == null){
//                return;
//            }
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)childView.getLayoutParams();
//            //重新计算高度
//            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,getPaddingTop()+getPaddingBottom(),
//                    lp.height);
//            childView.measure(widthSpec,childHeightSpec);
//            if(i%getSpanCount()==0){
//                int measuredHeight = childView.getMeasuredHeight()+getDecoratedBottom(childView)+
//                        lp.topMargin+lp.bottomMargin;
//                height+=measuredHeight;
//            }
//        }
//        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec),height);
//    }
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {

        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int height = 0;
        for (int i = 0; i < getItemCount(); ) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);
            height = height + mMeasuredDimension[1];
            i = i + mChildPerLines;
        }

        // If child view is more than screen size, there is no need to make it wrap content. We can use original onMeasure() so we can scroll view.
        if (height < heightSize) {
            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }
            setMeasuredDimension(widthSize, height);
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {

        View view = getChildAt(position);
        if (view != null) {
            // For adding Item Decor Insets to view
            super.measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    getPaddingLeft() + getPaddingRight() + getDecoratedLeft(view) + getDecoratedRight(view), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom() + getPaddingBottom() + getDecoratedBottom(view), p.height);
            view.measure(childWidthSpec, childHeightSpec);

            // Get decorated measurements
            measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin + p.rightMargin;
            measuredDimension[1] = getDecoratedMeasuredHeight(view) + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

}
