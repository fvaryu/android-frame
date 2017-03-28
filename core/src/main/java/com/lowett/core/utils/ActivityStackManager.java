package com.lowett.core.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Iterator;
import java.util.Stack;

public class ActivityStackManager {

    private static Stack<Activity> s_activityStack;
    private static ActivityStackManager s_instance;

    private ActivityStackManager() {
    }

    public static ActivityStackManager getInstance() {
        if (s_instance == null) {
            s_instance = new ActivityStackManager();
        }
        return s_instance;
    }


    public void addActivity(Activity activity) {
        if (s_activityStack == null) {
            s_activityStack = new Stack<Activity>();
        }
        s_activityStack.add(activity);
    }

    public Activity currentActivity() {
        Activity activity = s_activityStack.lastElement();
        return activity;
    }

    public Activity getActivity(Class<?> cls) {
        Activity result = null;
        for (Activity activity : s_activityStack) {
            if (activity.getClass().equals(cls)) {
                result = activity;
                break;
            }
        }
        return result;
    }

    public void finishActivity() {
        Activity activity = s_activityStack.lastElement();
        finishActivity(activity);
    }


    public void finishActivity(Activity activity) {
        if (activity != null) {
            s_activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void remove(Activity activity) {
        if (activity != null) {
            s_activityStack.remove(activity);
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls) {
//        for (Activity activity : s_activityStack) {
            Iterator<Activity> iterator = s_activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (cls.equals(activity.getClass())) {
                    activity.finish();
                    iterator.remove();
                    activity = null;
                }
            }
//            if (activity.getClass().equals(cls)) {
//                finishActivity(activity);
//            }
//        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = s_activityStack.size(); i < size; i++) {
            if (null != s_activityStack.get(i)) {
                s_activityStack.get(i).finish();
            }
        }
        s_activityStack.clear();
    }

    public void finishAllActivityExcept(Class<?> cls) {
        Activity result = getActivity(cls);
        finishAllActivityExcept(result);
    }

    public void finishActivity(Class<?>... cls) {
        for (Class<?> c : cls) {
            finishActivity(c);
        }
    }

    public void finishAllActivityExcept(Activity activity) {
        boolean bSuc = false;
        if (activity != null) {
            bSuc = s_activityStack.remove(activity);
        }
        finishAllActivity();
        if (bSuc) {
            addActivity(activity);
        }
    }


    public int activityCount() {
        if (s_activityStack != null) {
            return s_activityStack.size();
        }
        return 0;
    }

    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    /**
     * 是否存在某个acivity
     *
     * @param cls
     * @return
     */
    public Boolean existActivity(Class<?> cls) {
        boolean isExist = false;
        if (s_activityStack == null || s_activityStack.size() == 0) {
            return false;
        }
        for (Activity activity : s_activityStack) {
            if (activity.getClass().equals(cls)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }
}