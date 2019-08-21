package com.mobilfabrikator.goaltivibrowser.VodPack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.InfoPack.InfoActivity;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

public class VodInfoServisActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    // ArrayList<String> extensionList = new ArrayList<>();
    public static String channelCatID;
    private static String urlString;
    // URL to get contacts JSON
    Button bilgi, geri;
    JSONObject jsonObject;
    String sezonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
      /*  Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (width > height) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        setContentView(R.layout.activity_channel_categort_list);
        bilgi = findViewById(R.id.button);
        bilgi.setVisibility(View.INVISIBLE);
        geri = findViewById(R.id.button2);


        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelList.clear();
                onBackPressed();
            }
        });
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                MainActivity.userName + "&password=" + MainActivity.password + "&action=get_vod_info&vod_id=" + channelCatID;

        lv = findViewById(R.id.list);
        new GetChannelCategory().execute();
    }


    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(VodInfoServisActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + urlString);
            channelList.clear();
            // extensionList.clear();

            if (jsonStr != null) {
                try {
                    jsonObject = new JSONObject(jsonStr);

                    // Getting JSON Array node
                /*    for (int i = 0; i < jsonObject.getJSONObject("episodes").length(); i++) {

                        String sezon = String.valueOf(i+1 +".Sezon");
                        sezonNo = String.valueOf(i+1);
                        String imageurl = "";

                        channelList.add(new ChannelStreamData(sezon,sezonNo,imageurl));

                    }*/

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
         try {

                Intent sezonOynatma = new Intent(VodInfoServisActivity.this, InfoActivity.class);
                sezonOynatma.putExtra("data",jsonObject.toString());
                startActivity(sezonOynatma);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        Log.d("hello", String.valueOf(e.getKeyCode()));

        try {

            if (e.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                Intent i = new Intent(VodInfoServisActivity.this, VodORStreamActivity.class);
                startActivity(i);
                finish();

                return true;
            }
        } catch (Exception ez) {
            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
        }


        return super.dispatchKeyEvent(e);
    }


}
