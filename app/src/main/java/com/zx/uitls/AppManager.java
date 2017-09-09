package com.zx.uitls;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

public class AppManager
{
    private static final String TAG = AppManager.class.getSimpleName();

    private volatile Stack<WeakReference<Activity>> mActivityStack;
    private static   AppManager                     mInstances;

    private AppManager() {
        if (null == mActivityStack) {
            mActivityStack = new Stack<>();
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getInstances() {
        if (null == mInstances) {
            synchronized (AppManager.class) {
                if (null == mInstances) {
                    mInstances = new AppManager();
                }
            }
        }
        return mInstances;
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    public void checkWeakReference() {
        if (null != mActivityStack) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity                temp              = activityReference.get();
                if (temp == null) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        getInstances().mActivityStack.add(new WeakReference<>(activity));
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        getInstances().checkWeakReference();
        if (null != getInstances().mActivityStack && !getInstances().mActivityStack.isEmpty()) {
            return getInstances().mActivityStack.lastElement().get();
        }
        return null;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (null != activity && null != getInstances().mActivityStack) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = getInstances().mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity                tempActivity      = activityReference.get();
                // 清理掉已经释放的activity
                if (tempActivity == null) {
                    it.remove();
                    continue;
                }
                if (tempActivity == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (null != getInstances().mActivityStack) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = getInstances().mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity                tempActivity      = activityReference.get();
                // 清理掉已经释放的activity
                if (tempActivity == null) {
                    it.remove();
                    continue;
                }
                if (tempActivity.getClass().equals(cls)) {
                    it.remove();
                    tempActivity.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (null != getInstances().mActivityStack) {
            for (WeakReference<Activity> activityReference : getInstances().mActivityStack) {
                Activity activity = activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            getInstances().mActivityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}