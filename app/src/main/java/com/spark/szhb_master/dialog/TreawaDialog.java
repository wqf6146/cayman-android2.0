package com.spark.szhb_master.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.main.presenter.PreheatingPresenter;
import com.spark.szhb_master.activity.main.presenter.TreasureContract;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;

import java.util.HashMap;

import config.Injection;

/**
 * Created by Administrator on 2018/8/22 0022.
 */

public class TreawaDialog extends Dialog implements TreasureContract.PreheatingView {

    private Context context;
    private ImageView img_close;
    private SeekBar bunnleSeekBar;
    private EditText ed_trea_dialog;
    private TextView text_suanli, tv_gcx, tv_frd;
    private TextView text_view, go_up, cancle;
    private TextView name_title, comeone, cometwo, comethree;
    private LinearLayout gcxNum, line_dialog, lin_digg;
    private TextView tvLogin, min_text, max_text;
    private TreasureContract.PreheatingPresenter presenter;
    private int commid, activityId;
    private int min, max, rate_dig;
    private int voteAmount;
    private int comefrom;

    private CallDialog callDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth() * 0.8);
        dialogWindow.setAttributes(lp);
        new PreheatingPresenter(Injection.provideTasksRepository(context.getApplicationContext()), this);
    }


    public TreawaDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setLisener();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_treawa, null);
        setContentView(view);

        img_close = view.findViewById(R.id.img_close);
        bunnleSeekBar = view.findViewById(R.id.bunnleSeekBar);
        text_suanli = view.findViewById(R.id.text_suanli);
        tv_gcx = view.findViewById(R.id.tv_gcx);
        name_title = view.findViewById(R.id.name_title);
        comeone = view.findViewById(R.id.comeone);
        gcxNum = view.findViewById(R.id.gcxNum);
        cometwo = view.findViewById(R.id.cometwo);
        tvLogin = view.findViewById(R.id.tvLogin);
        min_text = view.findViewById(R.id.min_text);
        max_text = view.findViewById(R.id.max_text);
        cometwo = view.findViewById(R.id.cometwo);
        comethree = view.findViewById(R.id.comethree);
        line_dialog = view.findViewById(R.id.line_dialog);
        text_view = view.findViewById(R.id.text_view);
        lin_digg = view.findViewById(R.id.lin_digg);
        go_up = view.findViewById(R.id.go_up);
        cancle = view.findViewById(R.id.cancle);
        tv_frd = view.findViewById(R.id.tv_frd);
        ed_trea_dialog = view.findViewById(R.id.ed_trea_dialog);
