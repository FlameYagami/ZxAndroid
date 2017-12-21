package com.dab.zx.view.charts.listener;


public interface OnValueDeselectListener {

    /**
     * Called only in chart selection mode when user touch empty space causing value deselection.
     * Note: this method is not called when selection mode is disabled.
     */
    void onValueDeselected();
}
