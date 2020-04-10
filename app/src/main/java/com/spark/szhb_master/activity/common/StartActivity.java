package com.spark.szhb_master.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.LeadActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.lock.LockActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.serivce.GroupService;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.serivce.MyTextService;
import com.umeng.commonsdk.UMConfigure;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import pl.droidsonroids.gif.GifImageView;

public class StartActivity extends BaseActivity {
    private Timer timer;
    int n = 3;
    @BindView(R.id.image_start)
    ImageView image_start;
    @BindView(R.id.gv_error)
    GifImageView gv_error;

//    <pl.droidsonroids.gif.GifImageView
//    android:id="@+id/gv_error"
//    android:visibility="gone"
//    android:layout_width="match_parent"
//    android:layout_height="match_parent"
//    android:layout_centerInParent="true"
//    android:src="@drawable/oic_cm_two"/>
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).fullScreen(true).init();
        startService(new Intent(activity, MyTextService.class));
        //startService(new Intent(activity, GroupService.class));
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
        if (!isNeedShowLockActivity()) {
            timerStart();
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LockActivity.RETURN_LOCK) timerStart();
    }

    private void timerStart() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (n == 0) {
                    timer.cancel();
                    timer = null;
                    boolean isFirst = SharedPreferenceInstance.getInstance().getIsFirstUse();
                    if (isFirst) {
//                        image_start.setVisibility(View.VISIBLE);
//                        gv_error.setVisibility(View.GONE);
                        showActivity(LeadActivity.class, null);
                    } else {
//                        image_start.setVisibility(View.GONE);
//                        gv_error.setVisibility(View.VISIBLE);
                        showActivity(MainActivity.class, null);
                    }
                    finish();
                }
                n--;
            }
        }, 10, 399);
    }


    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onBackPressed();
    }
}
