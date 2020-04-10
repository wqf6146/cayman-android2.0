package com.spark.szhb_master.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/10/16 0016.
 */

public class DrawLine extends View implements View.OnClickListener {

    // 定义画笔
    private Paint mPaint;
    // 用于获取文字的宽和高
    private Rect mBounds;
    // 计数值，每点击一次本控件，其值增加1
    private int mCount;

    public DrawLine(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化画笔、Rect
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        // 本控件的点击事件
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(getResources().getColor(R.color.white));                    //设置画笔颜色
        canvas.drawColor(Color.WHITE);                  //设置背景颜色
        mPaint.setStrokeWidth((float) 1.0);              //设置线宽
        canvas.drawLine(150, 0, 155, 250, mPaint);
    }

    @Override
    public void onClick(View v) {
        mCount++;

        // 重绘
        invalidate();
    }

}

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        paint.setColor(getResources().getColor(R.color.white));                    //设置画笔颜色
//        canvas.drawColor(Color.WHITE);                  //设置背景颜色
//        paint.setStrokeWidth((float) 1.0);              //设置线宽
//        canvas.drawLine(150, 0, 155, 250, paint);



