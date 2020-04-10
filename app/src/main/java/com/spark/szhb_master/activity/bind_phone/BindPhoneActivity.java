package com.spark.szhb_master.activity.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.country.CountryActivity;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Country;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 绑定手机号和修改手机号界面
 */
public class BindPhoneActivity extends BaseActivity implements BindPhoneContract.View {
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCountry_two)
    TextView tvCountry_two;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvBind)
    TextView tvBind;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChgPhone)
    LinearLayout llChgPhone;
    @BindView(R.id.tvChgAreaCode)
    TextView tvChgAreaCode;
    @BindView(R.id.etChgPhone)
    EditText etChgPhone;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.llbind)
    LinearLayout llbind;
    private Country country;
    private BindPhoneContract.Presenter presenter;
    private TimeCount timeCount;
    private String strAreaCode = "86";
    private boolean isChg;
    private String phone;
    private String zhName = "中国";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CountryActivity.RETURN_COUNTRY:
                if (data == null) return;
                country = (Country) data.getSerializableExtra("country");
                if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
                    tvCountry.setText(country.getZhName());
                    tvCountry_two.setText(country.getZhName());
                } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
                    tvCountry.setText(country.getEnName());
                    tvCountry_two.setText(country.getEnName());
                } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 3) {
                    tvCountry.setText(country.getJpLanguage());
                    tvCountry_two.setText(country.getJpLanguage());
                }
                strAreaCode = country.getAreaCode();
                zhName = country.getZhName();
                if (isChg)
                    tvChgAreaCode.setText("+" + strAreaCode + ">");
                else
                    tvAreaCode.setText("+" + strAreaCode + ">");
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.binding_phone));
        tvGoto.setVisibility(View.INVISIBLE);
        timeCount = new TimeCount(90000, 1000, tvGetCode);
        new BindPhonePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            isChg = bundle.getBoolean("ChgPhone");
//            isChg = bundle.getBoolean("isChgPhone");
            if (isChg) {
                tvPhone.setText(getString(R.string.handset_tail_number) + phone.substring(7, 11) + getString(R.string.receive_message_code));
                llChgPhone.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                tvBind.setText(getString(R.string.change_bind_phone));
                llbind.setVisibility(View.GONE);
                setTitle(getString(R.string.change_phone_num));
            }

        }
    }

    @OnClick({R.id.tvCountry,R.id.tvCountry_two, R.id.tvGetCode, R.id.tvBind})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, CountryActivity.RETURN_COUNTRY);
                break;
            case R.id.tvCountry_two:
                showActivity(CountryActivity.class, null, CountryActivity.RETURN_COUNTRY);
                break;
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvBind:
                bindOrChgPhone();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        //    http://api.cex.wxmarket.cn/uc/mobile/change/new/code ?phone=18133623557&areaCode=86&
        if (isChg) {
//            presenter.sendChangePhoneCode();
            String phone = etChgPhone.getText().toString().trim();
            if (StringUtils.isEmpty(phone) || !StringUtils.isMobile(phone)) {
                ToastUtils.showToast(R.string.phone_not_correct);
            } else {
                HashMap<String, String> map = new HashMap<>();
//                map.put("phone", strAreaCode + phone);
                map.put("phone", phone);
                map.put("areaCode", strAreaCode);
                presenter.sendNewCode(map);
            }
        } else {
            String phone = etPhone.getText().toString().trim();
            if (StringUtils.isEmpty(phone) || !StringUtils.isMobile(phone)) {
                ToastUtils.showToast(R.string.phone_not_correct);
            } else {
                HashMap<String, String> map = new HashMap<>();
//                map.put("phone", strAreaCode + phone);
                map.put("phone", phone);
                presenter.sendCode(map);
            }
        }
    }

    /**
     * 绑定手机号/修改手机号
     */
    private void bindOrChgPhone() {
        if (isChg)
            phone = etChgPhone.getText().toString();
        else
            phone = etPhone.getText().toString();

        String password = etPwd.getText().toString();
        String code = etCode.getText().toString();
        if (StringUtils.isEmpty(password, phone, code)) {
            ToastUtils.showToast(R.string.incomplete_information);
        } else if (!StringUtils.isMobile(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        } else {
            HashMap<String, String> map = new HashMap<>();
//            map.put("phone", strAreaCode + phone);
            map.put("phone", phone);
            map.put("code", code);
            map.put("country", String.valueOf(zhName));
            map.put("password", password);
            if (isChg)
                presenter.newchangePhone(map);
            else
                presenter.bindPhone(map);
        }
    }
        @Override
        public void setPresenter (BindPhoneContract.Presenter presenter){
            this.presenter = presenter;
        }


        @Override
        public void bindPhoneSuccess (String obj){
            ToastUtils.showToast(obj);
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void sendChangePhoneCodeSuccess (String obj){
            ToastUtils.showToast(obj);
            timeCount.start();
            tvGetCode.setEnabled(false);
        }

        @Override
        public void sendNewChangePhoneCodeSuccess (String obj){
            ToastUtils.showToast(obj);
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void changePhoneSuccess (String obj){
            ToastUtils.showToast(obj);
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void sendCodeSuccess (String obj){
            ToastUtils.showToast(obj);
            timeCount.start();
            tvGetCode.setEnabled(false);
        }

        @Override
        public void sendNewCodeSuccess (String obj){
            ToastUtils.showToast(obj);
            timeCount.start();
            tvGetCode.setEnabled(false);
        }

        @Override
        public void doPostFail (Integer code, String toastMessage){
            NetCodeUtils.checkedErrorCode(this, code, toastMessage);
        }
    }
