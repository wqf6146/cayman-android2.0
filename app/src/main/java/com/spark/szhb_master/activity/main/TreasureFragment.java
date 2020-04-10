package com.spark.szhb_master.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;

import com.spark.szhb_master.activity.Treasure.GetTreaActivity;
import com.spark.szhb_master.activity.Treasure.PowerUpActivity;
import com.spark.szhb_master.activity.Treasure.SuanActivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.main.presenter.TreasureContract;
import com.spark.szhb_master.activity.main.presenter.TreasurePresenterImpl;
import com.spark.szhb_master.activity.message.WebViewActivity;
import com.spark.szhb_master.adapter.TreasureAdapter;
import com.spark.szhb_master.base.BaseFragment;
import com.spark.szhb_master.base.BaseNestingTransFragment;
import com.spark.szhb_master.dialog.LoginDialog;
import com.spark.szhb_master.dialog.ReceiveDialog;
import com.spark.szhb_master.entity.Message;
import com.spark.szhb_master.entity.Treaget;
import com.spark.szhb_master.entity.TreasureInfo;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.utils.CountNumberView;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.MarqueeTextView;
import com.spark.szhb_master.utils.MathUtils;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;


/**
 * Created by Administrator on 2018/9/11 0011.
 */

public class TreasureFragment extends BaseNestingTransFragment implements TreasureContract.View {

