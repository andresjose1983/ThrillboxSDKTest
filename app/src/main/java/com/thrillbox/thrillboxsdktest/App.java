package com.thrillbox.thrillboxsdktest;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.thrillbox.frameworktb.MDSettings;

/**
 * Created by andre on 2/22/2018.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MDSettings.initOneSignal(this, false, null, null,
                null, null);

        MDSettings.enableLocation(this, true);

        MDSettings.initAudienceId(this);

    }
}
