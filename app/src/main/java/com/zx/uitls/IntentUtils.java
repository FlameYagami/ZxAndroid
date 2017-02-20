package com.zx.uitls;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.zx.config.MyApp.context;


/**
 * Intent跳转工具类
 *
 * @version 1.0
 * @Description 用于简化页面跳转搞得重复代码
 */
public class IntentUtils
{
    /**
     * 普通的方式打开一个Activiy
     *
     * @param context   上下文
     * @param gotoClass 需要打开的Activity
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void gotoActivity(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
    }

    /**
     * 普通的方式打开一个activity，并携带数据
     *
     * @param context   上下文
     * @param gotoClass 需要打开的Activity
     * @param bundle    携带的数据
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void gotoActivity(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 跳转至指定activity,并关闭当前activity.
     *
     * @param context   上下文
     * @param gotoClass 需要跳转的Activity界面
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void gotoActivityAndFinish(Context context, Class<?> gotoClass) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    /**
     * 携带传递数据跳转至指定activity,并关闭当前activity.
     *
     * @param context   上下文
     * @param gotoClass 需要跳转的页面
     * @param bundle    附带数据
     * @updateInfo (此处输入修改内容, 若无修改可不写.)
     */
    public static void gotoActivityAndFinish(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static void sendBroadcast(Context context, String intentAction) {
        Intent intent = new Intent(intentAction);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String intentAction, Bundle bundle) {
        Intent intent = new Intent(intentAction);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    public static void startService(Class<?> className) {
        Intent intent = new Intent(context, className);//设置广播仅对本程序有效
        context.startService(intent);
    }
}
