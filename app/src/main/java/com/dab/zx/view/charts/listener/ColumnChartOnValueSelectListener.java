package com.dab.zx.view.charts.listener;

import com.dab.zx.view.charts.model.SubcolumnValue;

public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener {

    void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value);

}
