package com.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.R;

/**
 * Created by 八神火焰 on 2017/1/6.
 */

public class HeaderView extends LinearLayout
{
    private LinearLayout viewHeaderLeft;
    private LinearLayout viewHeaderRight;
    private ImageView    viewBack;
    private TextView     viewTitle;
    private ImageView    viewMenu;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_header, null);
        addView(view);

        viewHeaderLeft = (LinearLayout)view.findViewById(R.id.view_header_left);
        viewHeaderRight = (LinearLayout)view.findViewById(R.id.view_header_right);
        viewBack = (ImageView)view.findViewById(R.id.img_back);
        viewTitle = (TextView)view.findViewById(R.id.tv_title);
        viewMenu = (ImageView)view.findViewById(R.id.img_menu);

        TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        String     titleText   = typedArray.getString(R.styleable.HeaderView_title_text);
        Boolean    menuVisible = typedArray.getBoolean(R.styleable.HeaderView_menu_visible, false);
        viewTitle.setText(titleText);
        viewMenu.setVisibility(menuVisible ? VISIBLE: GONE);
        typedArray.recycle();
    }

    public void hideViewBack(boolean isVisible) {
        viewBack.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public LinearLayout getViewHeaderLeft() {
        return viewHeaderLeft;
    }

    public LinearLayout getViewHeaderRight() {
        return viewHeaderRight;
    }

    public ImageView getViewBack() {
        return viewBack;
    }

    public TextView getViewTitle() {
        return viewTitle;
    }

    public ImageView getViewMenu() {
        return viewMenu;
    }
}
