package com.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS;
import static android.support.design.widget.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL;

/**
 * Created by 八神火焰 on 2017/1/6.
 */

public class AppBarView extends AppBarLayout
{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public interface NavigationClickListener
    {
        void onNavigationClick();
    }

    public interface MenuClickListener
    {
        void onMenuClick(MenuItem item);
    }

    public AppBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_appbar, null);
        addView(view);
        ButterKnife.bind(this, view);

        TypedArray typedArray      = context.obtainStyledAttributes(attrs, R.styleable.AppBarView);
        String     titleText       = typedArray.getString(R.styleable.AppBarView_title_text);
        Boolean    menuVisible     = typedArray.getBoolean(R.styleable.AppBarView_menu_visible, false);
        Boolean    scrollFlags     = typedArray.getBoolean(R.styleable.AppBarView_scrollFlags, false);
        Integer    navigationResId = typedArray.getResourceId(R.styleable.AppBarView_navigation_src, R.mipmap.ic_nav_back);
        Integer    menuResId       = typedArray.getResourceId(R.styleable.AppBarView_menu_src, R.mipmap.ic_nav_menu);
        typedArray.recycle();

        mToolbar.setTitle(titleText);
        mToolbar.setNavigationIcon(navigationResId);
        if (scrollFlags) {
            AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams)mToolbar.getLayoutParams();
            mParams.setScrollFlags(SCROLL_FLAG_SCROLL | SCROLL_FLAG_ENTER_ALWAYS);
        }
        if (menuVisible) {
            mToolbar.setOverflowIcon(getResources().getDrawable(menuResId));
        }
    }

    public void setNavigationClickListener(NavigationClickListener mNavigationClickListener) {
        mToolbar.setNavigationOnClickListener(v -> mNavigationClickListener.onNavigationClick());
    }

    public void setMenuClickListener(int menuResId, MenuClickListener mMenuClickListener) {
        mToolbar.inflateMenu(menuResId);
        mToolbar.setOnMenuItemClickListener(item -> {
            mMenuClickListener.onMenuClick(item);
            return false;
        });
    }

    public void setTitleText(String titleText) {
        mToolbar.setTitle(titleText);
    }
}
