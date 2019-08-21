package com.mobilfabrikator.goaltivibrowser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesCatGridList;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesCatList;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelCategortListActivity;
import com.mobilfabrikator.goaltivibrowser.StreamPack.GridCatActivity;
import com.mobilfabrikator.goaltivibrowser.VodPack.VodCatGridList;
import com.mobilfabrikator.goaltivibrowser.VodPack.VodCatList;

public class infoActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();
    private CheckBox chkIos, chkAndroid, chkWindows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if(width>height){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_info);


        chkIos = findViewById(R.id.checkBox_mxplayer);
     /*   chkIos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                   sakla("mxplayer","ok");
                }else{
                    deletePreferences("mxplayer");
                }

            }
        });*/
     chkIos.setEnabled(false);


        chkAndroid = findViewById(R.id.checkBox2);
        chkAndroid.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    sakla("tip","liste");
                    chkWindows.setChecked(false);
                }else{
                    deletePreferences("tip");
                }

            }
        });

        chkWindows = findViewById(R.id.checkBox3);
        chkWindows.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    sakla("tip","grid");
                    chkAndroid.setChecked(false);

                }else{
                    deletePreferences("tip");
                }

            }
        });
        if(kayıtlı_kullanıcı("tip").equals("grid")){
            chkAndroid.setChecked(false);
            chkWindows.setChecked(true);

        }
        if(kayıtlı_kullanıcı("tip").equals("liste")){
            chkAndroid.setChecked(true);
            chkWindows.setChecked(false);

        }
        if(kayıtlı_kullanıcı("mxplayer").equals("ok")){
            chkIos.setChecked(true);


        }


        Button b5 = findViewById(R.id.btnSeries);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(kayıtlı_kullanıcı("tip").equals("liste")){

                 if(VodORStreamActivity.vodType=="stream"){
                     Intent i =new Intent(infoActivity.this,ChannelCategortListActivity.class);
                     startActivity(i);
                 }
                   if(VodORStreamActivity.vodType=="vod"){
                       Intent i =new Intent(infoActivity.this,VodCatList.class);
                       startActivity(i);
                   }
                   if(VodORStreamActivity.vodType=="series"){
                       Intent i =new Intent(infoActivity.this,SeriesCatList.class);
                       startActivity(i);
                   }
               }else  if(kayıtlı_kullanıcı("tip").equals("grid")){


                   if(VodORStreamActivity.vodType=="stream"){

                       Intent i =new Intent(infoActivity.this,GridCatActivity.class);
                       startActivity(i);
                   }
                   if(VodORStreamActivity.vodType=="vod"){

                       Intent i =new Intent(infoActivity.this,VodCatGridList.class);
                       startActivity(i);
                   }

                   if(VodORStreamActivity.vodType=="series"){
                       Intent i =new Intent(infoActivity.this,SeriesCatGridList.class);
                       startActivity(i);
                   }
               }


            }
        });




    }
    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        // dosyaya yazmamza yardmc olacak bir tane editr oluturduk.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editr aracl ile key value deerlerini yazdk.
        // ben string kaydettim ama farkl veri tipleri de kaydediliyor.
        editor.putString(key, value);
        // Farkl veri tiplerine rnek
        // editor.putBoolean("boolean", true);
        // editor.putFloat("float", (float) 123.23);
        // editor.putInt("int", 23);
        // editor.putLong("long", 123134243);

        /*
         * Serilize edilmi dosyay aadaki gibi kaydedebilirz.
         * ArrayList<String> al=new SharedPreferencesSampleActivity();
         * editor.putString(ARRAY_LIST_TAG, ObjectSerializer.serialize(al));
         */

        // bilgileri kaydettim.
        editor.commit();
    }
    private void deletePreferences(String key) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                Context.MODE_PRIVATE);
        // dosyaya yazmamï¿½za yardï¿½mcï¿½ olacak bir tane editï¿½r
        // oluï¿½turduk.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editï¿½r aracï¿½lï¿½ï¿½ï¿½ ile key value deï¿½erlerini yazdï¿½k.
        // ben string kaydettim ama farklï¿½ veri tipleri de kaydediliyor.

        // Farklï¿½ veri tiplerine ï¿½rnek
        // editor.putBoolean("boolean", true);
        // editor.putFloat("float", (float) 123.23);
        // editor.putInt("int", 23);
        // editor.putLong("long", 123134243);

        editor.putString(key, "");

        editor.commit();

    }
    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        // key deerini vererek value deerini aldm.
        String strSavedMem1 = sharedPreferences.getString(key, "");

        /*
         * Serilize edilmi dosyay aadaki gibi okuruz. return
         * (ArrayList<String>)
         * ObjectSerializer.deserialize(sharedPreferences.getString(key, ));
         */

        return strSavedMem1;

        // TODO Auto-generated method stub
    }

}
