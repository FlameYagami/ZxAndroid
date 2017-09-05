package com.zx.view.charts.listener;

import com.zx.view.charts.model.SubcolumnValue;

public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener
{

    void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

}
