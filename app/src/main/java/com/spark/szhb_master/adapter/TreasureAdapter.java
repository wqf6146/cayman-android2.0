package com.spark.szhb_master.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.spark.szhb_master.MyApplication;
import com.spark.szhb_master.R;
import com.spark.szhb_master.activity.Treasure.RuleAcitivity;
import com.spark.szhb_master.activity.login.LoginActivity;
import com.spark.szhb_master.dialog.LoginDialog;
import com.spark.szhb_master.dialog.ShiMingDialog;
import com.spark.szhb_master.dialog.TimeDialog;
import com.spark.szhb_master.dialog.TreawaDialog;
import com.spark.szhb_master.dialog.UpPowerDialog;
import com.spark.szhb_master.entity.Preheatinginfo;
import com.spark.szhb_master.entity.SafeSetting;
import com.spark.szhb_master.entity.User;
import com.spark.szhb_master.factory.UrlFactory;
import com.spark.szhb_master.utils.DateUtils;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.SharedPreferenceInstance;
import com.spark.szhb_master.utils.StringUtils;
import com.spark.szhb_master.utils.ToastUtils;
import com.spark.szhb_master.utils.WonderfulStringUtils;
import com.spark.szhb_master.utils.okhttp.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

import static com.spark.szhb_master.utils.GlobalConstant.JSON_ERROR;
import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/12 0012.
 */

public class TreasureAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Preheatinginfo> groupList = new ArrayList<>();
    private List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList = new ArrayList<>();
    private TreawaDialog treawaDialog;
    GroupViewHolder holder;
    private int activityid;
    private Preheatinginfo listBean;
    private long time_end;
    private long typeTime;

    public TreasureAdapter(Context context, List<Preheatinginfo> groupList, List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList) {
        this.context = context;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new GroupViewHolder();
            view = view.inflate(context, R.layout.treasure_group, null);

            holder.rule = view.findViewById(R.id.rule);
            holder.fen_line = view.findViewById(R.id.fen_line);
            holder.tv_miaosha_shi = view.findViewById(R.id.tv_miaosha_shi);
            holder.tv_miaosha_minter = view.findViewById(R.id.tv_miaosha_minter);
            holder.tv_miaosha_second = view.findViewById(R.id.tv_miaosha_second);
            holder.start_end = view.findViewById(R.id.start_end);
            holder.time_left = view.findViewById(R.id.time_left);
            holder.is_open = view.findViewById(R.id.is_open);
            holder.lltime = view.findViewById(R.id.lltime);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.tv_new_title = view.findViewById(R.id.tv_new_title);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }

//        if (i == 0) {
//            holder.rule.setVisibility(View.VISIBLE);
//            holder.rule.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.rule));
//        } else {
//            holder.rule.setVisibility(View.INVISIBLE);
//            holder.fen_line.setVisibility(View.VISIBLE);
//        }

        holder.rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RuleAcitivity.class).putExtra("num", 1));
            }
        });

        if (b) {
            holder.is_open.setImageResource(R.mipmap.open_blue);
        } else {
            holder.is_open.setImageResource(R.mipmap.shouqi_blue);
        }

        listBean = groupList.get(i);
        if (!StringUtils.isEmpty(listBean.getEndTime())) {//结束时间不为空
            time_end = DateUtils.getTimeMillis(null, listBean.getEndTime());
        } else {
        }
        String str = listBean.getEndTime();
        long time_start = DateUtils.getTimeMillis(null, listBean.getStartTime());
        long now_time = DateUtils.getSimpleDateFormat();
        if (now_time < time_start) {//小于开始时间
            holder.tv_title.setVisibility(View.GONE);
            holder.start_end.setText("后开始");
            time_end = time_start;
            typeTime = time_start - now_time;
            holder.tv_new_title.setVisibility(View.VISIBLE);
            holder.tv_new_title.setText(listBean.getTitle());
//          后开始
        } else if (now_time >= time_start) {//大于开始时间
            if (StringUtils.isEmpty(listBean.getEndTime())) {//结束时间为空
                holder.start_end.setVisibility(View.GONE);
                holder.lltime.setVisibility(View.GONE);

                holder.tv_new_title.setVisibility(View.GONE);

                holder.tv_title.setVisibility(View.VISIBLE);
                holder.tv_title.setText(listBean.getTitle());
            } else {
                holder.tv_new_title.setVisibility(View.VISIBLE);
                holder.tv_new_title.setText(listBean.getTitle());

                holder.tv_title.setVisibility(View.GONE);
                holder.start_end.setVisibility(View.VISIBLE);

                holder.lltime.setVisibility(View.VISIBLE);

                holder.start_end.setText("后结束");
            }
        }

        countDown(time_end);

