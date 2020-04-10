package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.CombinedData;

/**
 * Created by philipp on 11/06/16.
 */
public interface CombinedDataProvider extends LineDataProvider, BarDataProvider, BubbleDataProvider, CandleDataProvider, ScatterDataProvider {

    CombinedData getCombinedData();
}
