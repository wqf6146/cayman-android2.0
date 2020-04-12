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
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterStepOneActivity extends BaseActivity {

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.ar_ed_input)
    EditText edInput;

    @BindView(R.id.ar_rl_nextstep)
    RelativeLayout rlNextStep;

    @BindView(R.id.ar_tv_gologin)
    TextView tvGoLogin;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_register_stepone;
    }

    @OnClick({R.id.ar_iv_close, R.id.ar_rl_nextstep, R.id.ar_tv_gologin})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.ar_rl_nextstep:
                String account = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(account)){
                    if (account.length() < 8){
                        ToastUtils.showToast("账号不能少于8位");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("account",account);
                    showActivity(RegisterStepTwoActivity.class, bundle);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.ar_tv_gologin:
                showActivity(LoginStepOneActivity.class,null);
                break;
        }
    }

}
