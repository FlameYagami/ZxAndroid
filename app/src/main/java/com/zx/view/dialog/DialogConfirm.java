package com.zx.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class DialogConfirm
{
    private static Handler handler;
    public         Context context;
    private        int     dialogResult;

    private DialogConfirm(Context context, String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setPositiveButton("确定", new DialogButtonOnClick(1));
        dialogBuilder.setNegativeButton("取消", new DialogButtonOnClick(0));
        dialogBuilder.setOnCancelListener(new DialogCancelOnClick());
        dialogBuilder.setTitle(title).create().show();
        try {
            Looper.loop();
        } catch (Exception ignored) {
        }
    }

    public static boolean show(Context context, String title) {
        handler = new MyHandler();
        return new DialogConfirm(context, title).getResult() == 1;
    }

    public int getResult() {
        return dialogResult;
    }

    private static class MyHandler extends Handler
    {
        public void handleMessage(Message message) {
            throw new RuntimeException();
        }
    }

    /**
     * 确定、取消按钮的监听
     */
    private final class DialogButtonOnClick implements OnClickListener
    {
        int type;

        DialogButtonOnClick(int type) {
            this.type = type;
        }

        public void onClick(DialogInterface dialog, int which) {
            DialogConfirm.this.dialogResult = type;
            Message m = handler.obtainMessage();
            handler.sendMessage(m);
        }
    }

    /**
     * 返回键的监听
     */
    private final class DialogCancelOnClick implements DialogInterface.OnCancelListener
    {
        @Override
        public void onCancel(DialogInterface dialog) {
            DialogConfirm.this.dialogResult = 0;
            Message m = handler.obtainMessage();
            handler.sendMessage(m);
        }
    }
}
