package com.spark.szhb_master.activity.login;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.forgot_pwd.ForgotPwdStepOneActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.signup.RegisterStepOneActivity;
import com.spark.szhb_master.activity.signup.SignUpActivity;
import com.spark.szhb_master.base.ActivityManage;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.KeyboardUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class LoginStepTwoActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.as_iv_close)
    ImageView ivClose;

    @BindView(R.id.as_ed_input)
    EditText edInput;

    @BindView(R.id.as_tv_forgotpwd)
    TextView tvForgotPwd;

    @BindView(R.id.as_rl_login)
    RelativeLayout rlLogin;

    @BindView(R.id.as_tv_goregister)
    TextView tvGoRegister;

    private LoginContract.Presenter presenter;

    private String mAccount;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login_steptwo;
    }

    @Override
    protected void initData() {
        super.initData();
        mAccount = getIntent().getStringExtra("account");
        new LoginPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick({R.id.as_iv_close, R.id.as_ed_input, R.id.as_tv_forgotpwd, R.id.as_rl_login,R.id.as_tv_goregister})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.as_iv_close:
                finish();
                break;
            case R.id.as_tv_forgotpwd:
                showActivity(ForgotPwdStepOneActivity.class, null);
                break;
            case R.id.as_rl_login:
                String password = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(mAccount) && StringUtils.isNotEmpty(password)) {
                    KeyboardUtils.hideSoftInput(activity,edInput);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("password", password);
                    map.put("account", mAccount);
                    presenter.login(map);

                } else {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.as_tv_goregister:
                showActivity(RegisterStepOneActivity.class,null);
                break;
        }
    }

    @Override
    public void loginSuccess(User obj) {

        MainActivity.isAgain = true;
        MyApplication.getApp().setCurrentUser(obj);
        setResult(RESULT_OK);
        MobclickAgent.onEvent(LoginStepTwoActivity.this, "login");
        ActivityManage.closeActivity(LoginStepOneActivity.class);
        ToastUtils.show("登录成功", Toast.LENGTH_SHORT);
        finish();
    }


    @Override
    public void loginFail(Integer code, String toastMessage) {
        MyApplication.getApp().deleteCurrentUser();
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
