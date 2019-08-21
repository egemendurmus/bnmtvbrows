package com.mobilfabrikator.goaltivibrowser.StreamPack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.ChannelNameCategoryGridAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.VideoViewActivity;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;

public class GridChannelListActivity extends Activity {
    GridView gridview;
    private ProgressDialog pDialog;
    String urlString,videoURL;
    private String TAG = MainActivity.class.getSimpleName();
    final List<ChannelStreamData> channelList=new ArrayList<ChannelStreamData>();
    boolean value;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        if(width>height){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_grid_cat);
        TextView textView = findViewById(R.id.textView);
        textView.setText("Kanal Listesi");
        Button b6 = findViewById(R.id.button6);
        b6.setVisibility(View.INVISIBLE);

        gridview = findViewById(R.id.gridview);
        urlString = "http://"+MainActivity.MainUrl+":"+MainActivity.MainPort+"/player_api.php?username="+MainActivity.userName+"&password="+MainActivity.password+"&action=get_live_streams&category_id="+ ChannelListActivity.channelCatID;

        new GetChannelCategory().execute();
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        value=getPackageManager().hasSystemFeature("android.hardware.touchscreen");


    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        Log.d("hello", String.valueOf(e.getKeyCode()));

        try {

            if (e.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                Intent i = new Intent(GridChannelListActivity.this, VodORStreamActivity.class);
                startActivity(i);
                finish();

                return true;
            }
        } catch (Exception ez) {
            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
        }


        return super.dispatchKeyEvent(e);
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

    private void OpenMxPayer() {
        try {
            videoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/live/" + MainActivity.userName + "/" + MainActivity.password + "/" + channelIDTS + ".ts";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.mxtech.videoplayer.ad");
            intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
            Uri videoUri = Uri.parse(videoURL);
            intent.setDataAndType(videoUri, "application/x-mpegURL");
            intent.setPackage("com.mxtech.videoplayer.ad"); // com.mxtech.videoplayer.pro
            intent.putExtra("return_result", true);
//startActivity(intent);
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException e) {

        }

    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
            System.out.println(" yüklü");
            OpenMxPayer();

        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            System.out.println(" değil");
            OpenStoreIntent();

        }
        return installed;
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

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(GridChannelListActivity.this);
            pDialog.setMessage("Yükleniyor");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);
            channelList.clear();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj =  jsonarray.getJSONObject(i);
                        String catName=jsonObj.getString("name");
                        String catID=jsonObj.getString("stream_id");
                        String imageurl=jsonObj.getString("stream_icon");
                        channelList.add(new ChannelStreamData(catName,catID,imageurl));



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
            ChannelNameCategoryGridAdapter adaptorumuz=new ChannelNameCategoryGridAdapter(GridChannelListActivity.this, channelList);
            gridview.setAdapter(adaptorumuz);
            if (width > height) {
                gridview.setSelection(0);
            }
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelStreamData kisi = channelList.get(i);
                    channelIDTS=kisi.getChannelID();


                    Log.e(TAG, "Response from url: " +kisi.getChannelName() );
                    if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {
                        ChannelListActivity channelListActivity=new ChannelListActivity();


                        if (value == false) {
                            Intent iy = new Intent(GridChannelListActivity.this, VideoViewActivity.class);
                            startActivity(iy);
                        } else {
                            isAppInstalled("com.mxtech.videoplayer.ad");

                        }                    }else{
                        Intent iy =new Intent(GridChannelListActivity.this,VideoViewActivity.class);
                        startActivity(iy);
                        Log.e(TAG, "Response from url: " +kisi.getChannelName() );
                    }



                }
            });

            /**
             * Updating parsed JSON data into ListView
             * */

        }

    }
}
