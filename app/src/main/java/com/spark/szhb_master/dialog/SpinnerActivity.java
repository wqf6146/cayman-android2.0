package com.spark.szhb_master.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.spark.szhb_master.R;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.ShareUtils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public class SpinnerActivity extends Activity {

    @BindView(R.id.liner_frd)
    LinearLayout liner_frd;
    @BindView(R.id.liner_wx)
    LinearLayout liner_wx;

    private String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_share);
        liner_frd = findViewById(R.id.liner_frd);
        liner_wx = findViewById(R.id.liner_wx);
//        initWechatShare();

        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        liner_frd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShareUtils.shareWeb(SpinnerActivity.this, url, GlobalConstant.share_title +"--"+ GlobalConstant.share_des, GlobalConstant.share_description
//                        ,"", R.mipmap.frd_wx, SHARE_MEDIA.WEIXIN_CIRCLE);
                finish();
            }
        });

        liner_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ShareUtils.shareWeb(SpinnerActivity.this, url,  GlobalConstant.share_title, GlobalConstant.share_description,
//                        GlobalConstant.imageurl, R.mipmap.image_add, SHARE_MEDIA.WEIXIN);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
