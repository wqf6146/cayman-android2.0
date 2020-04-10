package com.spark.szhb_master.activity.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.widget.TimeCount;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 显示手机号码
 */
public class PhoneViewActivity extends BaseActivity implements PhoneViewContract.View{
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etCode)
    EditText etCode;
    private String phone;
    private TimeCount timeCount;
    private PhoneViewContract.Presenter presenter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_chg_phone;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @OnClick({R.id.tvEdit,R.id.tvGetCode})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvEdit:
                if (StringUtils.isEmpty(etCode.getText().toString())){
                    ToastUtils.showToast("验证码不能为空");
                }else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("code", etCode.getText().toString());
                    presenter.findChangePhoneCode(map);


//                showActivity(ChangeLeadActivity.class, getIntent().getExtras(), 0);
                }
                break;
            case R.id.tvGetCode:
                getCode();
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.change_phone_num));
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            String mobile = phone;
            String maskNumber = mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length());
            tvPhone.setText(maskNumber);
        }

        timeCount = new TimeCount(90000, 1000, tvGetCode);
        new PhoneViewPresenter(Injection.provideTasksRepository(getApplicationContext()),this);
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        presenter.sendChangePhoneCode();
    }

    @Override
    public void changePhoneSuccess(String obj) {

    }

    @Override
    public void sendChangePhoneCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void findChangePhoneCodeSuccess(String obj) {
//        ToastUtils.showToast(obj);
        Bundle bundle = getIntent().getExtras();
//                bundle.putBoolean("isChgPhone", true);
        bundle.putBoolean("ChgPhone", true);
        showActivity(BindPhoneActivity.class, bundle, 0);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
    }

    @Override
    public void setPresenter(PhoneViewContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
