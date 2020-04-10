package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.components.YAxis;
import com.github.mikephil.chartingtwo.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
