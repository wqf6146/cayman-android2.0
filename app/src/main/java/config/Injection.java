package config;

import android.content.Context;

import com.spark.szhb_master.data.DataRepository;
import com.spark.szhb_master.data.LocalDataSource;
import com.spark.szhb_master.data.RemoteDataSource;

/**
 * Created by Administrator on 2017/9/25.
 */

public class Injection {
    public static DataRepository provideTasksRepository(Context context) {
        return DataRepository.getInstance(RemoteDataSource.getInstance(), LocalDataSource.getInstance(context));
    }
}
