package com.example.agricom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Pump_Control extends AppCompatActivity {

    Switch lgt,snd;
    BackgroundTask bt;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump__control);
        lgt = findViewById(R.id.sw_light);
        snd = findViewById(R.id.sw_spkr);
        rg = findViewById(R.id.radioGroup);

        lgt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        System.out.println("oooooooooooooooo :"+output);
                        bt=null;
                    }
                });
                if(lgt.isChecked())
                {
                    Toast.makeText(Pump_Control.this, "ON", Toast.LENGTH_SHORT).show();
                    bt.execute("light", "1");
                }
                else
                {
                    Toast.makeText(Pump_Control.this, "OFF", Toast.LENGTH_SHORT).show();
                    bt.execute("light", "0");
                }
            }
        });

        snd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        System.out.println("2oooooooooooooooo :"+output);
                        bt=null;
                    }
                });
                if(snd.isChecked())
                {
                    Toast.makeText(Pump_Control.this, "ON", Toast.LENGTH_SHORT).show();
                    bt.execute("sound", "1");
                }
                else
                {
                    Toast.makeText(Pump_Control.this, "OFF", Toast.LENGTH_SHORT).show();
                    bt.execute("sound", "0");
                }
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_low:
                        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                System.out.println("light_output :"+output);
                                bt=null;
                            }
                        });
                        bt.execute("spr_lvl", "1");
                        Toast.makeText(Pump_Control.this, "Low", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_md:
                        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                System.out.println("light_output :"+output);
                                bt=null;
                            }
                        });
                        bt.execute("spr_lvl", "2");
                        Toast.makeText(Pump_Control.this, "Mid", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_high:
                        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                System.out.println("light_output :"+output);
                                bt=null;
                            }
                        });
                        bt.execute("spr_lvl", "3");
                        Toast.makeText(Pump_Control.this, "High", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_auto:
                        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                System.out.println("light_output :"+output);
                                bt=null;
                            }
                        });
                        bt.execute("spr_lvl", "4");
                        Toast.makeText(Pump_Control.this, "Auto Mode", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_off:
                        bt = new BackgroundTask(new BackgroundTask.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                System.out.println("light_output :"+output);
                                bt=null;
                            }
                        });
                        bt.execute("spr_lvl", "0");
                        Toast.makeText(Pump_Control.this, "Off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }
}
