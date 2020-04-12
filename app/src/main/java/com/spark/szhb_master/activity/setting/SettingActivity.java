package com.spark.szhb_master.activity.setting;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.aboutus.AboutUsActivity;
import com.spark.szhb_master.activity.feed.FeedbackActivity;
import com.spark.szhb_master.activity.language.LanguageActivity;
import com.spark.szhb_master.activity.switch_user.SwitchUserActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.SharedPreferenceInstance;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.llSwitchUser)
    LinearLayout llSwitchUser;
    @BindView(R.id.llSwitchLanguage)
    LinearLayout llSwitchLanguage;
    @BindView(R.id.llFeed)
    LinearLayout llFeed;
    @BindView(R.id.llAboutUs)
    LinearLayout llAboutUs;
    @BindView(R.id.tvLoginOut)
    TextView tvLoginOut;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.setting));
        tvGoto.setVisibility(View.INVISIBLE);
        super.initData();
    }

    @OnClick({R.id.tvLoginOut, R.id.llSwitchLanguage, R.id.llSwitchUser, R.id.llAboutUs, R.id.llFeed})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llSwitchUser:
                showActivity(SwitchUserActivity.class, null);
                break;
            case R.id.llSwitchLanguage:
                showActivity(LanguageActivity.class, null);
                break;
            case R.id.llFeed:
                showActivity(FeedbackActivity.class, null);
                break;
            case R.id.llAboutUs:
                showActivity(AboutUsActivity.class, null);
                break;
            case R.id.tvLoginOut:
                showCofirmDialog();
                break;
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (!MyApplication.getApp().isLogin()) {
            tvLoginOut.setVisibility(View.GONE);
        }
    }

    /**
     * 确认是否退出登录
     */
    private void showCofirmDialog() {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title("提示").titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.logout_current_account)).btnText(getString(R.string.cancle), getString(R.string.confirm)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        MyApplication.getApp().deleteCurrentUser();
                        MyApplication.getApp().loginAgain(SettingActivity.this);
//                        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
//                        SharedPreferenceInstance.getInstance().saveLockPwd("");
//                        SharedPreferenceInstance.getInstance().saveToken("");
//                        SharedPreferenceInstance.getInstance().saveGoogle("close");
                        dialog.superDismiss();
                        setResult(RESULT_OK);
                        activity.finish();
                    }
                });
        dialog.show();
    }

}
