package com.zx.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

/**
 * Created by 八神火焰 on 2016/12/1.
 */

public class DialogLoading
{
    private static ProgressDialog dialog;

    public static void showDialog(Context context, String msg, boolean cancelable) {
        dialog = ProgressDialog.show(context, null, !TextUtils.isEmpty(msg) ? msg : "请稍后...", true, cancelable);
    }

    public static void hideDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
