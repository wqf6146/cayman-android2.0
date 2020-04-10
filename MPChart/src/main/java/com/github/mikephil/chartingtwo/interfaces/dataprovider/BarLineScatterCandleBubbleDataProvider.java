package com.github.mikephil.chartingtwo.interfaces.dataprovider;

import com.github.mikephil.chartingtwo.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.chartingtwo.utils.Transformer;
import com.github.mikephil.chartingtwo.components.YAxis;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(YAxis.AxisDependency axis);
    boolean isInverted(YAxis.AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
