package com.example.demophotoselector.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**正方形的view(宽为边)
 * Created by developmc on 17/2/12.
 */

public class SquareFrameLayout extends FrameLayout{
    public SquareFrameLayout(Context context) {
        super(context);
    }

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),getMeasuredWidth());
    }
}
