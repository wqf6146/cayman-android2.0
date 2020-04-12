package com.spark.szhb_master.activity.signup;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.login.LoginStepTwoActivity;
import com.spark.szhb_master.base.ActivityManage;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class RegisterStepThreeActivity extends BaseActivity implements SignUpContract.View{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.ar_ed_input)
    EditText edInput;

    @BindView(R.id.ar_rl_nextstep)
    RelativeLayout rlNextStep;


    private String mAccount;
    private String mPwd;

    private SignUpContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_register_stepthree;
    }

    @Override
    protected void initData() {
        super.initData();
        new SignUpPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        mAccount = getIntent().getStringExtra("account");
        mPwd = getIntent().getStringExtra("pwd");
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void sighUpSuccess(String obj) {
        MobclickAgent.onEvent(RegisterStepThreeActivity.this, "register");
        ToastUtils.showToast("注册成功，请直接登录");
        ActivityManage.closeActivity(RegisterStepOneActivity.class);
        ActivityManage.closeActivity(RegisterStepTwoActivity.class);
        finish();
    }

    @Override
    public void sighUpFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @OnClick({R.id.ar_iv_close, R.id.ar_rl_nextstep})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.ar_rl_nextstep:
                String code = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(mAccount) && StringUtils.isNotEmpty(mPwd) && StringUtils.isNotEmpty(code)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("password", mPwd);
                    map.put("invite_code", code);
                    map.put("account", mAccount);
                    presenter.sighUp(UrlFactory.getSignUpByPhone(), map);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
        }
    }

}
