package com.spark.szhb_master.ui.intercept;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Wonderful on 2017/7/29.
 * 带事件拦截的ScrollView 和 滚动监听
 */

public class MyScrollView extends ScrollView {
    private boolean isIntercept = true;
    private OnScrollChangeListener onScrollChangedListener;

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept && super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(x, y, oldx, oldy);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangeListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }
}
