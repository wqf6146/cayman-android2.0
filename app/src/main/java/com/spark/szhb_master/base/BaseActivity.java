package com.spark.szhb_master.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.lock.LockActivity;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.utils.KeyboardUtils;
import com.spark.szhb_master.utils.LogUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;
    protected Activity activity;
    private PopupWindow loadingPopup;
    protected View notLoginView;
    private Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isSetTitle = false;
    private ViewGroup emptyView;
    boolean lockOpen = false;
    protected boolean isNeedChecke = true;// 解锁界面 是不需要判断的 用此变量控制
    protected boolean isNeedhide = true;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initLanguage();
        setContentView(getActivityLayoutId());
        unbinder = ButterKnife.bind(this);
        activity = this;
        initBaseView();
        if (isImmersionBarEnabled()) initImmersionBar();
        initView();
        initData();
        ActivityManage.addActivity(this);
        emptyView = getEmptyView();
        setListener();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    loadData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 设置基础化控件
     */
    private void initBaseView() {
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        llTitle = findViewById(R.id.llTitle);
        tvGoto = findViewById(R.id.tvGoto);
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    private void setShowBackBtn(boolean showBackBtn) {
        if (ivBack != null && showBackBtn) {
            if (showBackBtn) {
                ivBack.setVisibility(View.VISIBLE);
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            } else {
                ivBack.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化所有的控件
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 各控件的点击事件
     *
     * @param v
     */
    protected void setOnClickListener(View v) {

    }


    /**
     * 各控件的点击
     */
    protected void setListener() {

    }

    /**
     * 跳转activity,不关闭当前界面
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle) {
        showActivity(cls, bundle, -1);
    }

    /**
     * 跳转activity,不关闭当前界面，含跳转回来的的回调
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle, int requesCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        if (requesCode >= 0)
            startActivityForResult(intent, requesCode);
        else
            startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNeedShowLockActivity()) LockActivity.actionStart(this);
    }

    /**
     * 手势解锁
     *
     * @return
     */
    protected boolean isNeedShowLockActivity() {
        boolean isLogin = MyApplication.getApp().isLogin();//登录否？
        lockOpen = !StringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());//开启否？
        boolean b = SharedPreferenceInstance.getInstance().getIsNeedShowLock();//是否处于后台过？
        isOpen =   SharedPreferenceInstance.getInstance().getIsNeedColse();
        return isLogin && isNeedChecke && lockOpen && b && isOpen;
    }

    private boolean isOpen;
    /**
     * 初始化语言设置
     */
    private void initLanguage() {
        LogUtils.i("initLanguage");
        Locale l = null;
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            l = Locale.CHINESE;
        } else if (code == 2) {
            l = Locale.ENGLISH;
        } else if (code == 3) {
            l = Locale.JAPANESE;
        }
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.locale = l;
        resources.updateConfiguration(config, dm);
    }

    public void setSetTitleAndBack(boolean setTitle, boolean isShowBackBtn) {
        setShowBackBtn(isShowBackBtn);
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
            immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        }
        if (!setTitle) {
            isSetTitle = true;
            immersionBar.setTitleBar(this, llTitle);
        }
    }

    public void setImmersionBar(View view){
        immersionBar.setTitleBar(this, view);
    }

    /**
     * 子类重写实现扩展设置
     */
    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
    }

    /**
     * 获取布局ID
     */
    protected abstract int getActivityLayoutId();

    /**
     * 获取空布局的父布局
     */
    protected ViewGroup getEmptyView() {
        return null;
    }

    /**
     * 是否启用沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始数据加载
     */
    protected void loadData() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        lockOpen = !StringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());
        isOpen =   SharedPreferenceInstance.getInstance().getIsNeedColse();
        if (lockOpen && isOpen ) {
            if (!CommonUtils.isAppOnForeground(getApplicationContext()))
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(true);
            else
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityManage.removeActivity(this);
        hideLoadingPopup();
        if (immersionBar != null) immersionBar.destroy();
    }

    /**
     * 显示加载框
     */
    public void displayLoadingPopup() {
        if (loadingPopup == null) {
            View loadingView = getLayoutInflater().inflate(R.layout.pop_loading, null);
            loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadingPopup.setFocusable(true);
            loadingPopup.setClippingEnabled(false);
            loadingPopup.setBackgroundDrawable(new ColorDrawable());
        }
        if (!loadingPopup.isShowing())
            loadingPopup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 隐藏加载框
     */
    public void hideLoadingPopup() {
        if (loadingPopup != null && loadingPopup.isShowing())
            loadingPopup.dismiss();
    }

    /**
     * 隐藏去登录的布局
     */
    public void hideToLoginView() {
        if (emptyView == null) return;
        emptyView.removeAllViews();
        emptyView.setVisibility(View.GONE);
    }

    /**
     * 获取用户token
     */
    public String getToken() {
        return MyApplication.getApp().getCurrentUser().getToken();
    }

    public int getId() {
        return MyApplication.getApp().getCurrentUser().getId();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    /**
     * 处理软件盘智能弹出和隐藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                if (isNeedhide) {
                    KeyboardUtils.editKeyboard(ev, view, this);
                }
                if (isFastDoubleClick()) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (timeD >= 0 && timeD <= 500) {
            return true;
        } else {
            mLastClickTime = time;
            return false;
        }
    }

    private long mLastClickTime;
}
