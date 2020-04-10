package com.spark.szhb_master.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.R;

/**
 * Created by Administrator on 2018/8/29 0029.
 */

public class CopyToast extends Toast {


    private static CopyToast toast;
    private static ImageView toast_img;


    public CopyToast(Context context) {

        super(context);
    }

    /**
     * 初始化Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void initToast(Context context, CharSequence text) {
        try {
            cancelToast();

            toast = new CopyToast(context);

            // 获取LayoutInflater对象
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 由layout文件创建一个View对象
            View layout = inflater.inflate(R.layout.toast_layout, null);

            // 吐司上的图片
             toast_img = (ImageView) layout.findViewById(R.id.toast_img);

            // 吐司上的文字
            TextView toast_text = (TextView) layout.findViewById(R.id.toast_text);
            toast_text.setText("   "+text+"   ");
//            toast_text.setText("   复制成功   ");
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 170);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }
    /**
     * 显示Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void showToast(Context context, CharSequence text) {
        // 初始化一个新的Toast对象
        initToast(context, text);

        if (text.equals("不可修改")){
            toast_img.setVisibility(View.GONE);
        }

        // 设置显示时长
        toast.setDuration(Toast.LENGTH_SHORT);
        // 显示Toast
        toast.show();
    }


    /**
     * 显示一个带图标的吐司
     *
     * @param context   上下文
     * @param text      显示的文本
     */
    public static void showText(Context context, CharSequence text) {
        showToast(context, text);
    }



}
