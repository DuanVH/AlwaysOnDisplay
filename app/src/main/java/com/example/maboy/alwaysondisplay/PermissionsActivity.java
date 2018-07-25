package com.example.maboy.alwaysondisplay;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by maboy on 25/08/2017.
 */

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {

    private TextView tvTitle;
    private TextView tv01, tvContent01;
    private TextView tv02, tvContent02;
    private TextView tv03, tvContent03;
    private TextView tv04, tvContent04;
    private ImageView ivNext;

    //    private int click1 = 0;
    private int click2 = 0;
    private int click3 = 0;
    private int click4 = 0;

    private boolean isMakingAnimation;

    private int enable02 = 0;
    private int enable03 = 0;
    private int enable04 = 0;

    private Permission mPermission;
    String prefName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readData();

        if (getEnable02() == 1 && getEnable03() == 1 && getEnable04() == 1) {
            startActivity(new Intent(this, MainAct.class));
        }

        setContentView(R.layout.permissions_view);
        initViews();

        if (getEnable02() == 1) {
            tvContent02.setText("DONE");
        }
        if (getEnable03() == 1) {
            tvContent03.setText("DONE");
        }
        if (getEnable04() == 1) {
            tvContent04.setText("DONE");
            tvContent04.setEnabled(false);
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        isMarshmallowOrHigher();
    }

    private void makeAnimation(int animationID, View view) {
        Animation animation = AnimationUtils.loadAnimation(this, animationID);
        view.startAnimation(animation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tv01 = (TextView) findViewById(R.id.tv_01);
        tv02 = (TextView) findViewById(R.id.tv_02);
        tv03 = (TextView) findViewById(R.id.tv_03);
        tv04 = (TextView) findViewById(R.id.tv_04);
        tvContent01 = (TextView) findViewById(R.id.tv_content01);
        tvContent02 = (TextView) findViewById(R.id.tv_content02);
        tvContent03 = (TextView) findViewById(R.id.tv_content03);
        tvContent04 = (TextView) findViewById(R.id.tv_content04);
        ivNext = (ImageView) findViewById(R.id.iv_next);


//        tvContent01.setOnClickListener(this);
        tvContent02.setOnClickListener(this);
        tvContent03.setOnClickListener(this);
        tvContent04.setOnClickListener(this);
        ivNext.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (isMakingAnimation) {
            return;
        }
        switch (view.getId()) {

            // WRITE_EXTERNAL_STORAGE
            case R.id.tv_content02:
                click2 = 1;
                initPermissionStorage();
                break;

            // READ_PHONE_STATE
            case R.id.tv_content03:
                click3 = 1;
                initPermissionReadPhoneState();
                break;

            // ACCESS_NOTIFICATION
            case R.id.tv_content04:
                click4 = 1;
                initPermissionAccessNotifications();
                break;

            case R.id.iv_next:

                if (enable02 == 0) {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
                    tvContent02.startAnimation(animation);
                }
                if (enable03 == 0) {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
                    tvContent03.startAnimation(animation);
                }
                if (enable04 == 0) {
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
                    tvContent04.startAnimation(animation);
                }
                if (enable02 == 1 && enable03 == 1 && enable04 == 1) {
                    saveData();
                    startActivity(new Intent(this, MainAct.class));
                }
                break;
            default:
                break;
        }
    }


    private boolean isMarshmallowOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public void initPermissionStorage() {
        if (isMarshmallowOrHigher()) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Từ chối thì trả về True
                    // Tích chọn "Never ask again" và "từ chối" thì sẽ trả về False
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    // do nothing
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void initPermissionReadPhoneState() {
        if (isMarshmallowOrHigher()) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_PHONE_STATE)) {

                } else {

                }
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initPermissionAccessNotifications() {
        mPermission = new Permission(getBaseContext());
        if (!mPermission.isRequiredPermissionGranted()) {
            Intent intent1 = mPermission.createRequiredPermissionModifySystemSettings();
            startActivityForResult(intent1, Permission.REQUIRED_PERMISSION_REQUEST_CODE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Permission.REQUIRED_PERMISSION_REQUEST_CODE) {
            setEnable04(click4);
            tvContent04.setText("DONE");
            tvContent04.setEnabled(false);
            saveData();
        } else {
            setEnable04(click4);
            tvContent04.setText("ALLOW NOW");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ...khi người dùng click chọn allow

                if (click2 == 1) {
                    setEnable02(click2);
                    tvContent02.setText("DONE");
                    saveData();
                }
                if (click3 == 1) {
                    setEnable03(click3);
                    tvContent03.setText("DONE");
                    saveData();
                }
            } else {
                // ...khi người dùng click chọn denied
                if (click2 == 1) {
                    setEnable02(click2);
                    tvContent02.setText("ALLOW NOW");
                }
                if (click3 == 1) {
                    setEnable03(click3);
                    tvContent03.setText("ALLOW NOW");
                }
            }
        }
        else {
            // ...
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void saveData() {
        SharedPreferences pre = getSharedPreferences(prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        int en2 = getEnable02();
        int en3 = getEnable03();
        int en4 = getEnable04();

        editor.putInt("en2", en2);
        editor.putInt("en3", en3);
        editor.putInt("en4", en4);
        editor.commit();
    }

    public void readData() {
        SharedPreferences pre = getSharedPreferences(prefName, MODE_PRIVATE);
        setEnable02(pre.getInt("en2", 0));
        setEnable03(pre.getInt("en3", 0));
        setEnable04(pre.getInt("en4", 0));
    }

    public int getEnable02() {
        return enable02;
    }

    public int getEnable03() {
        return enable03;
    }

    public int getEnable04() {
        return enable04;
    }

    public void setEnable02(int enable02) {
        this.enable02 = enable02;
    }

    public void setEnable03(int enable03) {
        this.enable03 = enable03;
    }

    public void setEnable04(int enable04) {
        this.enable04 = enable04;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        isMakingAnimation = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isMakingAnimation = false;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // nothing
    }

}
