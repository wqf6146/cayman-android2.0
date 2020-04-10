package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
