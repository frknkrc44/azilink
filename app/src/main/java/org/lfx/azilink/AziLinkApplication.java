package org.lfx.azilink;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AziLinkApplication extends Application {
    private static AziLinkApplication app;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        app = this;
        super.onCreate();
    }

    public static AziLinkApplication getCtx() {
        return app;
    }

    /** Get app name as log tag. */
    public static String getLogTag() {
        return app.getString(R.string.app_name);
    }

    public static SharedPreferences getSP() {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(app);
        }

        return sp;
    }

    public static int getPort() {
        return Integer.parseInt(
                getSP().getString(app.getString(R.string.pref_key_socket_port), "41927"));
    }
}
