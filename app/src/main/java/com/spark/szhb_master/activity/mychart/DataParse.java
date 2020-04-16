package com.spark.szhb_master.activity.mychart;

import android.util.SparseArray;

import com.spark.szhb_master.entity.ChartBean;
import com.spark.szhb_master.entity.NewCurrency;
import com.spark.szhb_master.utils.GlobalConstant;
import com.spark.szhb_master.utils.DateUtils;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 */

public class DataParse implements Serializable {

    private ArrayList<MinutesBean> datas = new ArrayList<>();
    private ArrayList<KLineBean> kDatas = new ArrayList<>();

    private float baseValue;
    private float permaxmin;
    private float volmax;

    private SparseArray<String> xValuesLabel = new SparseArray<>();

    public void parseMinutes(JSONArray object, float baseValue) {
        if (object == null) return;
        /*数据解析依照自己需求来定，如果服务器直接返回百分比数据，则不需要客户端进行计算*/
        this.baseValue = baseValue;
        int count = object.length();
        for (int i = 0; i < count; i++) {
            JSONArray data = object.optJSONArray(i);
//            String[] t = object.optString(i).split(" ");/*  "0930 9.50 4707",*/
            MinutesBean minutesData = new MinutesBean();
            minutesData.open = (float) data.optDouble(1);
            minutesData.close = (float) data.optDouble(4);
            minutesData.high = (float) data.optDouble(2);
            minutesData.low = (float) data.optDouble(3);

            minutesData.time = DateUtils.getFormatTime("HH:mm", new Date(data.optLong(0)));
            minutesData.cjprice = (float) data.optDouble(4);
            minutesData.cjnum = (float) data.optDouble(5);
            minutesData.total = minutesData.cjnum * minutesData.cjprice;
            minutesData.avprice = minutesData.cjprice;
            minutesData.cha = minutesData.cjprice - baseValue;
            minutesData.per = (minutesData.cha / baseValue);
            double cha = minutesData.cjprice - baseValue;
            if (Math.abs(cha) > permaxmin) {
                permaxmin = (float) Math.abs(cha);
            }
            volmax = Math.max(minutesData.cjnum, volmax);
            datas.add(minutesData);
        }
        if (permaxmin == 0) {
            permaxmin = baseValue * 0.02f;
        }
    }

    /**
     * 将返回的k线数据转化成 自定义实体类
     */
    public void parseKLine(List<ChartBean> currencies, int tag) {
        //Entry高低 开收           //开高低收
        for (int i = 0; i < currencies.size(); i++) {
            ChartBean currency = currencies.get(i);
            String pattern = "MM-dd HH:mm";
            if (tag == GlobalConstant.TAG_DAY)
                pattern = "yyyy-MM-dd";
            String date = DateUtils.getFormatTime(pattern, new Date(currency.getId()));
            //K线实体类
            KLineBean kLineData = new KLineBean(date, (float)currency.getOpen(), (float)currency.getClose(),
                (float)currency.getHigh(), (float)currency.getLow(), (float)currency.getVol());
            kDatas.add(kLineData);
            volmax = Math.max(kLineData.vol, volmax);
            xValuesLabel.put(i, kLineData.date);
        }
    }


    public ArrayList<MinutesBean> getDatas() {
        return datas;
    }

    public ArrayList<KLineBean> getKLineDatas() {
        return kDatas;
    }

    /**
     * 得到百分百最大值
     *
     * @return
     */
    public float getPercentMax() {
        return permaxmin / baseValue;
    }

}
