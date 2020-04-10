package com.spark.szhb_master.activity.wallet_coin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.szhb_master.R;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Address;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.ExtractInfo;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.NetCodeUtils;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 提币
 */
public class ExtractActivity extends BaseActivity implements ExtractContract.View {
    @BindView(R.id.tvBuyCanUse)
    TextView tvCanUse;
    @BindView(R.id.tvCanUseUnit)
    TextView tvUnit1;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.etServiceFee)
    EditText etServiceFee;
    @BindView(R.id.tvGetUnit)
    TextView tvUnit2;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvCollectUnit)
    TextView tvUnit3;
    @BindView(R.id.tvExtract)
    TextView tvExtract;
    @BindView(R.id.tvFinalCount)
    TextView tvFinalCount;
    @BindView(R.id.remarks)
    TextView remarks;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private Coin coin;
    private ExtractInfo extractInfo;
    private ExtractContract.Presenter presenter;
    private MyTextWatcher myTextWatcher;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddressActivity.RETURN_ADDRESS) {
            if (resultCode == RESULT_OK && data != null) {
                etAddress.setText(((Address) data.getSerializableExtra("address")).getAddress());
            }
        }
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        myTextWatcher = new MyTextWatcher();
        new ExtractPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        tvGoto.setVisibility(View.INVISIBLE);
        if (bundle != null) {
            coin = (Coin) bundle.getSerializable("coin");
            if (coin != null) {
                Coin.CoinBean coinBean = coin.getCoin();
                tvTitle.setText(coinBean.getUnit() + getString(R.string.mention_money));
                tvUnit1.setText(coinBean.getUnit());
                tvUnit2.setText(coinBean.getUnit());
                tvUnit3.setText(coinBean.getUnit());
                tvCanUse.setText(coin.getBalance() + "");
            }
        }
    }

    @OnClick({R.id.tvSelectAdress, R.id.tvExtract, R.id.tvAll})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSelectAdress: // 选择地址
                if (extractInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("addresses", extractInfo.getAddresses());
                    showActivity(AddressActivity.class, bundle,AddressActivity.RETURN_ADDRESS);
                } else {
                    ToastUtils.showToast(getString(R.string.str_address_prompt));
                }
                break;
            case R.id.tvExtract: // 提币
                checkInput();
                break;
            case R.id.tvAll: // 全部
                if (extractInfo != null) {
                    etCount.setText(extractInfo.getBalance() + "");
                } else {
                    ToastUtils.showToast(getString(R.string.str_address_prompt));
                }
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etCount.addTextChangedListener(myTextWatcher);
        etServiceFee.addTextChangedListener(myTextWatcher);
    }

    @Override
    protected void loadData() {
        presenter.extractinfo();
    }


    @Override
    public void setPresenter(ExtractContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * 检查输入
     */
    private void checkInput() {
        String address = etAddress.getText().toString();
        String unit = extractInfo.getUnit();
        String amount = etCount.getText().toString();
        String fee = etServiceFee.getText().toString();


        if (StringUtils.isEmpty(address, unit, amount, fee)) {
            ToastUtils.showToast(R.string.incomplete_information);
            return;
        } else if (Double.valueOf(fee) < extractInfo.getMinTxFee() || Double.valueOf(fee) > extractInfo.getMaxTxFee()) {
            ToastUtils.showToast(getString(R.string.fee_limit_tag) + extractInfo.getMinTxFee() + "~" + extractInfo.getMaxTxFee());
        }else if (Double.valueOf(amount) < extractInfo.getMinAmount()){
            ToastUtils.showToast("提币数量不得小于"+ new BigDecimal(String.valueOf(extractInfo.getMinAmount())).toString());
        }else {
            String jyPassword = etPassword.getText().toString();
            String remark = remarks.getText().toString();
            doExtract(unit, amount, fee, remark, jyPassword, address);
        }
    }

    /**
     * 提币
     *
     * @param unit
     * @param amount
     * @param fee
     * @param remark
     * @param jyPassword
     * @param address
     */
    private void doExtract(String unit, String amount, String fee, String remark, String jyPassword, String address) {
        HashMap<String, String> map = new HashMap<>();
        map.put("jyPassword", jyPassword);
        map.put("unit", unit);
        map.put("amount", amount);
        map.put("fee", fee);
        map.put("address", address);
        map.put("doFeedBack", remark);
        presenter.extract(map);
    }


    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void extractinfoSuccess(List<ExtractInfo> obj) {
        if (obj != null) {
            for (ExtractInfo extractInfo : obj) {
                if (coin.getCoin().getUnit().equals(extractInfo.getUnit())) {
                    this.extractInfo = extractInfo;
                    break;
                }
            }
        }
        if (extractInfo != null) {
            fillView();
        }
    }

    private void fillView() {
        tvCanUse.setText(extractInfo.getBalance() + "");
        etCount.setHint(getString(R.string.min_coin_num_tag) + new BigDecimal(String.valueOf(extractInfo.getMinAmount())).toString());
        if (extractInfo.getMinTxFee() == extractInfo.getMaxTxFee()) {
            etServiceFee.setText(new BigDecimal(String.valueOf(extractInfo.getMaxTxFee())).toString());
            etServiceFee.setEnabled(false);
        } else
            etServiceFee.setHint(new BigDecimal(String.valueOf(extractInfo.getMinTxFee())).toString()
                    + " ~ " + new BigDecimal(String.valueOf(extractInfo.getMaxTxFee())).toString());
    }

    @Override
    public void extractSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
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
            String countStr = etCount.getText().toString();
            String serviceStr = etServiceFee.getText().toString();
            if (StringUtils.isEmpty(countStr, serviceStr)) return;
            double count = Double.parseDouble(countStr);
            double service = Double.parseDouble(serviceStr);
            double finalCount = count - service;
            if (finalCount < 0) finalCount = 0;
            tvFinalCount.setText(MathUtils.getRundNumber(finalCount, 4, null));
        }
    }
}
