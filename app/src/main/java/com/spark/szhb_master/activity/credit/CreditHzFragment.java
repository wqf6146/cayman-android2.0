package com.spark.szhb_master.activity.credit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.ActivityManage;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

public class CreditHzFragment extends BaseFragment implements CreditContract.View  {

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_code)
    EditText etCode;

    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private CreditContract.Presenter presenter;

    public static CreditHzFragment getInstance(int type) {
        CreditHzFragment creditHzFragment = new CreditHzFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        creditHzFragment.setArguments(bundle);
        return creditHzFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hz;
    }

    @Override
    protected void initData() {
        super.initData();
        new CreditPresenter(Injection.provideTasksRepository(getmActivity()), this);
    }

    @Override
    public void doCreditSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void setPresenter(CreditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick({R.id.tv_submit})
    @Override
    protected void setOnClickListener(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                String idCard = etCode.getText().toString().trim();
                String realName = etName.getText().toString().trim();

                if (StringUtils.isEmpty(realName, idCard)) {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                    break;
                }

                HashMap map = new HashMap<>();
                map.put("type", 1);
                map.put("real_name", realName);
                map.put("passport_id", idCard);
                presenter.credit(map);
                break;
        }
    }
}
