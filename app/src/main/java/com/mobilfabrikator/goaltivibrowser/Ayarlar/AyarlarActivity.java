package com.mobilfabrikator.goaltivibrowser.Ayarlar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

public class AyarlarActivity extends AppCompatActivity {
    private CheckBox chkMxPlayer, checkBoxNexa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        chkMxPlayer = findViewById(R.id.checkBox_mxplayer);
        checkBoxNexa = findViewById(R.id.checkBox_nexa);

        if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {
            chkMxPlayer.setChecked(true);
        } else {
            checkBoxNexa.setChecked(true);
        }

        chkMxPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    sakla("mxplayer", "ok");
                    checkBoxNexa.setChecked(false);
                    chkMxPlayer.setChecked(true);
                    if (!isAppInstalled("com.mxtech.videoplayer.ad")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AyarlarActivity.this);
                        builder.setTitle("GoalTivi");
                        builder.setMessage("Mx Player Bu Cihazda Yüklü Değül Yüklemek İster misiniz");
                        builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                        builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                OpenStoreIntent();

                            }
                        });


                        builder.show();
                    }
                } else {
                    deletePreferences("mxplayer");
                    checkBoxNexa.setChecked(true);
                    chkMxPlayer.setChecked(false);
                }

            }
        });

        checkBoxNexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    sakla("mxplayer", "no");
                    checkBoxNexa.setChecked(true);
                    chkMxPlayer.setChecked(false);
                } else {
                    deletePreferences("mxplayer");
                    checkBoxNexa.setChecked(false);
                    chkMxPlayer.setChecked(true);
                }

            }
        });

        Button btnLogout = findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearUserName("username");
                clearUserName("password");
                Intent i = new Intent(AyarlarActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        Button btnAnamenu = findViewById(R.id.button_anamenu);
        btnAnamenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AyarlarActivity.this, VodORStreamActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        // bilgileri kaydettim.
        editor.commit();
    }

    private void deletePreferences(String key) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, "");

        editor.commit();

    }

    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        String strSavedMem1 = sharedPreferences.getString(key, "");
        return strSavedMem1;

        // TODO Auto-generated method stub
    }

    public void clearUserName(String ctx) {
        SharedPreferences preferences = getSharedPreferences("mydocs", 0);
        preferences.edit().remove(ctx).commit();
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
            System.out.println(" yüklü");


        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            System.out.println(" değil");

        }
        return installed;
    }

    private void OpenStoreIntent() {
        String url = "";
        Intent storeintent = null;
        try {
            url = "market://details?id=com.mxtech.videoplayer.ad";
            storeintent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            storeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(storeintent);
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad";
            storeintent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            storeintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(storeintent);
        }

    }

}
