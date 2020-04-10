package com.spark.szhb_master.activity.forgot_pwd;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Captcha;
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

/**
 * 忘记密码
 */
public class ForgotPwdActivity extends BaseActivity implements ForgotPwdContract.View {
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRenewPassword)
    EditText etRenewPassword;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;    @BindView(R.id.text_main)
    TextView text_main;    @BindView(R.id.text_other)
    TextView text_other;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    private TimeCount timeCount;
    private ForgotPwdContract.Presenter presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    boolean isEmail = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gt3GeetestUtils.cancelUtils();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_forgot_pwd;
    }

    @Override
    protected void initView() {
    }

    //头部修改
    @Override
    protected void initData() {
        super.initData();
        timeCount = new TimeCount(90000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        new PhoneForgotPresenter(Injection.provideTasksRepository(activity), this);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tvGetCode, R.id.text_other, R.id.tvConfirm,R.id.ivBack})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.text_other:
                if (!isEmail) { // 当前为手机注册，切换为邮箱注册
                    isEmail = true;
                    text_other.setText(getString(R.string.phone_retrieve));
                    text_main.setText(getString(R.string.email_retrieve));
                    tvTag.setText(getString(R.string.email_retrieve));
                    etEmail.setVisibility(View.VISIBLE);
                    etPhone.setVisibility(View.GONE);
                    etCode.setHint(getString(R.string.email_code));
                    etEmail.requestFocus();

                } else {
                    isEmail = false;
                    text_other.setText(getString(R.string.email_retrieve));
                    text_main.setText(getString(R.string.phone_retrieve));
                    tvTag.setText(getString(R.string.phone_retrieve));
                    etEmail.setVisibility(View.GONE);
                    etPhone.setVisibility(View.VISIBLE);
                    etCode.setHint(getString(R.string.phone_code));
                    etPhone.requestFocus();
                }

                break;
            case R.id.tvConfirm:
                doSubmit();
                break;
        }

    }

    /**
     * 提交数据
     */
    private void doSubmit() {
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String code = etCode.getText().toString();
        String password = etPassword.getText().toString();
        String passwordRe = etRenewPassword.getText().toString();
        if (isEmail){
            if (StringUtils.isEmpty(email,code, password, passwordRe)){
                ToastUtils.showToast(getString(R.string.incomplete_information));
                return;
            }
        }else {
            if (StringUtils.isEmpty(phone, code, password, passwordRe)){
                ToastUtils.showToast(getString(R.string.incomplete_information));
                return;
            }
//        if (StringUtils.isEmpty(phone, code, password, passwordRe) || (isEmail && StringUtils.isEmpty( code, password, passwordRe))) {
//            ToastUtils.showToast(getString(R.string.incomplete_information));
        }
        if (!password.equals(passwordRe)) {
            ToastUtils.showToast("两次密码不一致！");
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("password", password);
            map.put("code", code);
            if (isEmail) {
                map.put("account", email);
                map.put("mode", "1");
            } else {
                map.put("account", phone);
                map.put("mode", "0");
            }
            presenter.doForget(map);
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (isEmail) {
            String email = etEmail.getText().toString().trim();
            if (StringUtils.isEmpty(email) || !StringUtils.isEmail(email)) {
                ToastUtils.showToast(R.string.email_diff);
            } else {
                presenter.captch();
            }
        } else {
            String phone = etPhone.getText().toString();
            if (StringUtils.isEmpty(phone) || phone.length() < 11) {
                ToastUtils.showToast(R.string.phone_not_correct);
            } else {
                presenter.captch();
            }
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etPhone.addTextChangedListener(new MyTextWatcher());
        etCode.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        etRenewPassword.addTextChangedListener(new MyTextWatcher());
        etEmail.addTextChangedListener(new MyTextWatcher());
    }

    @Override
    public void setPresenter(ForgotPwdContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void forgotCodeSuccess(String obj) {
        try {
            gt3GeetestUtils.gt3TestFinish();
            timeCount.start();
            tvGetCode.setEnabled(false);
            ToastUtils.showToast(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forgotCodeFail(Integer code, String toastMessage) {
        gt3GeetestUtils.gt3TestClose();
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, null, null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    if (captcha == null) return;
                    String geetest_challenge = captcha.getGeetest_challenge();
                    String geetest_validate = captcha.getGeetest_validate();
                    String geetest_seccode = captcha.getGeetest_seccode();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("geetest_challenge", geetest_challenge);
                    map.put("geetest_validate", geetest_validate);
                    map.put("geetest_seccode", geetest_seccode);
                    if (isEmail) {
                        String email = etEmail.getText().toString().trim();
                        map.put("account", email);
                        presenter.forgotCode(UrlFactory.getEmailForgotPwdCodeUrl(), map);
                    } else {
                        String phone = etPhone.getText().toString().trim();
                        map.put("account", phone);
                        presenter.forgotCode(UrlFactory.getPhoneForgotPwdCodeUrl(), map);
                    }
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void captchFail(Integer code, String toastMessage) {

    }

    @Override
    public void doForgetSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void doForgetFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = etPassword.getText().toString().trim();
            String rePwd = etRenewPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            String etEmai = etEmail.getText().toString().trim();
            if ((StringUtils.isEmpty(password, phone, code,rePwd) && !isEmail) || (StringUtils.isEmpty(password, rePwd, etEmai,code) && isEmail)){
//            if (StringUtils.isEmpty(password, rePwd, phone, code)) {
                tvConfirm.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                tvConfirm.setEnabled(false);
            } else {
                tvConfirm.setBackgroundResource(R.drawable.ripple_btn_global_option_selector);
                tvConfirm.setEnabled(true);
            }
        }
    }

}
