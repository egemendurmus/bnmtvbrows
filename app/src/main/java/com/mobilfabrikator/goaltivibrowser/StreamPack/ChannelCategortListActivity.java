package com.mobilfabrikator.goaltivibrowser.StreamPack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.OzelAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelCategoryData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.infoActivity;
import com.mobilfabrikator.goaltivibrowser.R;

public class ChannelCategortListActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    Button bilgi, geri;
    final List<ChannelCategoryData> channelList = new ArrayList<ChannelCategoryData>();
//    private static String urlString = "http://iptv.nexa.space:8000/player_api.php?username=egementst&password=564fdtr&action=get_live_categories";

    // URL to get contacts JSON
    private static String urlString;
    ArrayList<HashMap<String, String>> contactList;

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
        setContentView(R.layout.activity_channel_categort_list);


        lv = (ListView) findViewById(R.id.list);
        bilgi = (Button) findViewById(R.id.button);
        geri = (Button) findViewById(R.id.button2);

        // urlString = "http://iptv.nexa.space:8000/player_api.php?username="+MainActivity.userName+"&password="+MainActivity.password+"&action=get_live_categories";

        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" + MainActivity.userName +
                "&password=" + MainActivity.password + "&action=get_live_categories";

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bilgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChannelCategortListActivity.this, infoActivity.class);
                startActivity(i);
            }
        });
        new GetChannelCategory().execute();
    }


    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ChannelCategortListActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);


            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        String catName = jsonObj.getString("category_name");
                        String catID = jsonObj.getString("category_id");
                        channelList.add(new ChannelCategoryData(catName, catID));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            OzelAdapter adaptorumuz = new OzelAdapter(ChannelCategortListActivity.this, channelList);
            lv.setAdapter(adaptorumuz);
            if (adaptorumuz.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "Kullanıcı Adın ve Şifreni Kontrol Et", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ChannelCategortListActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                sakla("password", MainActivity.password);
                sakla("username", MainActivity.userName);
                sakla("port", MainActivity.MainPort);
                sakla("MainUrl", MainActivity.MainUrl);


            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelCategoryData kisi = channelList.get(i);
                    ChannelListActivity.channelCatID = kisi.getCategoryID();

                    if (kisi.getCategoryName().equals("FOR ADULTS")) {
                        showDialog(ChannelCategortListActivity.this, null);
                    } else {
                        if (kayıtlı_kullanıcı("tip").equals("liste")) {

                            Intent is = new Intent(ChannelCategortListActivity.this, ChannelListActivity.class);
                            startActivity(is);
                        } else if (kayıtlı_kullanıcı("tip").equals("grid")) {
                            Intent is = new Intent(ChannelCategortListActivity.this, GridCatActivity.class);
                            startActivity(is);
                            ;
                        }

                    }


                    Log.e(TAG, "Response from url: " + kisi.getCategoryName());

                }
            });

            /**
             * Updating parsed JSON data into ListView
             * */
            if (kayıtlı_kullanıcı("tip").equals("grid")) {
                Toast.makeText(ChannelCategortListActivity.this,
                        "Bro, try Android :)", Toast.LENGTH_LONG).show();
            }

        }

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

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        final EditText et = dialog.findViewById(R.id.editText);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = et.getText().toString();
                if (pass.equals("2580")) {
                    if (kayıtlı_kullanıcı("tip").equals("liste")) {
                        Intent is = new Intent(ChannelCategortListActivity.this, ChannelListActivity.class);
                        startActivity(is);
                    } else if (kayıtlı_kullanıcı("tip").equals("grid")) {
                        Intent is = new Intent(ChannelCategortListActivity.this, GridCatActivity.class);
                        startActivity(is);
                        ;
                    }
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }
}


