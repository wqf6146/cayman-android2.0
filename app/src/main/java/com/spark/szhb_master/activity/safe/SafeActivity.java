package com.spark.szhb_master.activity.safe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.account_pwd.AccountPwdActivity;
import com.spark.szhb_master.activity.edit_login_pwd.EditLoginPwdActivity;
import com.spark.szhb_master.activity.language.LanguageActivity;
import com.spark.szhb_master.dialog.CancleGoogleDialog;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.CopyToast;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

public class SafeActivity extends BaseActivity implements GoogleContract.UnBindView {

//    @BindView(R.id.llGoogleView)
//    LinearLayout llGoogleView;
//    private int googleStatus;
//    private int googlecode;
//    @BindView(R.id.tvGoogle)
//    TextView tvGoogle;
//    @BindView(R.id.google_img)
//    ImageView google_img;
//
//    private CancleGoogleDialog cancleGoogleDialog;
    private GoogleContract.UnBindPresenter presenter;
    private String stauts;


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SafeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SetLockActivity.RETURN_SET_LOCK:

                break;
            case 1:
                if (resultCode == RESULT_OK) {
//                    if (googleStatus == 0) {
//                        googleStatus = 1;
//                    } else {
//                        googleStatus = 0;
//                    }
//                    SharedPreferenceInstance.getInstance().saveGoogleSatus(googleStatus);
//                    tvGoogle.setText(googleStatus == 0 ? R.string.unbound : R.string.bound);
//                    tvGoogle.setEnabled(googleStatus == 0);
                }
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_safe;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
//        cancleGoogleDialog = new CancleGoogleDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();

        setTitle(getString(R.string.my_setting));
        tvGoto.setVisibility(View.INVISIBLE);
//        if (GlobalConstant.isOPenGoogle)
//            llGoogleView.setVisibility(View.VISIBLE);
//        new GoogleUnbindPresenter(Injection.provideTasksRepository(getApplicationContext()), SafeActivity.this);
//        googleStatus = SharedPreferenceInstance.getInstance().getLanguageCode();

//        stauts = SharedPreferenceInstance.getInstance().getGoogle();
//        if (StringUtils.isEmpty(stauts)) return;
//        if (stauts.equals("close")) {
//            googlecode = 0;
//        } else {
//            googlecode = 1;
//        }
//        google_img.setImageResource(googlecode == 0 ? R.mipmap.close_image : R.mipmap.open_img);
//
//        tvGoogle.setText(googleStatus == 0 ? R.string.unbound : R.string.bound);
//        tvGoogle.setEnabled(googleStatus == 0);

        String password = SharedPreferenceInstance.getInstance().getLockPwd();
        if (StringUtils.isEmpty(password)) {
//            button_img.setImageResource(R.mipmap.close_image);
        } else {
//            button_img.setImageResource(R.mipmap.open_img);
        }


    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.safeSetting();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stauts = SharedPreferenceInstance.getInstance().getGoogle();
//        if (!StringUtils.isEmpty(stauts)) {
//            if (stauts.equals("close")) {
//                googlecode = 0;
//            } else {
//                googlecode = 1;
//            }
//            google_img.setImageResource(googlecode == 0 ? R.mipmap.close_image : R.mipmap.open_img);
//        }
    }

    int type = 0;

    @Override
    protected void onResume() {
        super.onResume();
//        button_img.setEnabled(true);
    }

    @Override
    protected void setListener() {
        super.setListener();
//        switchButton.setOnCheckedChangeListener(listener);

//        button_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                button_img.setEnabled(false);
//                String password = SharedPreferenceInstance.getInstance().getLockPwd();
//                if (StringUtils.isEmpty(password)) {
//                    type = 0;
//                } else {
//                    type = 1;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putInt("type", type);
//                showActivity(SetLockActivity.class, bundle, 0);
//            }
//        });

//        cancleGoogleDialog.getTvConfirm().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cancleGoogleDialog.dismiss();
//                showPopWindow();
//            }
//        });
//
//        google_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (stauts.equals("close")) {
//                    if (isbund == 0) {
//                        ToastUtils.showToast(getString(R.string.binding_phone_first));
//                    } else {
//                        showActivity(GoogleAuthorActivity.class, null, 1);
//                    }
//                } else {
//                    getPhone();
//                    cancleGoogleDialog.show();
//                }
//
//            }
//        });
    }

    @OnClick({R.id.lldlmm,R.id.llzjmm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.llGoogle:
//                if (StringUtils.isEmpty(stauts)) return;
//                if (isbund == 0) {
//                    ToastUtils.showToast(getString(R.string.binding_phone_first));
//                } else if (isbund == 1) {
//                    if (stauts.equals("close")) {
//                        showActivity(GoogleAuthorActivity.class, null, 1);
//                    } else if (stauts.equals("open")) {
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("isfirst", 1);
//                        showActivity(GoogleAuthorActivity.class, bundle, 1);
//                    }
//                }
//                if (googlecode == 0) {
//                } else {
//                }
//                break;
            case R.id.lldlmm:
                showActivity(EditLoginPwdActivity.class, null);
                break;
            case R.id.llzjmm:
                showActivity(AccountPwdActivity.class, null);
                break;
        }
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", isChecked ? 0 : 1);
            showActivity(SetLockActivity.class, bundle, 0);
        }
    };


    private PopupWindow popWnd;

    private void showPopWindow() {
        //配对PopWindow
        View contentView = LayoutInflater.from(SafeActivity.this).inflate(R.layout.google_dialog, null);
        popWnd = new PopupWindow(SafeActivity.this);
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
        final TextView google_send = contentView.findViewById(R.id.google_send);
        TextView google_phone = contentView.findViewById(R.id.google_phone);
        final EditText google_code = contentView.findViewById(R.id.google_code);
        final EditText google_yzm = contentView.findViewById(R.id.google_yzm);
        ImageView img_close = contentView.findViewById(R.id.img_close);

        String maskNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        google_phone.setText(maskNumber);

        google_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.unBindCode();
                google_send.setEnabled(false);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setEnabled(false);
                String phoneCode = google_code.getText().toString();//手机验证码
                String googleCode = google_yzm.getText().toString();//手机验证码
                HashMap<String, String> map = new HashMap<>();
                map.put("codes", googleCode);
                map.put("phoneCode", phoneCode);
                presenter.doUnbind(map);//解绑
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWnd.dismiss();
            }
        });


        View rootview = LayoutInflater.from(SafeActivity.this).inflate(R.layout.activity_safe, null);
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

    @Override
    public void setPresenter(GoogleContract.UnBindPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doUnbindSuccess(String obj) {
//        ToastUtils.showToast(obj);
        popWnd.dismiss();
        CopyToast.showText(this, "解绑成功");
        SharedPreferenceInstance.getInstance().saveGoogle("close");
//        google_img.setImageResource(R.mipmap.close_image);
    }

    @Override
    public void doUnbindFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


    @Override
    public void unBindCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
    }

    @Override
    public void unBindCodeFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private int isbund = 0;

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj.getPhoneVerified() == 0) {
            isbund = 0;
        } else {
            isbund = 1;
        }
    }

    @Override
    public void safeSettingFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


    private String mobile;

    private void getPhone() {

        post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token", getToken()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
                        mobile = object.getJSONObject("data").getString("mobilePhone");
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }











}
