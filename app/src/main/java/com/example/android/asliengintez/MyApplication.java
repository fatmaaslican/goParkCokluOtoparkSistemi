package com.example.android.asliengintez;

import com.firebase.client.Firebase;

/**
 * Includes one-time initialization of Firebase related code
 */
public class MyApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /* Initialize Firebase */
        Firebase.setAndroidContext(this);

        /* Enable disk persistence  */
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
//        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
    }

}