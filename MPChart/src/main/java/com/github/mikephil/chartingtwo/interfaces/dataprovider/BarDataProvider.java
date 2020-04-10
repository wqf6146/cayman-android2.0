package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BarData getBarData();
    boolean isDrawBarShadowEnabled();
    boolean isDrawValueAboveBarEnabled();
    boolean isHighlightFullBarEnabled();
}
