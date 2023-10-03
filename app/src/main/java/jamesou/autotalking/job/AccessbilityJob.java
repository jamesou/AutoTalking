package jamesou.autotalking.job;

import android.view.accessibility.AccessibilityEvent;
import jamesou.autotalking.service.AutoTalkingService;

public interface AccessbilityJob {
    String getTargetPackageName();
    void onCreateJob(AutoTalkingService service);
    void onReceiveJob(AccessibilityEvent event);
    void onStopJob();
    void onNotificationPosted(IStatusBarNotification service);
    boolean isEnable();
}
