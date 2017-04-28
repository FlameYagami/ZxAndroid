package com.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by 八神火焰 on 2017/1/6.
 */

public class HeaderView extends LinearLayout
{
    @BindView(R.id.view_header_left)
    LinearLayout   viewHeaderLeft;
    @BindView(R.id.view_header_right)
    LinearLayout   viewHeaderRight;
    @BindView(R.id.img_back)
    ImageView      viewBack;
    @BindView(R.id.tv_title)
    TextView       viewTitle;
    @BindView(R.id.img_menu)
    ImageView      viewMenu;
    @BindView(R.id.view_shadowbar)
    View           viewShadowBar;
    @BindView(R.id.view_statusbar)
    View           viewStatusbar;
    @BindView(R.id.view_headerbar)
    RelativeLayout viewHeaderBar;

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_header, null);
        addView(view);
        ButterKnife.bind(this, view);

        TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        String     titleText   = typedArray.getString(R.styleable.HeaderView_title_text);
        Boolean    menuVisible = typedArray.getBoolean(R.styleable.HeaderView_menu_visible, false);
        Boolean    backVisible = typedArray.getBoolean(R.styleable.HeaderView_back_visible, true);
        typedArray.recycle();

        viewTitle.setText(titleText);
        viewBack.setVisibility(backVisible ? VISIBLE : GONE);
        viewMenu.setVisibility(menuVisible ? VISIBLE : GONE);
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

    public RelativeLayout getHeaderBar() {
        return viewHeaderBar;
    }

    public View getStatusbar() {
        return viewStatusbar;
    }

    public View getShadowBar() {
        return viewShadowBar;
    }
}
