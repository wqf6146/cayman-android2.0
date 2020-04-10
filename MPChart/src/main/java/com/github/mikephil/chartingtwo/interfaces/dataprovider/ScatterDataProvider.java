package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