    public static final String TAG = TreasureFragment.class.getSimpleName();
    @BindView(R.id.anminal_text)
    CountNumberView anminal_text;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.look_tv)
    TextView look_tv;
    @BindArray(R.array.home_five_top_tab)
    String[] tabs;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.marqueeViewText)
    MarqueeTextView marqueeViewText;
    @BindView(R.id.power_img)
    ImageView power_img;
    @BindView(R.id.gcx_num)
    TextView gcx_num;
    @BindView(R.id.rocket)
    ImageView rocket;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.line_rocket)
    LinearLayout line_rocket;
    @BindView(R.id.trea_share)
    ImageView trea_share;
    @BindView(R.id.rule)
    ImageView rule;
    private int pageNo = 1;

    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llMarket)
    LinearLayout llMarket;
    @BindView(R.id.llTrade)
    LinearLayout llTrade; @BindView(R.id.lin_gcx)
    LinearLayout lin_gcx; @BindView(R.id.btn_Login)
    Button btn_Login;
    private LinearLayout[] lls;
    private int count = 0;

    private DiggingFragment diggingFragment;
    private PreheatingFragment preheatingFragment;
    private MydiggingFragment mydiggingFragment;
    private TreasureAdapter adapter;
    private TreasureContract.TreasurePresenter presenter;
    private List<Message> messageList = new ArrayList<>();
    private List<Treaget> treaget = new ArrayList<>();
    private boolean isfirst = false;
    private List<String> info = new ArrayList<>();
    private ReceiveDialog receiveDialog;
    private User user;
    private LoginDialog loginDialog;
    private int come_from = 0;

    @Override
    protected void initData() {
        super.initData();
        if (((MainActivity) getActivity()).getSavedInstanceState() != null) recoveryFragments();
        new TreasurePresenterImpl(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        receiveDialog = new ReceiveDialog(getContext());
        user = MyApplication.getApp().getCurrentUser();
        if (fragments != null && fragments.size() > 0)
            selecte(llHome, 0);
        loginDialog = new LoginDialog(getContext());
        setVIsible();
    }

    private void setVIsible() {
        if (MyApplication.getApp().isLogin()) {
            line_rocket.setVisibility(View.VISIBLE);
            lin_gcx.setVisibility(View.VISIBLE);
            btn_Login.setVisibility(View.GONE);
        }else {
            line_rocket.setVisibility(View.GONE);
            lin_gcx.setVisibility(View.INVISIBLE);
            btn_Login.setVisibility(View.VISIBLE);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (fragments != null && fragments.size() > 0)
                selecte(llHome, 0);
        }
    };

    @Override
    protected void initView() {
        isfirst = true;
    }


    @OnClick({R.id.look_tv, R.id.line_rocket, R.id.llHome, R.id.llMarket, R.id.llTrade, R.id.trea_share, R.id.rule,R.id.btn_Login})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.look_tv:
                showActivity(GetTreaActivity.class, null);
                break;
            case R.id.line_rocket:
                if (!MyApplication.getApp().isLogin()) {
                    loginDialog.show();
                } else {
                    showActivity(PowerUpActivity.class, null);
                }
                break;
            case R.id.llHome:
                selecte(v, 0);
                break;
            case R.id.llMarket:
                selecte(v, 1);
                break;
            case R.id.llTrade:
                if (!MyApplication.getApp().isLogin()) {
                    loginDialog.show();
                } else {
                    selecte(v, 2);
                }
                break;
            case R.id.trea_share:
                if (!MyApplication.getApp().isLogin()) {
                    loginDialog.show();
                } else {
                    MobclickAgent.onEvent(getContext(), "share");
                    showActivity(SuanActivity.class, null);
                }
                break;
            case R.id.rule:
//                startActivity(new Intent(getContext(), RuleAcitivity.class));
                Bundle bundle = new Bundle();
                bundle.putString("title", "攻略");
                bundle.putString("url", GlobalConstant.rule_url);
                showActivity(WebViewActivity.class, bundle);
                break;
            case R.id.btn_Login:
                startActivity(new Intent(getContext(),LoginActivity.class));
                break;
        }
    }

    private void selecte(View v, int page) {
        llHome.setSelected(false);
        llMarket.setSelected(false);
        llTrade.setSelected(false);
        v.setSelected(true);
        showFragment(page);
    }

    private BaseFragment currentFragment;

    private void showFragment(int page) { // 用这种需要时才添加fragment的方式，避免一次性将fragment添加到内存中，造成界面卡顿
        BaseFragment fragment = fragments.get(page);
        if (currentFragment == fragment) return;
        currentFragment = fragment;
        hideFragments();
        if (!fragment.isAdded()) {
            String tag = fragment.getTag();
            if (page == 0) {
                tag = DiggingFragment.class.getSimpleName();
            } else if (page == 1) {
                tag = PreheatingFragment.class.getSimpleName();
            } else {
                tag = MydiggingFragment.class.getSimpleName();
            }
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.flContainer, fragment, tag).commit();
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.show(fragment).commit();
    }

    private void hideFragments() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (!fragments.get(i).isHidden() && currentFragment != fragments.get(i)) {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    @Override
    protected void loadData() {
        super.loadData();
//        if (MyApplication.getApp().isLogin()) {
        llHome.setClickable(true);
        llMarket.setClickable(true);
        llTrade.setClickable(true);
        flContainer.setVisibility(View.VISIBLE);
        getMarqueeText();
        presenter.getTreaInfo(); // 获取GCX与算力值
        come_from = 0;
//        }
//        GuidelinesDialog guidelinesDialog = new GuidelinesDialog(getContext());
//        guidelinesDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getApp().isLogin()) {
            presenter.getTreaInfo(); // 获取GCX与算力值
            come_from = 1;
        }
        int msg = SharedPreferenceInstance.getInstance().getMSG();
        if (msg == 1){
            selecte(llMarket,1);
        }
        SharedPreferenceInstance.getInstance().saveMSG(0);
        setVIsible();
    }

    /**
     * 获取滚动text
     */
    private void getMarqueeText() {
        presenter.getRewardList();
    }

    @Override
    protected void setListener() {
        super.setListener();
        receiveDialog.tvbtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecte(llTrade, 2);
                receiveDialog.dismiss();
            }
        });
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.treasure_fragment;
    }

    @Override
    protected void addFragments() {
        if (diggingFragment == null) fragments.add(diggingFragment = new DiggingFragment());
        if (preheatingFragment == null)
            fragments.add(preheatingFragment = new PreheatingFragment());
        if (mydiggingFragment == null) fragments.add(mydiggingFragment = new MydiggingFragment());
    }


    @Override
    protected void recoveryFragments() {
        diggingFragment = (DiggingFragment) getChildFragmentManager().findFragmentByTag(DiggingFragment.class.getSimpleName());
        preheatingFragment = (PreheatingFragment) getChildFragmentManager().findFragmentByTag(PreheatingFragment.class.getSimpleName());
        mydiggingFragment = (MydiggingFragment) getChildFragmentManager().findFragmentByTag(MydiggingFragment.class.getSimpleName());
        fragments.add(diggingFragment);
        fragments.add(preheatingFragment);
        fragments.add(mydiggingFragment);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setPresenter(TreasureContract.TreasurePresenter presenter) {
        this.presenter = presenter;
    }

    private void setMarqueeView(List<Treaget> messageList) {
        info.clear();
        for (Treaget message : messageList) {
            info.add("恭喜" + message.getLuckUsername() + "在" + message.getActivityTitle() + "获得" + message.getCommodityName() + "        ");
        }
        if (messageList.size() != 0) {
            String str = info.toString();
            str = str.substring(1, str.length());
            str = str.replace(",", " ").replace("[", "").replace("]", "");
            marqueeViewText.setText(str);
        }
    }

    @Override
    public void getRewardListSuccess(List<Treaget> messages) {
        treaget.clear();
        treaget.addAll(messages);
        setMarqueeView(treaget);
    }

    private double num_pw;
    private int gcx_nu;
    private int pward;

    @Override
    public void getTreaInfoSuccess(TreasureInfo treasureInfo) {

        pward = treasureInfo.getUnReceiveRewardAmount();
        if (pward != 0 && isfirst && count == 0 && come_from == 0) {
            receiveDialog.setEntrust("");
            receiveDialog.show();
            count = 1;
        }
        num_pw = treasureInfo.getAvailablePower();
        anminal_text.showNumberWithAnimation(Float.parseFloat(String.valueOf(MathUtils.getRundNumber(num_pw, 2, null))), CountNumberView.FLOATREGEX);
        gcx_nu = (int) Math.floor(treasureInfo.getGcxAmount());
        gcx_num.setText("" + (int) Math.floor(treasureInfo.getGcxAmount()));
        SharedPreferenceInstance.getInstance().saveGCX((int) Math.floor(treasureInfo.getGcxAmount()), (int) num_pw);
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
        if (code == 4000) {
//            ToastUtils.show(MyApplication.getApp().getResources().getString(R.string.login_invalid), Toast.LENGTH_SHORT);
            MyApplication.getApp().deleteCurrentUser();
        }
        if (StringUtils.isEmpty(toastMessage)) {
            ToastUtils.show("未知错误", Toast.LENGTH_SHORT);
        }
    }

    public void referfragment(int postion) {
        if (MyApplication.getApp().isLogin())
            presenter.getTreaInfo();
    }
}
