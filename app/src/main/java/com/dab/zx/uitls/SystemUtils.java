package com.dab.zx.uitls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dab.zx.R;
import com.dab.zx.bean.UpdateBean;
import com.dab.zx.config.MyApp;
import com.dab.zx.view.dialog.DialogConfirm;

/**
 * Created by 八神火焰 on 2016/12/29.
 */
public class SystemUtils {
    private static final String TAG = SystemUtils.class.getSimpleName();

    /**
     * 获取当前系统版本号
     */
    public static int getSystemVersionCode() {
        int versionCode = 1;
        // 获取PackageManager的实例
        PackageManager packageManager = MyApp.context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(MyApp.context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return versionCode;
        }
        return versionCode;
    }

    /**
     * 获取当前系统版本名称
     */
    public static String getVersionName() {
        String versionName;
        // 获取PackageManager的实例
        PackageManager packageManager = MyApp.context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(MyApp.context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "1.0.0";
        }
        return versionName;
    }


    /**
     * 显示更新 daliog
     *
     * @param context    上下文
     * @param updateBean 更新模型
     * @param type       0为静默检测更新使用，1为用户点击检测更新使用
     */
    public static void showUpdateDialog(final Context context, final UpdateBean updateBean, int type) {
        // 版本号数小于已安装版本号数，则不用更新
        if (checkVersionCode(updateBean.getVersioncode())) {
            if (DialogConfirm.show(context, context.getString(R.string.update_is_update))) {
//                IntentUtils.startUpdateService(mUpdateBean);
            } else {
                MyApp.mUpdateBean = null;
            }
        } else if (type == 1) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.update_title))
                    .setMessage(context.getString(R.string.update_is_new))
                    .setCancelable(false)
                    .setPositiveButton("确定", (dialog, which) -> dialog.dismiss()).create().show();
        }
    }

    /**
     * 判断版本号
     *
     * @param versionCode 服务器文件的版本号
     * @return 比对结果
     */
    private static boolean checkVersionCode(int versionCode) {
        int systemVersionCode = getSystemVersionCode();
        return versionCode > systemVersionCode;
    }

    public static boolean isInstallOnSDCard() {
        PackageManager  pm = MyApp.context.getPackageManager();
        ApplicationInfo appInfo;
        try {
            appInfo = pm.getApplicationInfo(MyApp.context.getPackageName(), 0);
            if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