//        handler.sendEmptyMessage((int) time_end);老版本
//        Log.e("TAG",time_end + "             "+ i);//获得序号i
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        final ChildViewHolder holder;
        if (view == null) {
            holder = new ChildViewHolder();
            view = view.inflate(context, R.layout.treasure_child, null);
            holder.tv_num = view.findViewById(R.id.tvNum);
            holder.lin_gray = view.findViewById(R.id.lin_gray);
            holder.trea_btn = view.findViewById(R.id.trea_btn);
            holder.comm_name = view.findViewById(R.id.comm_name);
            holder.tvLimit = view.findViewById(R.id.tvLimit);
            holder.image_child = view.findViewById(R.id.image_child);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }

        if (i1 % 2 == 0) {
            holder.lin_gray.setBackgroundColor(context.getResources().getColor(R.color.grgray));
        } else {
            holder.lin_gray.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        final Preheatinginfo.ActivityCommodityInfoListBean datasBean = childList.get(i).get(i1);
        if (!WonderfulStringUtils.isEmpty(datasBean.getCover()))
            Glide.with(context).load(datasBean.getCover())
                    .into(holder.image_child);

        String html = context.getResources().getString(R.string.completion)+"：" + "<font color=#ff6e00>" + datasBean.getCompleteAmount() + "</font>" + "/" + datasBean.getDegree() + " ";
        holder.tvLimit.setText(Html.fromHtml(html));

        holder.tv_num.setText(context.getResources().getString(R.string.number_participants)+"：" + datasBean.getJoinAmount());
        if (datasBean.getCompleteAmount() == datasBean.getDegree()) {
            holder.trea_btn.setBackground(context.getResources().getDrawable(R.mipmap.nojoin_btn));
            holder.trea_btn.setText("等待结果");
            holder.trea_btn.setEnabled(false);
        } else {
            holder.trea_btn.setBackground(context.getResources().getDrawable(R.mipmap.trea_btn));
            holder.trea_btn.setText(context.getResources().getString(R.string.start_digging));
            holder.trea_btn.setEnabled(true);
        }
        holder.comm_name.setText(datasBean.getCommodityName());
//        activityid = listBean.getActivityId();
        holder.trea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                User user = MyApplication.getApp().getCurrentUser();
                if (!MyApplication.getApp().isLogin() || StringUtils.isEmpty(user.getNick_name())){
                    LoginDialog loginDialog = new LoginDialog(context);
                    loginDialog.show();
                } else {
                    long time_start = DateUtils.getTimeMillis(null, groupList.get(i).getStartTime());
                    long now_time = DateUtils.getSimpleDateFormat();
                    int type_two = (now_time > time_start ? 1 : 2);
                    int suan = SharedPreferenceInstance.getInstance().getSUAN();
                    int gcx = SharedPreferenceInstance.getInstance().getGCX();
                    TimeDialog timeDialog = new TimeDialog(context);
                    UpPowerDialog upPowerDialog = new UpPowerDialog(context);
                    if (type_two == 2) { //2 未开始
                        String str = DateUtils.getSFM(typeTime);
                        timeDialog.setEntrust(str,3);
                        timeDialog.show();
                    } else if (suan == 0) {
                        upPowerDialog.setEntrust(1);
                        upPowerDialog.show();
                    } else if (gcx == 0) {
                        upPowerDialog.setEntrust(2);
                        upPowerDialog.show();
                    } else {
                        onClickMyTextView.myTextViewClick(i1, datasBean.getActivityCommodityId(), groupList.get(i).getActivityId());
                    }
                }
            }
        });
        return view;
    }

    private onClickMyTextView onClickMyTextView;

    public interface onClickMyTextView {
        public void myTextViewClick(int i, int commodId, int activityId);
    }

    public void setOnClickMyTextView(onClickMyTextView onClickMyTextView) {
        this.onClickMyTextView = onClickMyTextView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    class ChildViewHolder {
        TextView tv_num;
        LinearLayout lin_gray;
        TextView trea_btn;
        TextView comm_name;
        TextView tvLimit;
        ImageView image_child;
    }

    class GroupViewHolder {
        ImageView rule;
        View fen_line;
        TextView tv_miaosha_shi;
        TextView tv_miaosha_minter;
        TextView tv_miaosha_second;
        TextView start_end;
        TextView time_left;
        ImageView is_open;
        LinearLayout lltime;
        TextView tv_title;
        TextView tv_new_title;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                long lasttiem = 0;
                for (int i = 0; i < groupList.size(); i++) {
                    if (!StringUtils.isEmpty(groupList.get(i).getEndTime())) {
                        lasttiem =DateUtils.getTimeMillis(null, groupList.get(i).getEndTime());
                    }
                    long time_start = DateUtils.getTimeMillis(null, listBean.getStartTime());
                    long now_time = DateUtils.getSimpleDateFormat();
                    if (now_time < time_start) {//小于开始时间
                        lasttiem = time_start;
                    }
                    countDown(lasttiem);
                }
            }
//            countDown(time_end);老版本
//            sendEmptyMessageDelayed((int) time_end, 1000);
        }
    };


    /**
     * 秒杀
     */
    public void countDown(long time_start) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);

//        Date curDat = new Date(time);
//        String format2 = df.format(curDat);
        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        buffer.append(substring);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 == 0) {
            buffer.append((hour + 3));
            buffer.append(":00:00");
        } else {
            buffer.append((hour + 5));
            buffer.append(":00:00");
        }
        String totime = buffer.toString();
        try {
            java.util.Date date = df.parse(totime);//到的时间
            java.util.Date date1 = df.parse(format);
            long defferenttime = time_start - date1.getTime();
            if (defferenttime < 0) {
                return;
            }
//            long days = defferenttime / (1000 * 60 * 60 * 24);
//            long hours = (defferenttime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//            long minute = (defferenttime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//            long seconds = defferenttime % 60000;
//            long second = Math.round((float) seconds / 1000);
            long days = defferenttime / (1000 * 60 * 60 * 24);
            long hours = (defferenttime) / (1000 * 60 * 60);
            long minute = (defferenttime - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = defferenttime % 60000;
            long second = Math.round((float) seconds / 1000);
            if (hours >= 10) {
                holder.tv_miaosha_shi.setText(hours + "");
            } else {
                holder.tv_miaosha_shi.setText("0" + hours + "");
            }
//            holder.tv_miaosha_shi.setText("0" + hours + "");
            if (minute >= 10) {
                holder.tv_miaosha_minter.setText(minute + "");
            } else {
                holder.tv_miaosha_minter.setText("0" + minute + "");
            }
            if (second >= 10) {
                holder.tv_miaosha_second.setText(second + "");
            } else {
                holder.tv_miaosha_second.setText("0" + second + "");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
