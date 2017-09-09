package com.zx.uitls.statusbar;

import android.app.Activity;
import android.os.Build;

public class StatusBarHelper
{

    private final StatusBarHelperApi mImpl;

    public StatusBarHelper(Activity activity) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            mImpl = new StatusBarHelperImpl19(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImpl = new StatusBarHelperImpl21(activity);
        } else {
            mImpl = new StatusBarHelperImpl(activity);
        }
    }

    public void setColor(int ResId) {
        mImpl.setColor(ResId);
    }
}
