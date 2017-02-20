package com.zx.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zx.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 八神火焰 on 2017/2/9.
 */

public class DialogVersusPersonal extends AlertDialog
{
    private OnButtonClick onButtonClick;

    public interface OnButtonClick
    {
        void getButtonType(DialogVersusPersonal dialog, ButtonType type);
    }

    public enum ButtonType
    {
        JoinRoom,
        CreateRoom
    }

    public DialogVersusPersonal(@NonNull Context context, OnButtonClick onButtonClick) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_versus_psersonal, null);
        ButterKnife.bind(this, view);
        setView(view);
        setTitle(context.getString(R.string.versus_personal));
        this.onButtonClick = onButtonClick;
    }

    @OnClick({R.id.btn_join_room, R.id.btn_create_room})
    public void onButton_Click(View view) {
        switch (view.getId()) {
            case R.id.btn_join_room:
                onButtonClick.getButtonType(this, ButtonType.JoinRoom);
                break;
            case R.id.btn_create_room:
                onButtonClick.getButtonType(this, ButtonType.CreateRoom);
                break;
        }
    }
}
