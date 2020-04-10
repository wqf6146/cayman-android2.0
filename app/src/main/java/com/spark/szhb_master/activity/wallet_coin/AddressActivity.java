package com.spark.szhb_master.activity.wallet_coin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.szhb_master.R;
import com.spark.szhb_master.adapter.AddressAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.entity.Address;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 提币地址
 */
public class AddressActivity extends BaseActivity {
    public static final int RETURN_ADDRESS = 0;
    @BindView(R.id.rvAddress)
    RecyclerView rvAddress;
    private AddressAdapter adapter;
    private ArrayList<Address> addresses;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.mention_money_address));
        tvGoto.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            addresses = (ArrayList<Address>) bundle.getSerializable("addresses");
            initRvAddress();
        }

    }

    private void initRvAddress() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddress.setLayoutManager(manager);
        if (addresses == null) {
            addresses = new ArrayList<>();
        }
        adapter = new AddressAdapter(R.layout.item_address, addresses);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                itemClick(position);
            }
        });
        adapter.bindToRecyclerView(rvAddress);
        if (addresses.size() == 0) {
            View view = LayoutInflater.from(activity).inflate(R.layout.empty_no_message, null);
            TextView textView = view.findViewById(R.id.tvMessage);
            adapter.setEmptyView(view);
            textView.setText(getString(R.string.no_extract_address_tag));
        }
    }

    private void itemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("address", addresses.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void loadData() {

    }

}
