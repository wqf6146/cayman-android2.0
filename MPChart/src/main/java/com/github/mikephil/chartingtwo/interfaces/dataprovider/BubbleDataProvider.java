package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
