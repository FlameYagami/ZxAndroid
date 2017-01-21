package com.zx.uitls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zx.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/1/14.
 */

public class MyPopup extends PopupWindow
{
    public HandViewHolder mHandViewHolder;

    public MyPopup(Context context, int layoutId) {
        View view = View.inflate(context, layoutId, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        switch (layoutId){
            case R.layout.popupwindow_hand:{
                mHandViewHolder = new HandViewHolder(view);
                break;
            }
        }
    }

    public class HandViewHolder
    {
        HandViewHolder(View view){
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.popup_set)
        public void Set_Click(){
            dismiss();
        }

        @OnClick(R.id.popup_launch)
        public void Launch_Click(){
            dismiss();
        }
    }
}
