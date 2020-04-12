package com.spark.szhb_master.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManage {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        for (int i=0;i<activities.size();i++){
            if (activities.get(i) == activity){
                activities.remove(i);
                break;
            }
        }
    }

    public static void closeActivity(Class activity) {
        for (int i=0;i<activities.size();i++){
            if (activities.get(i).getClass() == activity){
                Activity tar = activities.get(i);
                activities.remove(i);
                if (!tar.isFinishing())
                    tar.finish();
                break;
            }
        }
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}

