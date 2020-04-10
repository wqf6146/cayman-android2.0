package com.spark.szhb_master.activity.safe;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkb.vcedittext.VerificationCodeEditText;
import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.GoogleDialog;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.KeyboardUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import config.Injection;
import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * 谷歌验证
 */
public class GoogleCodeActivity extends BaseActivity implements GoogleContract.BindView {
    @BindView(R.id.etCode)
    VerificationCodeEditText etCode;  @BindView(R.id.google_send)
    TextView google_send;@BindView(R.id.tvGccConfirm)
    TextView tvGccConfirm;@BindView(R.id.google_phone)
    TextView google_phone;@BindView(R.id.google_code)
    EditText google_code;@BindView(R.id.line_google)
    LinearLayout line_google;
    private String code;
    private String secret;
    private GoogleContract.BindPresenter presenter;

    private GoogleDialog dialog;
    private int isfirst;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_google_code;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        dialog = new GoogleDialog(this);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.google_authentication_tag));
        tvGoto.setVisibility(View.INVISIBLE);
        new GoogleBindPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            secret = bundle.getString("secret");
            isfirst = bundle.getInt("isfirst");
            if (isfirst == 1) {
                setTitle(getString(R.string.google_authentication_modify));
                line_google.setVisibility(View.INVISIBLE);
            }
        }
        getPhone();

    }




    @Override
    protected void setListener() {
        super.setListener();
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (StringUtils.isNotEmpty(str) && str.length() == 6) {
                    code = str;
                    KeyboardUtils.hideSoftInput(GoogleCodeActivity.this);

                    if (isfirst == 0) {
//                        dialog.show();
                    } else if (isfirst == 1) {
                        dialog.line_two().setVisibility(View.VISIBLE);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("secret", secret);
                        map.put("codes", code);
                        presenter.modifyvAlidate(map);
                    }
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("codes", code);
//                    map.put("secret", secret);
//                    presenter.doBind(map);
                }
            }
        });

        dialog.sendCode().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfirst == 1) {
                    presenter.modifyvcode();
                }
            }
        });
        google_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfirst == 0) {
                    presenter.bindcode();
                }

            }
        });

        dialog.tvConfigm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneCode = dialog.getCode().getText().toString();
                String oldcode = dialog.google_yzm().getText().toString();
                HashMap<String, String> map = new HashMap<>();

                map.put("secret", secret);
                map.put("phoneCode", phoneCode);
                if (isfirst == 1) {
                    map.put("codes", oldcode);
                    presenter.modifyvBind(map);
                }
            }
        });
        tvGccConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String phoneCode = dialog.getCode().getText().toString();
                String phoneCode = google_code.getText().toString();
                String oldcode = dialog.google_yzm().getText().toString();
                HashMap<String, String> map = new HashMap<>();

                map.put("secret", secret);
                map.put("phoneCode", phoneCode);
                if (isfirst == 0) {
                    map.put("codes", code);
                    presenter.doBind(map);
                }
//                else if (isfirst == 1) {
//                    map.put("codes", oldcode);
//                    presenter.modifyvBind(map);
//                }

            }
        });
    }


    @Override
    public void setPresenter(GoogleContract.BindPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doBindSuccess(String obj) {
        SharedPreferenceInstance.getInstance().saveGoogle("open");
        ToastUtils.showToast("绑定成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doBindFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
        dialog.sendCode().setEnabled(true);
    }

    @Override
    public void modifyvAlidateSuccess(String obj) {
        ToastUtils.showToast(obj);
        dialog.show();
    }

    @Override
    public void modifyvAlidateFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void getbindcodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        dialog.sendCode().setEnabled(false);
    }

    @Override
    public void getbindcodeFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
        dialog.sendCode().setEnabled(true);
    }

    @Override
    public void modifyvcodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        dialog.sendCode().setEnabled(false);
    }

    @Override
    public void modifyvcodeFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
        dialog.sendCode().setEnabled(true);
    }

    @Override
    public void modifyvBindSuccess(String obj) {
        ToastUtils.showToast("修改成功");
        SharedPreferenceInstance.getInstance().saveGoogle("open");
        setResult(RESULT_OK);
        finish();
    }

    private String mobile;

    @Override
    public void modifyvBindFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    private void getPhone() {
        post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token",getToken()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
                        mobile = object.getJSONObject("data").getString("mobilePhone");
                        String maskNumber = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
                        google_phone.setText(maskNumber);
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
