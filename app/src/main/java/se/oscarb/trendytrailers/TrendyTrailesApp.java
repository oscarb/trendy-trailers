package se.oscarb.trendytrailers;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


public class TrendyTrailesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
