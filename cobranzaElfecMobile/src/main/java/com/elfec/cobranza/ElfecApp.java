package com.elfec.cobranza;

import com.activeandroid.app.Application;
import com.elfec.cobranza.settings.PreferencesManager;

import net.danlew.android.joda.JodaTimeAndroid;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ElfecApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/segoe_ui.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        JodaTimeAndroid.init(this);
        PreferencesManager.initialize(this);
    }
}
