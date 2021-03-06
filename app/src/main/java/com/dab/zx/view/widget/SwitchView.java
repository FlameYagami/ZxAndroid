package com.dab.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dab.zx.R;
import com.dab.zx.uitls.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/3/15.
 */

public class SwitchView extends LinearLayout {
    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;
    @BindView(R.id.tv_key)
    TextView     textView;

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_switch, null);
        ButterKnife.bind(this, view);
        addView(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        String     titleText  = typedArray.getString(R.styleable.SwitchView_title_text);
        textView.setText(titleText);
        typedArray.recycle();
    }

    public String getKey() {
        return textView.getText().toString().trim();
    }

    public void setChecked(boolean isChecked) {
        switchCompat.setChecked(isChecked);
        SpUtils.putInt(getKey(), switchCompat.isChecked() ? 1 : 0);
    }

    @OnClick(R.id.switch_compat)
    public void onSwitchCompat_Click() {
        setChecked(switchCompat.isChecked());
    }
}
