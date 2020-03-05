package com.example.agricom;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
public class ScreenshotService extends Service {


    private MediaProjectionManager mgr;
    private WindowManager wmgr;
    private Handler handler;
    private VirtualDisplay vdisplay;
    static final String EXTRA_RESULT_CODE = "resultCode";
    static final String EXTRA_RESULT_INTENT = "resultIntent";
    private int resultCode;
    private Intent resultData;
    static final int VIRT_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    final private HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_BACKGROUND);

    public ScreenshotService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service->>","Created");
        mgr = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        wmgr = (WindowManager) getSystemService(WINDOW_SERVICE);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        resultCode = i.getIntExtra(EXTRA_RESULT_CODE, 1337);
        resultData = i.getParcelableExtra(EXTRA_RESULT_INTENT);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                try {
                    while (true) {
                        if(MainActivity.tak)
                            doti();
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {

                }
            }
        }).start();


        return super.onStartCommand(i, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void doti() {
        MediaProjection projection = mgr.getMediaProjection(resultCode, resultData);
        ImageTransmogrifier it = new ImageTransmogrifier(this);

        MediaProjection.Callback cb = new MediaProjection.Callback() {
            @Override
            public void onStop() {
                vdisplay.release();
            }
        };

        vdisplay = projection.createVirtualDisplay("Agricom", it.getWidth(), it.getHeight(), getResources().getDisplayMetrics().densityDpi, VIRT_DISPLAY_FLAGS, it.getSurface(), null, handler);
        projection.registerCallback(cb, handler);
    }

    WindowManager getWindowManager() {
        return (wmgr);
    }

    void processImage(final byte[] png) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/Agricom/" + now + ".jpg";
        File output = new File(mPath);
        //File output = new File(getExternalFilesDir(null), "screenshot.png");
        System.out.println("infileinfile");
        try {
            FileOutputStream fos = new FileOutputStream(output);
            fos.write(png);
            fos.flush();
            fos.getFD().sync();
            fos.close();
            MediaScannerConnection.scanFile(ScreenshotService.this, new String[]{output.getAbsolutePath()}, new String[]{"image/png"}, null);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Exception writing out screenshot", e);
        }

    }

    Handler getHandler() {
        return (handler);
    }
}
