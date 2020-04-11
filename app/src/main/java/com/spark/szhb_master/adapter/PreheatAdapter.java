package com.spark.szhb_master.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import okhttp3.Request;

import static com.spark.szhb_master.utils.okhttp.OkhttpUtils.post;

/**
 * Created by Administrator on 2018/9/12 0012.
 */

public class PreheatAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Preheatinginfo> groupList = new ArrayList<>();
    private List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList = new ArrayList<>();
    private TreawaDialog treawaDialog;
    GroupViewHolder holder;
    private long time_end;
    private long time;
    private Preheatinginfo listBean;
    private int activityid;
    private long typeTime;


    public PreheatAdapter(Context context, List<Preheatinginfo> groupList, List<List<Preheatinginfo.ActivityCommodityInfoListBean>> childList) {
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
            holder.tv_right = view.findViewById(R.id.tv_right);
            holder.tv_new_title = view.findViewById(R.id.tv_new_title);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        holder.tv_right.setVisibility(View.GONE);

//        if (i == 0) {
//            Log.e("TAGqwer", "1111111");
//            holder.rule.setVisibility(View.VISIBLE);
//            holder.rule.setImageResource(R.mipmap.rule_red);
//        } else {
//            Log.e("TAGqwer", "2222222");
//            holder.rule.setVisibility(View.INVISIBLE);
//            holder.fen_line.setVisibility(View.VISIBLE);
//        }

        holder.rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, RuleAcitivity.class).putExtra("num", 2));
            }
        });

        if (b) {
            holder.is_open.setImageResource(R.mipmap.openkai);
        } else {
            holder.is_open.setImageResource(R.mipmap.shouqi);
        }

        holder.tv_miaosha_shi.setBackground((context.getResources().getDrawable(R.drawable.shape_miaosha_red)));
        holder.tv_miaosha_minter.setBackground((context.getResources().getDrawable(R.drawable.shape_miaosha_red)));
        holder.tv_miaosha_second.setBackground((context.getResources().getDrawable(R.drawable.shape_miaosha_red)));
        Drawable drawable = context.getResources().getDrawable(R.mipmap.title_right_red);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        Drawable drawableright = context.getResources().getDrawable(R.mipmap.title_left_red);
        drawableright.setBounds(0, 0, drawableright.getMinimumWidth(), drawableright.getMinimumHeight());
        holder.start_end.setCompoundDrawables(null, null, drawable, null);
        holder.time_left.setCompoundDrawables(drawableright, null, null, null);
        listBean = groupList.get(i);

        time_end = DateUtils.getTimeMillis(null, listBean.getEndTime());
        String str = listBean.getEndTime();

        long time_start = DateUtils.getTimeMillis(null, listBean.getStartTime());
        long now_time = DateUtils.getSimpleDateFormat();

        holder.start_end.setTextColor(context.getResources().getColor(R.color.kred));
        if (now_time > time_start) {
//            后结束  已经开始
            holder.start_end.setText("后结束");
        } else if (now_time < time_start) {
            holder.start_end.setText("后开始");
            time_end = time_start;
        } else if (now_time == time_start) {
            onClickMyTextView.myTextViewClick(0, 0, 0);
        }
        holder.tv_new_title.setVisibility(View.VISIBLE);
        holder.tv_new_title.setText(listBean.getTitle());
        holder.tv_new_title.setTextColor(context.getResources().getColor(R.color.kred));
        typeTime = time_end - now_time;
        handler.sendEmptyMessage((int) time_end);


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
            Glide.with(context).load(datasBean.getCover()).into(holder.image_child);

        if (datasBean.getJoinVote() == 1) {
            holder.trea_btn.setBackground(context.getResources().getDrawable(R.mipmap.preheat_btn));
        } else if (datasBean.getJoinVote() == 0) {
            holder.trea_btn.setBackground(context.getResources().getDrawable(R.mipmap.nojoin_btn));
        }

        int type_enable = (childList.get(i).get(i1).getJoinVote() == 1 ? 1 : 2);
        if (type_enable == 2) {
            holder.tvLimit.setText("");
            Drawable drawable = context.getResources().getDrawable(R.mipmap.fire);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.tv_num.setCompoundDrawables(drawable, null, null, null);
            holder.tv_num.setText("热捧商品，无需打Call！");
            holder.trea_btn.setVisibility(View.GONE);
        } else {
            holder.trea_btn.setVisibility(View.VISIBLE);
            holder.trea_btn.setText(context.getResources().getString(R.string.call));
            holder.tv_num.setCompoundDrawables(null, null, null, null);
            holder.tv_num.setText(context.getResources().getString(R.string.number_participants) + "：" + datasBean.getJoinAmount());
            String html = context.getResources().getString(R.string.heat) + "：" + "<font color=#f84f5c>" + (datasBean.getVoteAmount() * 10) + "";
            holder.tvLimit.setText(Html.fromHtml(html));
        }
        holder.comm_name.setText(datasBean.getCommodityName());
        holder.trea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = MyApplication.getApp().getCurrentUser();
                if (!MyApplication.getApp().isLogin() || StringUtils.isEmpty(user.getNick_name())) {
                    LoginDialog loginDialog = new LoginDialog(context);
                    loginDialog.show();
                } else {
                    long time_start = DateUtils.getTimeMillis(null, groupList.get(i).getStartTime());
                    long now_time = DateUtils.getSimpleDateFormat();
                    int type_two = (now_time > time_start ? 1 : 2);
                    int type = (childList.get(i).get(i1).getJoinVote() == 1 ? 1 : 2);
                    TimeDialog timeDialog = new TimeDialog(context);
                    int gcx = SharedPreferenceInstance.getInstance().getGCX();
                    if (type == 2) {   //type  1无需打call
                        String str = DateUtils.getSFM(typeTime);
                        timeDialog.setEntrust(str, 1);
                        timeDialog.show();
                    } else if (type_two == 2) { //2 未开始
                        String str = DateUtils.getSFM(typeTime);
                        timeDialog.setEntrust(str, 2);
                        timeDialog.show();
                    } else if (gcx == 0) {
                        UpPowerDialog upPowerDialog = new UpPowerDialog(context);
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
        TextView tv_right;
        TextView tv_new_title;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Bundle bundle = msg.getData();
//            String time = bundle.getString("time");
//            int num = bundle.getInt("num");

            countDown(time_end);
            sendEmptyMessageDelayed((int) time_end, 1000);
        }
    };

    /**
     * 秒杀
     */
    private void countDown(long time_end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String format = df.format(curDate);
        StringBuffer buffer = new StringBuffer();
        String substring = format.substring(0, 11);
        buffer.append(substring);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour % 2 == 0) {
            buffer.append((hour + 2));
            buffer.append(":00:00");
        } else {
            buffer.append((hour + 1));
            buffer.append(":00:00");
        }
        String totime = buffer.toString();
        try {
            Date date = df.parse(totime);//到的时间
            Date date1 = df.parse(format);
            long defferenttime = time_end - date1.getTime();
            if (defferenttime < 0) {
                return;
            }
            long hours = (defferenttime) / (1000 * 60 * 60);
            long minute = (defferenttime - hours * (1000 * 60 * 60)) / (1000 * 60);
            long seconds = defferenttime % 60000;
            long second = Math.round((float) seconds / 1000);

            if (hours >= 10) {
                holder.tv_miaosha_shi.setText(hours + "");
            } else {
                holder.tv_miaosha_shi.setText("0" + hours + "");
            }
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
