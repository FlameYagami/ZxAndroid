package com.zx.view.charts.formatter;

import com.zx.view.charts.model.SubcolumnValue;

public interface ColumnChartValueFormatter
{

    int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
