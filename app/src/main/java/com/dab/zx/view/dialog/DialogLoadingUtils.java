package com.dab.zx.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by 八神火焰 on 2016/12/1.
 */

public class DialogLoadingUtils {
    @SuppressLint("StaticFieldLeak")
    private static DialogLoading dialog;

    public static void show(Context context) {
        dialog = new DialogLoading(context);
        show();
    }

    public static void show(Context context, String message) {
        dialog = new DialogLoading(context, message);
        show();
    }

    public static void show(Context context, DialogInterface.OnKeyListener onKeyListener) {
        dialog = new DialogLoading(context, onKeyListener);
        show();
    }

    public static void show(Context context, String message, DialogInterface.OnKeyListener onCancelListener) {
        dialog = new DialogLoading(context, message, onCancelListener);
        show();
    }

    public static void hide() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private static void show() {
        dialog.show();
        dialog.getWindow().setLayout(300, 300);
    }
}
