package com.zx.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.R;
import com.zx.uitls.SpUtil;

/**
 * Created by 八神火焰 on 2017/1/12.
 */

public class MessageView extends LinearLayout
{
    TextView tvTitle;
    TextView tvMessage;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context, R.layout.widget_message, null);
        addView(view);
        tvTitle = (TextView)view.findViewById(R.id.title);
        tvMessage = (TextView)view.findViewById(R.id.message);

        TypedArray typedArray  = context.obtainStyledAttributes(attrs, R.styleable.MessageView);
        String     titleText   = typedArray.getString(R.styleable.MessageView_message_title_text);
        String     messageText = typedArray.getString(R.styleable.MessageView_message_message_text);
        tvTitle.setText(titleText);
        tvMessage.setText(messageText);
        typedArray.recycle();
    }

    public void setDefaultSp(String key, String value) {
        SpUtil.getInstances().putString(key, value);
        tvMessage.setText(value);
    }

    public void setMessage(String message){
        tvMessage.setText(message);
    }
}
