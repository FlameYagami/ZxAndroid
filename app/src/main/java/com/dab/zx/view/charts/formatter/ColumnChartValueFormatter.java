package com.dab.zx.view.charts.formatter;

import com.dab.zx.view.charts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
