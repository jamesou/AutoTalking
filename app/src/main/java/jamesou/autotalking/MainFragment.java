package jamesou.autotalking;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

import jamesou.autotalking.service.AutoTalkingService;
import jamesou.autotalking.service.NotificationService;
import jamesou.autotalking.utils.Config;

/**
 * Created by ouyangjian on 2016-04-12.
 */
public class MainFragment extends PreferenceFragment {


    private SwitchPreference notificationPref;
    private Preference wechatPref;
    //  private boolean notificationChangeByUser = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(Config.PREFERENCE_NAME);
        addPreferencesFromResource(R.xml.main);

        //微信红包开关
        wechatPref = findPreference(Config.KEY_WECHAT_ENABLE);

        wechatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!AutoTalkingService.isRunning()) {
                    ((MainActivity) getActivity()).showAssistant();
                    return false;//不保存该值
                }
                boolean enable = (boolean) newValue;
                Config.getConfig(getActivity()).setAutoTalkingServiceEnable(enable);
                return true;//保存该值
            }
        });

        notificationPref = (SwitchPreference) findPreference(Config.KEY_NOTIFICATION_SERVICE_ENABLE);
        notificationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Toast.makeText(getActivity(), "该功能只支持安卓4.3以上的系统", Toast.LENGTH_SHORT).show();
                    return false;//不保存该值
                }
                if (!NotificationService.isRunning()) {
                    ((MainActivity) getActivity()).showNotice();
                    return false;
                }
                boolean enable = (boolean) newValue;
                Config.getConfig(getActivity()).setNotificationServiceEnable(enable);
                return true;//保存该值
                  /*  if(!notificationChangeByUser) {
                        notificationChangeByUser = true;
                        return true;
                    }*/
                   /*
                    boolean enalbe = (boolean) newValue;

                    Config.getConfig(getActivity()).setNotificationServiceEnable(enalbe);

                    if(enalbe && !AutoTalkingService.isNotificationServiceRunning()) {
                        ((MainActivity)getActivity()).showNotice();
                        return false;
                    }
                  //  QHBApplication.eventStatistics(getActivity(), "notify_service", String.valueOf(newValue));
                    return true;*/
            }
        });

          /*  Preference preference = findPreference("KEY_FOLLOW_ME");
            if(preference != null) {
                preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                    //    ((MainActivity) getActivity()).showQrDialog();
                   //     QHBApplication.eventStatistics(getActivity(), "about_author");
                        return true;
                    }
                });
            }

            preference = findPreference("KEY_DONATE_ME");
            if(preference != null) {
                preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                     //   ((MainActivity) getActivity()).showDonateDialog();
                     //   QHBApplication.eventStatistics(getActivity(), "donate");
                        return true;
                    }
                });
            }

            findPreference("WECHAT_SETTINGS").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                 //   startActivity(new Intent(getActivity(), WechatSettingsActivity.class));
                    return true;
                }
            });

            findPreference("NOTIFY_SETTINGS").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                 //   startActivity(new Intent(getActivity(), NotifySettingsActivity.class));
                    return true;
                }
            });*/

    }

    /**
     * 更新快速读取通知的设置
     */
       /* public void updateNotifyPreference() {
            if(notificationPref == null) {
                return;
            }
            boolean running = AutoTalkingService.isNotificationServiceRunning();
            boolean enable = Config.getConfig(getActivity()).isEnableNotificationService();
            if( enable && running && !notificationPref.isChecked()) {
            //    QHBApplication.eventStatistics(getActivity(), "notify_service", String.valueOf(true));
                notificationChangeByUser = false;
                notificationPref.setChecked(true);
            } else if((!enable || !running) && notificationPref.isChecked()) {
                notificationChangeByUser = false;
                notificationPref.setChecked(false);
            }
        }*/
    @Override
    public void onResume() {
        super.onResume();
        //  updateNotifyPreference();
    }

    public Preference getWechatPref() {
        return wechatPref;
    }

}