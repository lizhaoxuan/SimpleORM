package com.zhaoxuan.ormdemo;

import android.app.Application;

/**
 * Created by lizhaoxuan on 15/12/15.
 */
public class OrmApplication extends Application {

    private static OrmApplication instance;

    public static OrmApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
