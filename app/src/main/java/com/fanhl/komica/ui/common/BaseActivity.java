package com.fanhl.komica.ui.common;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by fanhl on 15/10/24.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Handler backgroundHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    public static class BackgroundThread extends HandlerThread {
        public BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }
}
