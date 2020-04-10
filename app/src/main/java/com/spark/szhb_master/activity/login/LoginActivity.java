package com.spark.szhb_master.activity.login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.forgot_pwd.ForgotPwdActivity;
import com.spark.szhb_master.activity.main.MainActivity;
import com.spark.szhb_master.activity.signup.SignUpActivity;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Captcha;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.KeyboardUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.EncryUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.CommonUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    public static final int RETURN_LOGIN = 0;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas; @BindView(R.id.tvToRegist)
    TextView tvToRegist;@BindView(R.id.ib_Back)
    ImageButton ibBack;
    @BindView(R.id.name_close)
    ImageView name_close; @BindView(R.id.pass_close)
    ImageView pass_close;
    @BindView(R.id.image_show)
    ToggleButton image_show;
    @BindView(R.id.ivEye)
    ImageView ivEye;
    private LoginContract.Presenter presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private Handler handler = new Handler();


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        ivEye.setTag(false);
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.login));
//        tvGoto.setVisibility(View.VISIBLE);
//        tvGoto.setText(getString(R.string.regist));
    }

    @Override
    protected void initData() {
        super.initData();
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        new LoginPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tvToRegist, R.id.tvForgetPas, R.id.tvLogin, R.id.ivEye,R.id.ib_Back,R.id.name_close,R.id.pass_close})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvToRegist:
                showActivity(SignUpActivity.class, null);
                break;
            case R.id.tvForgetPas:
                showActivity(ForgotPwdActivity.class, null);
                break;
            case R.id.tvLogin:
                String account = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(password)) {
                    //KeyboardUtils.hideSoftInput(activity);
                    KeyboardUtils.hideSoftInput(activity,etPassword);
                    //presenter.captch();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("password", password);
                    map.put("account", account);
                    presenter.login(map);

                } else {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                }
                break;
            case R.id.ivEye:
                boolean isVisible = (boolean) ivEye.getTag();
                if (!isVisible) {
                    isVisible = true;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye_open);
                } else {
                    isVisible = false;
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye_close);
                }
                ivEye.setTag(isVisible);
                break;
            case R.id.ib_Back:
                finish();
                break;
            case R.id.name_close:
                etUsername.setText("");
                break;
            case R.id.pass_close:
                etPassword.setText("");
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etUsername.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());

        image_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });
    }

    @Override

    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(this, null, null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    if (captcha != null) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("password", etPassword.getText().toString().trim());
                        map.put("username", etUsername.getText().toString().trim());
                        map.put("geetest_challenge", captcha.getGeetest_challenge());
                        map.put("geetest_validate", captcha.getGeetest_validate());
                        map.put("geetest_seccode", captcha.getGeetest_seccode());
                        map.put("loginEntry", "APP");
                        presenter.login(map);
                    }
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    protected void onDestroy() {
        gt3GeetestUtils.cancelUtils();
        super.onDestroy();
    }

    @Override
    public void captchFail(Integer code, String toastMessage) {

    }

    @Override
    public void loginSuccess(User obj) {
        //if (obj.getGoogleState() == 1) {
        //    gt3GeetestUtils.gt3TestFinish();
        //    showPopWindow();
        //} else {
            //.gt3TestFinish();
            //gt3GeetestUtils.getDialog().dismiss();
            MainActivity.isAgain = true;
//            String md5Key = CommonUtils.getMd5Key(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
//            SharedPreferenceInstance.getInstance().saveToken(EncryUtils.getInstance().encryptString(md5Key, MyApplication.getApp().getPackageName()));
//            SharedPreferenceInstance.getInstance().saveLockPwd("");
//            SharedPreferenceInstance.getInstance().saveGoogle("close");
            MyApplication.getApp().setCurrentUser(obj);
            setResult(RESULT_OK);
            handler.postDelayed(new Runnable() { // gt3GeetestUtils关闭之后，执行关闭界面操作
                @Override
                public void run() {
                    MobclickAgent.onEvent(LoginActivity.this, "login");
                    ToastUtils.show("登录成功", Toast.LENGTH_SHORT);
                    finish();
                }
            }, 100);
        //}
    }

    @Override
    public void loginFail(Integer code, String toastMessage) {
        //gt3GeetestUtils.gt3TestClose();
        MyApplication.getApp().deleteCurrentUser();
        //SharedPreferenceInstance.getInstance().saveToken("");
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void googleLoginSuccess(User obj) {
        ToastUtils.showToast("success");
        gt3GeetestUtils.gt3TestFinish();
        MainActivity.isAgain = true;
        String md5Key = CommonUtils.getMd5Key(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
        SharedPreferenceInstance.getInstance().saveToken(EncryUtils.getInstance().encryptString(md5Key, MyApplication.getApp().getPackageName()));
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        SharedPreferenceInstance.getInstance().saveGoogle("open");
        MyApplication.getApp().setCurrentUser(obj);
        setResult(RESULT_OK);
        handler.postDelayed(new Runnable() { // gt3GeetestUtils关闭之后，执行关闭界面操作
            @Override
            public void run() {
                MobclickAgent.onEvent(LoginActivity.this, "login");
                popWnd.dismiss();
                ToastUtils.show("登录成功", Toast.LENGTH_SHORT);
                finish();
            }
        }, 100);
    }

    @Override
    public void googleLoginFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveToken("");
        popWnd.dismiss();
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
            String username = etUsername.getText().toString().trim();
            if (StringUtils.isEmpty(password, username)) {
                tvLogin.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                tvLogin.setEnabled(false);
            } else {
                tvLogin.setBackgroundResource(R.drawable.ripple_btn_global_option_selector);
                tvLogin.setEnabled(true);
            }
            if (password.length() == 0){
                pass_close.setVisibility(View.GONE);
            }else {
                pass_close.setVisibility(View.VISIBLE);
            }
        }
    }


    private PopupWindow popWnd;

    private void showPopWindow() {
        //配对PopWindow
        View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.google_dialog, null);
        popWnd = new PopupWindow(LoginActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        popWnd.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        popWnd.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        darkenBackground(0.4f);

        final TextView confirm = contentView.findViewById(R.id.tvGccConfirm);
        LinearLayout line_one = contentView.findViewById(R.id.line_one);
        line_one.setVisibility(View.GONE);
        final EditText google_yzm = contentView.findViewById(R.id.google_yzm);
        ImageView img_close = contentView.findViewById(R.id.img_close);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setEnabled(false);
                String phoneCode = google_yzm.getText().toString();
                HashMap<String, String> map = new HashMap<>();
                map.put("codes", phoneCode);
                map.put("type", "login");
                presenter.googleLogin(map);
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWnd.dismiss();
            }
        });
        View rootview = LayoutInflater.from(LoginActivity.this).inflate(R.layout.activity_login, null);
        popWnd.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }


    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

}
