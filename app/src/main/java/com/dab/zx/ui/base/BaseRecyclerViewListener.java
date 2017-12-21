package com.dab.zx.ui.base;

import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by 八神火焰 on 2016/12/22.
 */

public interface BaseRecyclerViewListener {
    interface OnItemClickListener {
        void onItemClick(View view, List<?> data, int position);
    }

    interface OnItemLongClickListener {
        void onItemLongClick(View view, List<?> data, int position);
    }

    interface OnItemTouchListener {
        void OnItemTouch(View view, MotionEvent motionEvent, List<?> data, int position);
    }
}
