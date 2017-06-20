package com.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 八神火焰 on 2017/1/12.
 */
public class SelectView extends LinearLayout
{
    @BindView(R.id.tv_key)
    TextView  tvKey;
    @BindView(R.id.tv_value)
    TextView  tvValue;
    @BindView(R.id.img_right_arrow)
    ImageView imgRightArrow;

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_select, null);
        addView(view);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectView);
        String     keyText    = typedArray.getString(R.styleable.SelectView_title_text);
        String     valueText  = typedArray.getString(R.styleable.SelectView_message_text);
        Boolean    isEnable   = typedArray.getBoolean(R.styleable.SelectView_enable, true);
        typedArray.recycle();
        tvKey.setText(keyText);
        tvValue.setText(valueText);
        if (!isEnable) {
            imgRightArrow.setVisibility(GONE);
        }
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setValue(String message) {
        tvValue.setText(message);
    }

    public String getValue() {
        return tvValue.getText().toString().trim();
    }
}
