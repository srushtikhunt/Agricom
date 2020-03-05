package com.example.agricom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SCREENSHOT=59706;
    private MediaProjectionManager mgr;
    public static  boolean tak=false;



    Switch aSwitch;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.swh_takeSS);

        File folder = new File(Environment.getExternalStorageDirectory() + "/Agricom");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            System.out.println("Folder made successfully");
        } else {
            System.out.println("Error in making Folder");
        }

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        mgr=(MediaProjectionManager)getSystemService(MEDIA_PROJECTION_SERVICE);
        assert mgr != null;
        startActivityForResult(mgr.createScreenCaptureIntent(), REQUEST_SCREENSHOT);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tak = aSwitch.isChecked();
            }
        });

        //setAll();
    }

    public void openPlanTX(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.peat.GartenBank");
        if (launchIntent != null) {
            Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            startActivity(launchIntent);//null pointer check in case package name was not found
        } else {
            Toast.makeText(this, "Please install Plantix From Play store", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void openDroneStream(View view) {
    }

    public void openControl(View view) {
        startActivity(new Intent(getApplicationContext(), Pump_Control.class));
    }


    public void clrData(View view) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Agricom");
        try {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                assert children != null;
                System.out.println("ifififif:" + children.toString());
                for (String child : children) {
                    new File(dir, child).delete();
                }
            }
            Toast.makeText(this, "Memory has been cleared", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "No any Data stored", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            System.out.println("granted");
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        File imagePath = new File(Environment.getExternalStorageDirectory().toString() + "/Agricom/" + now + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("outout");
        super.onActivityResult(requestCode, resultCode, data);
        Intent i= new Intent(this, ScreenshotService.class).putExtra(ScreenshotService.EXTRA_RESULT_CODE, resultCode).putExtra(ScreenshotService.EXTRA_RESULT_INTENT, data);
        startService(i);
    }


}
