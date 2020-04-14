package com.spark.szhb_master.activity.forgot_pwd;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.activity.login.LoginStepTwoActivity;
import com.spark.szhb_master.activity.signup.RegisterStepOneActivity;
import com.spark.szhb_master.activity.signup.RegisterStepTwoActivity;
import com.spark.szhb_master.activity.signup.SignUpContract;
import com.spark.szhb_master.activity.signup.SignUpPresenter;
import com.spark.szhb_master.base.ActivityManage;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class ForgotPwdStepThreeActivity extends BaseActivity implements ForgotPwdContract.View{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.ar_ed_input)
    EditText edInput;

    @BindView(R.id.ar_rl_nextstep)
    RelativeLayout rlNextStep;

    @BindView(R.id.ar_tv_gologin)
    TextView tvGoLogin;

    private String mAccount;
    private int mCode;

    private ForgotPwdContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_resetpwd_stepthree;
    }

    @Override
    protected void initData() {
        super.initData();
        new PhoneForgotPresenter(Injection.provideTasksRepository(activity), this);
        mAccount = getIntent().getStringExtra("account");
        mCode = getIntent().getIntExtra("code",0);
    }

    @Override
    public void setPresenter(ForgotPwdContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void forgotCodeSuccess(String obj) {

    }

    @Override
    public void forgotCodeFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void captchSuccess(JSONObject obj) {

    }

    @Override
    public void captchFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void doForgetSuccess(String obj) {
        ToastUtils.showToast(getString(R.string.change_login_pwd_success_tag));
        ActivityManage.closeActivity(ForgotPwdStepOneActivity.class);
        ActivityManage.closeActivity(ForgotPwdStepTwoActivity.class);
        ActivityManage.closeActivity(LoginStepTwoActivity.class);
        finish();
    }

    @Override
    public void doForgetFail(Integer code, String toastMessage) {
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
                String pwd = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(mAccount) && StringUtils.isNotEmpty(pwd)) {
                    if (pwd.length()<8){
                        ToastUtils.showToast("密码不能少于8位");
                        return;
                    }
                    HashMap map = new HashMap<>();
                    map.put("code", mCode);
                    map.put("new_password", pwd);
                    map.put("account",mAccount);
                    presenter.doForget(map);
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
