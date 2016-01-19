package com.colintheshots.testlivewallpaper;

import com.jaredrummler.android.device.DeviceName;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.Build;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.support.v4.media.session.MediaButtonReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by colin on 1/18/16.
 */
public class MyWallpaperService extends WallpaperService {

    public static final int FRAME_DURATION = 5000;

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Handler mHandler;
        private boolean mVisible = false;
        private int mWidth = 0;
        private int mHeight = 0;

        public WallpaperEngine() {
            mHandler = new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mHolder = surfaceHolder;
            mHolder.addCallback(this);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (mVisible) {
                mHandler.post(draw);
            } else {
                mHandler.removeCallbacks(draw);
            }
        }

        private Runnable draw = new Runnable() {
            @Override
            public void run() {

                if (mVisible) {
                    Canvas canvas = mHolder.lockCanvas();
                    WidgetGroup group = new WidgetGroup(getApplicationContext());
                    group.setBackgroundColor(Color.WHITE);
                    group.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    group.setAddStatesFromChildren(true);
                    group.layout(0, 0, mWidth, mHeight);

                    String result = Build.MANUFACTURER + " " +
                            Build.VERSION.RELEASE + " " + DeviceName.getDeviceName();
                    TextView osVersion = new TextView(getApplicationContext());
                    osVersion.setTextColor(Color.BLACK);
                    osVersion.setTextSize(80.f);
                    osVersion.setText(result);
                    osVersion.setId(R.id.os_version);

                    osVersion.layout(0, 0, mWidth, mHeight);
                    group.addView(osVersion);

                    group.draw(canvas);
                    getSurfaceHolder().unlockCanvasAndPost(canvas);
                    mHandler.removeCallbacks(draw);
                    mHandler.postDelayed(draw, FRAME_DURATION);
                }
            }
        };

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(draw);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    public class WidgetGroup extends ViewGroup {

        public WidgetGroup(Context context) {
            super(context);
            setWillNotDraw(true);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            layout(l, t, r, b);
        }
    }
}
