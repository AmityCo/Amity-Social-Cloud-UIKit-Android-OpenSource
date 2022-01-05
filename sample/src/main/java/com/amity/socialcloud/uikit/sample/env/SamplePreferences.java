package com.amity.socialcloud.uikit.sample.env;

import android.content.Context;
import android.content.SharedPreferences;

import com.amity.socialcloud.uikit.sample.AmitySampleApp;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

public class SamplePreferences {

    private static final String API_KEY_KEY = "API_KEY_KEY";
    private static final String HTTP_URL_KEY = "HTTP_URL_KEY";
    private static final String SOCKET_URL_KEY = "SOCKET_URL_KEY";
    private static final String MY_USER_ID_KEY = "MY_USER_ID_KEY";

    private static class SamplePreferencesHolder {
        private static final RxSharedPreferences INSTANCE = init();
    }

    private static RxSharedPreferences init() {
        SharedPreferences preferences = AmitySampleApp.APP
                .getSharedPreferences("uikit_sample_preferences", Context.MODE_PRIVATE);
        return RxSharedPreferences.create(preferences);
    }

    public static RxSharedPreferences get() {
        return SamplePreferencesHolder.INSTANCE;
    }


    public static Preference<String> getApiKey() {
        return get().getString(API_KEY_KEY, SampleAPIKey.INSTANCE.get());
    }

    public static Preference<String> getHttpUrl() {
        return get().getString(HTTP_URL_KEY, SampleUrl.INSTANCE.get());
    }

    public static Preference<String> getSocketUrl() {
        return get().getString(SOCKET_URL_KEY, SampleUrl.INSTANCE.get());
    }

    public static Preference<String> getMyUserId() {
        return get().getString(MY_USER_ID_KEY, "victimAndroid");
    }

}