//        bunnleSeekBar.setProgress( max >= 100 ? 9 : max);
    }

    public void setEntrust(int come, int id, int activityid, int minn, int maxx, int rate) {
        comefrom = come;
        min = minn;
        max = maxx;
        min_text.setText(min + "");
        max_text.setText(max + "");
        bunnleSeekBar.setProgress(min);
        bunnleSeekBar.setMax(max);
        if (comefrom == 2) {
            name_title.setText(context.getResources().getString(R.string.call));
            comeone.setVisibility(View.INVISIBLE);
            gcxNum.setVisibility(View.GONE);
            cometwo.setVisibility(View.VISIBLE);
            tvLogin.setText(context.getResources().getString(R.string.call_for));
        } else if (comefrom == 1) {
            gcxNum.setVisibility(View.VISIBLE);
            rate_dig = rate;
        }
        activityId = activityid;
        Log.e("TAg", activityid + "qwer" + id);
        commid = id;
//        ((OperateCallback) cont).toRefer();

        text_suanli.setText("-" + minn + " "+context.getResources().getString(R.string.force_value));
        tv_gcx.setText("-" + (minn * rate_dig) + " GCX");
    }

    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String, String> map = new HashMap<>();
        map.put("commodityId", commid + "");
        if (comefrom == 1) {
//            presenter.getCheckJoin(map);//检测挖宝算力
        } else if (comefrom == 2) {
//            presenter.getCheckVote(map);//检测投票GCX
        }
    }

    private void setLisener() {

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bunnleSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                comfrome = 2;
                ed_trea_dialog.setCursorVisible(false);
                return false;
            }
        });

        bunnleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (comefrom == 1) {
                    text_view.setText(i + "");
                    ed_trea_dialog.setText(i + "");
                    if (i == 0){
                        text_suanli.setText("-" + 1 + " "+context.getResources().getString(R.string.force_value));
                        tv_gcx.setText("-" + (1 * rate_dig) + " GCX");
                    }else {
                        text_suanli.setText("-" + i + " "+context.getResources().getString(R.string.force_value));
                        tv_gcx.setText("-" + (i * rate_dig) + " GCX");
                    }
                } else if (comefrom == 2) {
                    text_view.setText(i + "GCX");
                    ed_trea_dialog.setText(i + "");
                }
                voteAmount = i;
                ed_trea_dialog.setSelection(ed_trea_dialog.getText().toString().length());//将光标移至文字末尾
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        tvLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                tvLogin.setEnabled(false);
//                HashMap<String, String> map = new HashMap<>();
//                map.put("commodityId", commid + "");
//                if (comefrom == 1) {
//                    map.put("power", voteAmount + "");
//                    map.put("activityId", activityId + "");
//                    presenter.getJoin(map);
//                } else if (comefrom == 2) {
//                    map.put("voteId", activityId + "");
//                    map.put("voteAmount", voteAmount + "");
//                    presenter.getVote(map);
//                }
//            }
//        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        //邀请好友
        tv_frd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ed_trea_dialog.addTextChangedListener(new MyTextWatcher(0));
        ed_trea_dialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                comfrome = 1;
                ed_trea_dialog.setCursorVisible(true);
                return false;
            }
        });

    }

    private int comfrome;

    public TextView tvbtn() {
        return tvLogin;
    }

    public SeekBar seekBar() {
        return bunnleSeekBar;
    }

    @Override
    public void getVoteListSuccess(Object obj) {

    }

    @Override
    public void getVoteSuccess(int obj) {
        callDialog = new CallDialog(getContext());
        callDialog.setEntrust(voteAmount, obj);
        callDialog.show();
        dismiss();
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {

    }

    @Override
    public void getCheckVoteSuccess(int minzhi, int maxzhi) {
//        min = minzhi;
//        max = maxzhi;
//        min_text.setText(minzhi + "");
//        max_text.setText(maxzhi + "");
//        bunnleSeekBar.setMax(max);
//        if (max == 0) {
//            name_title.setText("提示");
//            comethree.setVisibility(View.VISIBLE);
//            gcxNum.setVisibility(View.GONE);
//            line_dialog.setVisibility(View.GONE);
//            cometwo.setVisibility(View.GONE);
//            comeone.setVisibility(View.GONE);
//            text_view.setVisibility(View.GONE);
//            tvLogin.setVisibility(View.GONE);
//            tv_frd.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public void geCheckVoteFailed(Integer code, String toastMessage) {

    }


    @Override
    public void getCheckJoinSuccess(int minzhi, int maxzhi) {
//        min = minzhi;
//        max = maxzhi;
//        min_text.setText(minzhi + "");
//        max_text.setText(max + "");
//        bunnleSeekBar.setMax(max);
//        if (max == 0) {
//            dismiss();
//            DiggingDialog diggingDialog = new DiggingDialog(context);
//            diggingDialog.setEntrust(1);
//            diggingDialog.show();
//        }else {
//            gcxNum.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void getJoinSuccess(Object obj) {
        DiggingDialog diggingDialog = new DiggingDialog(context);
        diggingDialog.setEntrust(1);
        diggingDialog.show();
        dismiss();
    }

    @Override
    public void doFiled(Integer code, String toastMessage) {
        tvLogin.setEnabled(true);
        ToastUtils.show(toastMessage, Toast.LENGTH_SHORT);
        dismiss();
    }

    @Override
    public void setPresenter(TreasureContract.PreheatingPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void hideLoadingPopup() {
    }

    @Override
    public void displayLoadingPopup() {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    /**
     * 价格，数量监听
     */
    private class MyTextWatcher implements TextWatcher {
        private int intput;

        public MyTextWatcher(int intput) {
            this.intput = intput;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
//            if (intput == 0 && comfrome == 1) {
            bunnleSeekBar.setProgress(StringUtils.isEmpty(ed_trea_dialog.getText().toString()) ? 0 : Integer.parseInt(ed_trea_dialog.getText().toString()));
//            }
            if (!StringUtils.isEmpty(ed_trea_dialog.getText().toString())) {
                if (Integer.parseInt(ed_trea_dialog.getText().toString()) > max) {
                    ed_trea_dialog.setText(max + "");
                }
                if (Integer.parseInt(ed_trea_dialog.getText().toString()) == 0) {
                    ed_trea_dialog.setText("");
                    ed_trea_dialog.setHint("1 ");
                }
//                if (Integer.parseInt(ed_trea_dialog.getText().toString()) < min) {
//                    ed_trea_dialog.setText(min + "");
//                    bunnleSeekBar.setProgress(StringUtils.isEmpty(ed_trea_dialog.getText().toString()) ? 0 : Integer.parseInt(ed_trea_dialog.getText().toString()));
//                }
            }
            ed_trea_dialog.setSelection(ed_trea_dialog.getText().toString().length());//将光标移至文字末尾
        }
    }

}
