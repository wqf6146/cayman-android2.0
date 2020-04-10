package com.spark.szhb_master.activity.my_match;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.activity.message.WebViewActivity;
import com.spark.szhb_master.activity.wallet.WalletContract;
import com.spark.szhb_master.adapter.MatchAdapter;
import com.spark.szhb_master.base.BaseActivity;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.entity.Coin;
import com.spark.szhb_master.entity.GccMatch;
import com.spark.szhb_master.entity.MyMacth;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.WonderfulStringUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import config.Injection;
import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MatchActivity extends BaseActivity implements MyMatchContract.View {

    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.rlMatch)
    RecyclerView rlMatch;
    @BindView(R.id.tvNum_CGX)
    TextView tvNum_CGX;
    @BindView(R.id.tvKindC)
    TextView tvKindC;
    @BindView(R.id.tvKindA)
    TextView tvKindA;
    @BindView(R.id.tvKindB)
    TextView tvKindB;
    @BindView(R.id.ratioA)
    TextView ratioA;
    @BindView(R.id.ratioC)
    TextView ratioC;
    @BindView(R.id.ratioB)
    TextView ratioB;
    @BindView(R.id.numA)
    TextView numA;
    @BindView(R.id.numB)
    TextView numB;
    @BindView(R.id.numC)
    TextView numC; @BindView(R.id.match_web)
    TextView match_web;
    @BindView(R.id.GCX_his)
    ImageView GCX_his;
    @BindView(R.id.llGca)
    LinearLayout llGca;
    @BindView(R.id.llGcb)
    LinearLayout llGcb;
    @BindView(R.id.llGcc)
    LinearLayout llGcc;

    private MatchAdapter adapter;
    private MyMatchContract.Present presenter;
    private WalletContract.Presenter Wallpresenter;
    private List<MyMacth> list = new ArrayList<>();
    private String unitA,unitB,unitC;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MatchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_match;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    protected void initView() {
        new MatchActivityPresent(Injection.provideTasksRepository(getApplicationContext()), this);
        if (MyApplication.getApp().isLogin()) {
            initRlview();
        } else {
            showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GCX_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchHistory.actionStart(MatchActivity.this);
            }
        });

        llGca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llGca.setEnabled(false);
                checkMatch(unitA);
            }
        });

        llGcb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llGcb.setEnabled(false);
                checkMatch(unitB);
            }
        });

        llGcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llGcc.setEnabled(false);
                checkMatch(unitC);
            }
        });

        match_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "配对规则");
                bundle.putString("url", GlobalConstant.match_url);
                showActivity(WebViewActivity.class, bundle);
            }
        });

    }



    private void initRlview() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rlMatch.setLayoutManager(manager);
        adapter = new MatchAdapter(R.layout.adapter_match, list);
        rlMatch.setAdapter(adapter);

