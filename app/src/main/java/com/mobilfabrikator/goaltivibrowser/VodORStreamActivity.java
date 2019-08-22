package com.mobilfabrikator.goaltivibrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.Ayarlar.AyarlarActivity;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesCatGridList;
import com.mobilfabrikator.goaltivibrowser.StreamPack.GridCatActivity;
import com.mobilfabrikator.goaltivibrowser.VodPack.VodCatGridList;
import com.mobilfabrikator.goaltivibrowser.favoripack.FavoriVodOrSeriActivity;

import org.apache.commons.lang3.StringUtils;

public class VodORStreamActivity extends Activity {
    public static String vodType;
    Time today;
    public static String bitisTarihi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

      /*  if(width>height){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        // its landscape
     //   this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_vod_or_stream_v2);
        //  sakla("mxplayer","ok");
        Intent i = getIntent();
        bitisTarihi = i.getStringExtra("bitis");

        TextView txtBitisTarihi = findViewById(R.id.txtBitisTarihi);
        TextView txtMacAdress = findViewById(R.id.textView_macadress);
        TextView txtDate = findViewById(R.id.textView_dates);

        final TextView txtSaat = findViewById(R.id.textView_time);
        today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        System.out.println("kjşsbds "+bitisTarihi);
        if(StringUtils.isEmpty(bitisTarihi)){
            bitisTarihi="Limitsiz Kullanıcı";
         }

        txtBitisTarihi.setText("Bitiş Tarihiniz : "+bitisTarihi);
        txtMacAdress.setText("Mac Adresiniz : "+getMacAddr());
        txtSaat.setText(today.format("%k:%M"));
        txtDate.setText(today.monthDay+"-"+ today.month+"-"+today.year);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
                today.setToNow();
                txtSaat.setText(today.format("%k:%M"));
                handler.postDelayed(this, 30000);
            }
        }, 50000);


        Button btnFilmler = findViewById(R.id.btnDiziler);
          btnFilmler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               vodType="vod";
                Intent i =new Intent(VodORStreamActivity.this,VodCatGridList.class);
                startActivity(i);

             /*   if(kayıtlı_kullanıcı("tip").equals("liste")){
                    Intent i =new Intent(VodORStreamActivity.this,VodCatList.class);
                    startActivity(i);
                }else  if(kayıtlı_kullanıcı("tip").equals("grid")){
                    Intent i =new Intent(VodORStreamActivity.this,VodCatGridList.class);
                    startActivity(i);
                }else{
                    Intent i =new Intent(VodORStreamActivity.this,VodCatList.class);
                    startActivity(i);
                    sakla("tip","liste");
                }*/
            }
        });


        Button btnCanli = findViewById(R.id.btnCanliTv);
        btnCanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vodType="stream";
                Intent i =new Intent(VodORStreamActivity.this, GridCatActivity.class);
                startActivity(i);
                /*  if(kayıtlı_kullanıcı("tip").equals("liste")){
                    Intent i =new Intent(VodORStreamActivity.this,ChannelCategortListActivity.class);
                    startActivity(i);
                }else  if(kayıtlı_kullanıcı("tip").equals("grid")){
                    Intent i =new Intent(VodORStreamActivity.this,GridCatActivity.class);
                    startActivity(i);
                }else{
                    Intent i =new Intent(VodORStreamActivity.this,ChannelCategortListActivity.class);
                    startActivity(i);
                    sakla("tip","liste");

                }*/
            }
        });


        Button btnSeries = findViewById(R.id.btnSeries);
        btnSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vodType="series";
                Intent i =new Intent(VodORStreamActivity.this,SeriesCatGridList.class);
                startActivity(i);
             /*   if(kayıtlı_kullanıcı("tip").equals("liste")){
                    Intent i =new Intent(VodORStreamActivity.this,SeriesCatList.class);
                    startActivity(i);
                }else  if(kayıtlı_kullanıcı("tip").equals("grid")){
                    Intent i =new Intent(VodORStreamActivity.this,SeriesCatGridList.class);
                    startActivity(i);
                }else{
                    Intent i =new Intent(VodORStreamActivity.this,SeriesCatList.class);
                    startActivity(i);
                    sakla("tip","liste");
                }*/
            }
        });


        Button btnFavorilerim = findViewById(R.id.buttonFavorilerim);
        btnFavorilerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VodORStreamActivity.this, FavoriVodOrSeriActivity.class);
                startActivity(i);

            }
        });


        Button btnAyarlar = findViewById(R.id.button_ayarlar);
        btnAyarlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VodORStreamActivity.this, AyarlarActivity.class);
                startActivity(i);

            }
        });



    }


    public  String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        String strSavedMem1 = sharedPreferences.getString(key, "");
        return strSavedMem1;

        // TODO Auto-generated method stub
    }
    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
