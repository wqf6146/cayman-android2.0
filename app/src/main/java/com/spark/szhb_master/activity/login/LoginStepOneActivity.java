package com.spark.szhb_master.activity.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.signup.SignUpActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginStepOneActivity extends BaseActivity {

    @BindView(R.id.as_iv_close)
    ImageView ivClose;

    @BindView(R.id.as_ed_input)
    EditText edInput;

    @BindView(R.id.as_rl_nextstep)
    RelativeLayout rlNextStep;

    @BindView(R.id.as_tv_goregister)
    TextView tvGoRegister;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login_stepone;
    }

    @OnClick({R.id.as_iv_close, R.id.as_ed_input, R.id.as_rl_nextstep, R.id.as_tv_goregister})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.as_iv_close:
                finish();
                break;
            case R.id.as_tv_forgotpwd:
                showActivity(SignUpActivity.class, null);
                break;
            case R.id.as_rl_nextstep:
                String account = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(account)){
                    Bundle bundle = new Bundle();
                    bundle.putString("account",account);
                    showActivity(LoginStepTwoActivity.class, bundle);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.as_tv_goregister:
                break;
        }
    }

}
