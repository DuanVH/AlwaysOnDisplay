package com.example.maboy.alwaysondisplay;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainAct extends AppCompatActivity {

    private Switch swDoubleTap;
    private ImageView ivOnOff;
    private EditText edtSignature;
    private Switch swSignature;
    private TextView tvAbout;
    private ImageView ivPreview;

    private int enable = 0;
    private static final int ON = 1;  // bat
    private static final int OFF = 0;  // tat

    private AlertDialog alertDialog;

    String shareData;
    private String signature = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        read();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        initViews();

        if (getEnable() == 1) {
            Intent intent = new Intent(MainAct.this, ServiceLock.class);
            setEnable(1);
            ivOnOff.setImageLevel(ON);
            swDoubleTap.setChecked(true);
            startService(intent);
        } else if (getEnable() == 0) {
            setEnable(0);
            ivOnOff.setImageLevel(OFF);
            swDoubleTap.setChecked(false);
        }
    }

    private void initViews() {

        ivOnOff = (ImageView) findViewById(R.id.iv_on_off);
        swDoubleTap = (Switch) findViewById(R.id.sw_double_tap);
        edtSignature = (EditText) findViewById(R.id.edt_set_signature);
        swSignature = (Switch) findViewById(R.id.sw_signature);
        swSignature.setChecked(false);
        tvAbout = (TextView) findViewById(R.id.tv_about);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);

        ivOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainAct.this, ServiceLock.class);

                if (getEnable() == 0) {
                    setEnable(1);
                    ivOnOff.setImageLevel(ON);
                    swDoubleTap.setChecked(true);
                    startService(intent);

                } else if (getEnable() == 1) {
                    setEnable(0);
                    ivOnOff.setImageLevel(OFF);
                    swDoubleTap.setChecked(false);
                    stopService(intent);

                }
            }
        });

        swDoubleTap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Intent intent = new Intent(MainAct.this, ServiceLock.class);

                if (b) {
                    setEnable(1);
                    ivOnOff.setImageLevel(ON);
                    startService(intent);
                } else {
                    setEnable(0);
                    ivOnOff.setImageLevel(OFF);
                    stopService(intent);
                }
            }
        });

        edtSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swSignature.setChecked(false);
            }
        });

        swSignature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    writeData();
                    Log.e("SIG", "onCheckedChanged: ");
                    readData();
                }
            }
        });

        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainAct.this, LockScreen.class);
                intent.putExtra("PREVIEW", edtSignature.getText().toString());
                startActivity(intent);
            }
        });
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void writeData() {
        String str = edtSignature.getText().toString();
        try {
            FileOutputStream fOut = openFileOutput("myFile.txt", MODE_PRIVATE);

            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            //---Bắt đầu quá trình ghi file---
            osw.write(str);
            osw.flush();
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void readData() {
        try {
            FileInputStream fIn = openFileInput("myFile.txt");
            InputStreamReader isr = new InputStreamReader(fIn);

            char[] inputBuffer = new char[100];
            String s = "";

            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //---Chuyển từ kiểu char sang string---
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s += readString;
                inputBuffer = new char[100];
            }
            //---set text vừa đọc ra lên ô nhập liệu
            // read---
            Log.e("READ_DATA", "readData: " + s.toString() );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Information");
        builder.setMessage("Version: 1.0" + "\n" + "Email: maboy170296@gmail.com" + "\n" + "Developer: DuanVu");
        builder.setPositiveButton("OK", null);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    public void save() {
        SharedPreferences pre = getSharedPreferences(shareData, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();

        int ena = getEnable();
        editor.putInt("enable", ena);
        editor.commit();
        Log.e("SAVE", "save: ");
    }

    public void read() {
        SharedPreferences pre = getSharedPreferences(shareData, MODE_PRIVATE);
        setEnable(pre.getInt("enable", 1));
        Log.e("READ", "read: ");
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
}
