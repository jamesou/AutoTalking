package jamesou.autotalking.job;

import android.app.Notification;


public interface IStatusBarNotification {
    String getPackageName();
    Notification getNotification();
}
