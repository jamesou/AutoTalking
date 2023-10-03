package jamesou.autotalking.job;

import android.content.Context;

import jamesou.autotalking.utils.Config;
import jamesou.autotalking.service.AutoTalkingService;


public abstract class BaseAccessbilityJob implements AccessbilityJob {

    private AutoTalkingService service;

    @Override
    public void onCreateJob(AutoTalkingService service) {
        this.service = service;
    }

    public Context getContext() {
        return service.getApplicationContext();
    }

    public Config getConfig() {
        return service.getConfig();
    }

    public AutoTalkingService getService() {
        return service;
    }
}