//        presenter.myMatch(getToken());
    }

    @Override
    protected void loadData() {
        if (!WonderfulStringUtils.isEmpty(getToken())) {
//            presenter.myMatch(getToken());
            http();
            hideToLoginView();
        } else showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);;

    }

    private List<Coin> coinsA = new ArrayList<>();
    private List<Coin> coinsB = new ArrayList<>();
    private List<Coin> coinsC = new ArrayList<>();
    private void http() {
        post().url(UrlFactory.getMatchUrl()).addHeader("x-auth-token", getToken()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public boolean onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (!response.equals("")) {
//                        ArrayList<Match> arrayList = new ArrayList<>();
//                        for (Match jsonObject : arrayList) {
//                            arrayList.add(new Gson().fromJson(jsonObject, clazz));
//                        }

//                        List<Match> list =  object.getJSONObject("coins");
//                        for (Match match:list){
//                            String s1 = match.getGCC().getCoin().getName();
//                        }

//                        List<MyMacth.CoinsBean> objs = gson.fromJson(object.getJSONObject("coins").toString(), new TypeToken<List<MyMacth>>() {}.getType());
//                        String str = object.get("gcxMatchBalance").toString();
                        String gcxMatch = object.get("gcxMatchBalance").toString();
                        if (gcxMatch.equals("null") || gcxMatch == null){
                            tvNum_CGX.setText(0 + "GCX");
                        }else {
                            tvNum_CGX.setText(object.get("gcxMatchBalance").toString() + "GCX");
                        }

                        JSONObject jsonObject = object.getJSONObject("coins");
                        unitA = jsonObject.getJSONObject("GCA").getJSONObject("coin").get("unit").toString();
                        unitB = jsonObject.getJSONObject("GCB").getJSONObject("coin").get("unit").toString();
                        unitC = jsonObject.getJSONObject("GCC").getJSONObject("coin").get("unit").toString();
                        tvKindC.setText(jsonObject.getJSONObject("GCC").getJSONObject("coin").get("unit").toString());
                        tvKindA.setText(jsonObject.getJSONObject("GCA").getJSONObject("coin").get("unit").toString());
                        tvKindB.setText(jsonObject.getJSONObject("GCB").getJSONObject("coin").get("unit").toString());

                        double inputRatioC = jsonObject.getJSONObject("GCC").getDouble("inputRatio");
                        double inputRatioB = jsonObject.getJSONObject("GCB").getDouble("inputRatio");
                        double inputRatioA = jsonObject.getJSONObject("GCA").getDouble("inputRatio");

                        if (inputRatioC == 0.5) ratioC.setText("1:2");
                        else ratioC.setText("1:1");
                        if (inputRatioB == 0.5) ratioB.setText("1:2");
                        else ratioB.setText("1:1");
                        if (inputRatioA == 0.5) ratioA.setText("1:2");
                        else ratioA.setText("1:1");

                        numA.setText(jsonObject.getJSONObject("GCA").get("available").toString());
                        numB.setText(jsonObject.getJSONObject("GCB").get("available").toString());
                        numC.setText(jsonObject.getJSONObject("GCC").get("available").toString());

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }



    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(MatchActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MyMatchContract.Present presenter) {
        this.presenter = presenter;
    }


    @Override
    public void myMatchFail(Integer code, String toastMessage) {

    }
    private List<MyMacth> myMacth = new ArrayList<>();

    @Override
    public void myMatchSuccess(String objs) {
        Log.e("TAG", objs);
    }

    private String unitsymbol ;
    private void checkMatch(final String symbol) {
        if (!StringUtils.isEmpty(symbol)){
            presenter.safeSetting();
            unitsymbol = symbol;
        }
    }

    private PopupWindow popWnd;

    private void showPopWindow(final String unit, final double data) {
        //配对PopWindow
        View contentView = LayoutInflater.from(MatchActivity.this).inflate(R.layout.pop_gcc, null);
        popWnd = new PopupWindow(MatchActivity.this);
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
         ImageView img_close = contentView.findViewById(R.id.img_close);
        TextView maxAmount = contentView.findViewById(R.id.tvMaxMatcchAmount);
        TextView maxAmountUnit = contentView.findViewById(R.id.tvMaxAmountUnit);
        TextView matchAmountUnit = contentView.findViewById(R.id.tvMatchAmountUnit);
        final EditText matchAmount = contentView.findViewById(R.id.evMatcchAmount);
        matchAmountUnit.setText(unit);
        maxAmountUnit.setText(unit);
       /* matchAmount.setFocusable(true);
        matchAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager) matchAmount.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);*/

        maxAmount.setText(String.valueOf(data));
        confirm.requestFocus();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWnd.dismiss();
                llGca.setEnabled(true);
                llGcb.setEnabled(true);
                llGcc.setEnabled(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = matchAmount.getText().toString();
                if (!WonderfulStringUtils.isEmpty(amount)) {
                    if (Double.valueOf(amount) > 0 && Double.valueOf(amount) <= data) {
                        presenter.getStartMatch(getToken(),amount,unit);
                        confirm.setEnabled(false);
                    }else {
                        ToastUtils.showToast("请检查配对数量");
                    }
                }else{
                    ToastUtils.showToast("配对数量不能为空");
                }
            }

        });
        View rootview = LayoutInflater.from(MatchActivity.this).inflate(R.layout.activity_match, null);
        popWnd.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
                llGca.setEnabled(true);
                llGcb.setEnabled(true);
                llGcc.setEnabled(true);
            }
        });
    }


    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    @Override
    public void getCheckMatchSuccess(GccMatch obj) {
        showPopWindow(unitsymbol,obj.getData());
    }
    @Override
    public void getCheckMatchFail(Integer code, String toastMessage) {
        //showPopWindow(20);
        ToastUtils.showToast(toastMessage);
        llGca.setEnabled(true);
        llGcb.setEnabled(true);
        llGcc.setEnabled(true);
    }

    @Override
    public void getStartMatchSuccess(String obj) {
        //JSONObject object = new JSONObject(response);
        popWnd.dismiss();
        ToastUtils.showToast("配对成功");
        llGca.setEnabled(true);
        llGcb.setEnabled(true);
        llGcc.setEnabled(true);
    }

    @Override
    public void getStartMatchFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
        llGca.setEnabled(true);
        llGcb.setEnabled(true);
        llGcc.setEnabled(true);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
        llGca.setEnabled(true);
        llGcb.setEnabled(true);
        llGcc.setEnabled(true);
    }

    private SafeSetting safeSetting;
    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        if (obj == null)
            return;
        safeSetting = obj;
        if (safeSetting.getRealVerified() == 0) {
            ShiMingDialog shiMingDialog = new ShiMingDialog(this);
            String name = safeSetting.getRealNameRejectReason();
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {
                    shiMingDialog.setEntrust(7, name,1);
                } else {
                    if (safeSetting.getRealNameRejectReason() != null)
                        shiMingDialog.setEntrust(8, name,1);
                    else
                        shiMingDialog.setEntrust(9, name,1);
                }
            } else {
                shiMingDialog.setEntrust(6, name,1);
            }
            shiMingDialog.show();
            llGca.setEnabled(true);
            llGcb.setEnabled(true);
            llGcc.setEnabled(true);
        }else {
            presenter.getCheckMatch(getToken(),unitsymbol);
        }

    }


}
