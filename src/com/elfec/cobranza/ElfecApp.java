package com.elfec.cobranza;

import com.activeandroid.app.Application;
import com.elfec.cobranza.helpers.PreferencesManager;

public class ElfecApp extends Application {

	@Override
	public void onCreate()
	{
		super.onCreate();
		PreferencesManager.initialize(this);
	}
}
