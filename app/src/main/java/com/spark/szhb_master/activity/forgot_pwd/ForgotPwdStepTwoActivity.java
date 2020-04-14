package com.spark.szhb_master.activity.forgot_pwd;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginStepOneActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class ForgotPwdStepTwoActivity extends BaseActivity implements ForgotPwdContract.View{

    @BindView(R.id.ar_iv_close)
    ImageView ivClose;

    @BindView(R.id.ar_ed_input)
    EditText edInput;

    @BindView(R.id.ar_rl_nextstep)
    RelativeLayout rlNextStep;

    @BindView(R.id.ar_tv_gologin)
    TextView tvGoLogin;

    @BindView(R.id.ar_tv_sendcode)
    TextView tvSendcode;

    @BindView(R.id.ar_tv_desc)
    TextView tvDesc;

    private TimeCount timeCount;
    private String mAccount;
    private boolean isEmail = false;

    private ForgotPwdContract.Presenter presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_resetpwd_steptwo;
    }

    @Override
    protected void initData() {
        super.initData();
        new PhoneForgotPresenter(Injection.provideTasksRepository(activity), this);
        timeCount = new TimeCount(90000, 1000, tvSendcode);
        mAccount = getIntent().getStringExtra("account");

        tvDesc.setText(String.format(getString(R.string.reset_pwd_desc1),mAccount.substring(mAccount.length() - 4,mAccount.length())));

        getCode();
    }

    @OnClick({R.id.ar_iv_close,R.id.ar_tv_sendcode, R.id.ar_rl_nextstep, R.id.ar_tv_gologin})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.ar_iv_close:
                finish();
                break;
            case R.id.ar_tv_sendcode:
                getCode();
                break;
            case R.id.ar_rl_nextstep:
                String code = edInput.getText().toString().trim();
                if (StringUtils.isNotEmpty(mAccount) || StringUtils.isNotEmpty(code)){
                    Bundle bundle = new Bundle();
                    bundle.putString("account",mAccount);
                    bundle.putInt("code",Integer.parseInt(code));
                    showActivity(ForgotPwdStepThreeActivity.class, bundle);
                }else{
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.ar_tv_gologin:
                showActivity(LoginStepOneActivity.class,null);
                break;
        }
    }

    @Override
    public void setPresenter(ForgotPwdContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void forgotCodeSuccess(String obj) {
        try {
            timeCount.start();
            tvSendcode.setEnabled(false);
            ToastUtils.showToast("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    }

    @Override
    public void doForgetFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    private void getCode() {
        if (isEmail) {
//            String email = edInput.getText().toString().trim();
//            if (StringUtils.isEmpty(email) || !StringUtils.isEmail(email)) {
//                ToastUtils.showToast(R.string.email_diff);
//            } else {
//                presenter.captch();
//            }
        } else {
            HashMap map = new HashMap<>();
            map.put("account", mAccount);
            presenter.forgotCode(UrlFactory.getEmailForgotPwdCodeUrl(), map);

        }
    }

}
