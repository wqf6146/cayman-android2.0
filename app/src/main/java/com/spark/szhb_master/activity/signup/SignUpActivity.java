package com.spark.szhb_master.activity.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.country.CountryActivity;
import com.spark.szhb_master.activity.message.WebViewActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Captcha;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * Created by Administrator on 2018/2/2.
 */

public class SignUpActivity extends BaseActivity implements SignUpContract.View {
    public static final String TAG = SignUpActivity.class.getSimpleName();
    public static String token = "";
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etUsername)
    EditText etUsername;
//    @BindView(R.id.etCode)
//    EditText etCode;
    @BindView(R.id.etPromoCode)
    EditText etPromoCode; // 推广码
    @BindView(R.id.etPassword)
    EditText etPassword;
//    @BindView(R.id.tvCountry)
//    TextView tvCountry;
//    @BindView(R.id.tvGetCode)
//    TextView tvGetCode;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.etRenewPassword)
    EditText etRenewPassword;
//    @BindView(R.id.llGetCode)
//    LinearLayout llGetCode;
//    @BindView(R.id.ivGetCode)
//    ImageView ivGetCode;
//    @BindView(R.id.img_singup)
//    ImageView img_singup;
    @BindView(R.id.text_xieyi)
    TextView text_xieyi;
    @BindView(R.id.text_main)
    TextView text_main;
    @BindView(R.id.text_other)
    TextView text_other;
    @BindView(R.id.singup_login)
    TextView singup_login;
    private Country country;
    private TimeCount timeCount;
    private SignUpContract.Presenter presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    boolean isEmail = false;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CountryActivity.RETURN_COUNTRY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                country = (Country) data.getSerializableExtra("country");
                //tvCountry.setText(CommonUtils.getCountryNameByLanguageCode(country));
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        gt3GeetestUtils.cancelUtils();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initView() {
        super.initView();
    }


    @Override
    protected void initData() {
        super.initData();
        //timeCount = new TimeCount(90000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        new SignUpPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }


    // R.id.img_singup,R.id.tvGetCode, R.id.tvCountry,
    @OnClick({ R.id.tvSignUp, R.id.text_other, R.id.text_xieyi, R.id.ivBack, R.id.singup_login})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.tvGetCode:
//                getCode();
//                break;
            case R.id.singup_login:
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSignUp:
                signUp();
                break;
//            case R.id.tvCountry:
//                showActivity(CountryActivity.class, null, 0);
//                break;
            case R.id.text_other:
//                etPromoCode.setText("");
//                etPromoCode.setHint(getString(R.string.promotion_code));
//                etUsername.setText("");
//                etUsername.setHint(getString(R.string.user_name));
//                etPassword.setText("");
//                etPassword.setHint(getString(R.string.password));
                if (!isEmail) { // 当前为手机注册，切换为邮箱注册
                    isEmail = true;
                    text_other.setText(getString(R.string.phone_sign_up));
                    text_main.setText(getString(R.string.email_sign_up));
                    etEmail.setVisibility(View.VISIBLE);
                    etPhone.setVisibility(View.GONE);
                    //llGetCode.setVisibility(View.GONE);
                    //ivGetCode.setVisibility(View.GONE);
                    etEmail.requestFocus();
                } else {
                    isEmail = false;
                    text_other.setText(getString(R.string.email_sign_up));
                    text_main.setText(getString(R.string.phone_sign_up));
                    etEmail.setVisibility(View.GONE);
                    etPhone.setVisibility(View.VISIBLE);
                    //llGetCode.setVisibility(View.VISIBLE);
                    //ivGetCode.setVisibility(View.VISIBLE);
                    etPhone.requestFocus();
                }
                break;
//            case R.id.img_singup:
//                if (count == 0) {
//                    count = 1;
//                    img_singup.setImageResource(R.mipmap.singup_true);
//                } else if (count == 1) {
//                    count = 0;
//                    img_singup.setImageResource(R.mipmap.singup_false);
//                }
////                findisEnabled();
//                break;
            case R.id.text_xieyi:
                Bundle bundle = new Bundle();
                bundle.putString("title", "用户协议");
                bundle.putString("url", GlobalConstant.register_url);
                showActivity(WebViewActivity.class, bundle);
                break;

        }
    }

    private int count = 0;

    private void signUp() {
        String phone = etPhone.getText().toString();
//        String username = etUsername.getText().toString();
        String username = "InitUsername";
        //String code = etCode.getText().toString();
        String password = etPassword.getText().toString();
        String etRenewPass = etRenewPassword.getText().toString();
        //String country = tvCountry.getText().toString();
        String email = etEmail.getText().toString().trim();
        if ((StringUtils.isEmpty(phone,  username, password, etRenewPass) && !isEmail) || (isEmail && StringUtils.isEmpty(email, username, password, etRenewPass))) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (isEmail && !StringUtils.isEmail(email)) {
            ToastUtils.showToast(R.string.email_not_correct);
        } else if(password.length() < 8) {
            ToastUtils.showToast("密码格式为8至12位");
        } else if (!password.equals(etRenewPass)) {
            ToastUtils.showToast("两次密码不一致！");
        } else  {
            HashMap<String, String> map = new HashMap<>();
            if (!isEmail) {
//                map.put("username", username);
                //map.put("username", "InitUsername");
                map.put("password", password);
                //map.put("country", country);
                map.put("invite_code", etPromoCode.getText().toString().trim());
                map.put("account", phone);
                //map.put("code", code);
                presenter.sighUp(UrlFactory.getSignUpByPhone(), map);
            } else {
                presenter.captch();
            }
        }
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        String phone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phone) || !StringUtils.isMobile(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        } else {
            presenter.captch();
        }
    }


    @Override
    protected void loadData() {
    }

    @Override
    protected void setListener() {
        super.setListener();
        etPhone.addTextChangedListener(new MyTextWatcher());
        //etCode.addTextChangedListener(new MyTextWatcher());
        etUsername.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        etRenewPassword.addTextChangedListener(new MyTextWatcher());
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        this.presenter = presenter;
    }

   // @Override
    public void codeSuccess(String obj) {
        try {
            ToastUtils.showToast(obj);
            gt3GeetestUtils.gt3TestFinish();
            timeCount.start();
            //tvGetCode.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void codeFail(Integer code, String toastMessage) {
        gt3GeetestUtils.gt3TestClose();
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    //@Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, UrlFactory.getCaptchaUrl(), null, null, new GT3GeetestBindListener() {
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
                    if (isEmail) { // 邮箱登录
//                        map.put("username", etUsername.getText().toString().trim());
                        map.put("username", "InitUsername");
                        map.put("password", etPassword.getText().toString().trim());
                        //map.put("country", tvCountry.getText().toString());
                        map.put("promotion", etPromoCode.getText().toString().trim());
                        map.put("email", etEmail.getText().toString().trim());
                        presenter.sighUp(UrlFactory.getSignUpByEmail(), map);
                    } else {
                        String phone = etPhone.getText().toString().trim();
                        //String country = tvCountry.getText().toString();
                        map.put("phone", phone);
                        //map.put("country", country);
                        presenter.getCode(map);
                    }
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

   // @Override
    public void captchFail(Integer code, String toastMessage) {

    }

    @Override
    public void sighUpSuccess(String obj) {
        if (isEmail)
            gt3GeetestUtils.gt3TestFinish();
        MobclickAgent.onEvent(SignUpActivity.this, "register");
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void sighUpFail(Integer code, String toastMessage) {
        if (isEmail)
            gt3GeetestUtils.gt3TestClose();
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
            password = etPassword.getText().toString().trim();
            rePwd = etRenewPassword.getText().toString().trim();
            username = etUsername.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            //code = etCode.getText().toString().trim();
//            if ((StringUtils.isEmpty(password, username, phone, code,rePwd) && !isEmail) || (StringUtils.isEmpty(password, username, email) && isEmail)) {
            findisEnabled();
        }
    }

    //code
    private String password, rePwd, username, phone, email ;

    private void findisEnabled() {
        if ((StringUtils.isEmpty(password, phone, rePwd) && !isEmail) || (StringUtils.isEmpty(password, email) && isEmail)) {
            tvSignUp.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
            tvSignUp.setEnabled(false);
        }

//        else if (count == 1){
//            tvSignUp.setBackgroundResource(R.drawable.ripple_btn_global_option_selector);
//            tvSignUp.setEnabled(true);
//        }
        else {
            tvSignUp.setBackgroundResource(R.drawable.ripple_btn_global_option_selector);
            tvSignUp.setEnabled(true);
        }
    }
}
