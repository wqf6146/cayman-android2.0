package com.spark.szhb_master.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spark.szhb_master.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCardFragment extends Fragment {

    protected View rootView;

    private int mType;// 0资金账户 1合约全仓账户
    Unbinder unbinder;

    @BindView(R.id.fm_tv_name)
    TextView tvName;

    @BindView(R.id.fm_tv_total)
    TextView tvTotal;

    @BindView(R.id.fm_tv_mtotal)
    TextView tvMTotal;

    public static MyCardFragment newInstance(int type){
        MyCardFragment fragment = new MyCardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);//传参
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_mycard, null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mType = getArguments().getInt("type");
        unbinder = ButterKnife.bind(this, rootView);

        if (mType == 0){
            tvName.setText("资金账户");
        }else{
            tvName.setText("合约全仓账户");
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }
}
