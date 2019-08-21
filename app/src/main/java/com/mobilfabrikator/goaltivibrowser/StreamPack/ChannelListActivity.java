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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.VideoAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.VideoViewActivity;
import com.mobilfabrikator.goaltivibrowser.R;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;

public class ChannelListActivity extends Activity {
    private String TAG = MainActivity.class.getSimpleName();
    boolean value;

    private ProgressDialog pDialog;
    private ListView lv;
    public static List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    public static String channelCatID;
    private static String urlString, videoURL;
    // URL to get contacts JSON
    Button bilgi, geri;
    ;
    public static int getPos;

    public static ArrayList ChannelList = new ArrayList();

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
        if (width > height) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_channel_categort_list);
        bilgi = (Button) findViewById(R.id.button);
        bilgi.setVisibility(View.INVISIBLE);
        geri = (Button) findViewById(R.id.button2);
        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" + MainActivity.userName +
                "&password=" + MainActivity.password + "&action=get_live_streams&category_id=" + channelCatID;
        Log.e(TAG, "R url: " + urlString);
        value = getPackageManager().hasSystemFeature("android.hardware.touchscreen");
        lv = (ListView) findViewById(R.id.list);
        lv.setItemsCanFocus(true);
        new GetChannelCategory().execute();
    }

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ChannelListActivity.this);
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
            ChannelList.clear();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        String catName = jsonObj.getString("name");
                        String catID = jsonObj.getString("stream_id");
                        String imageurl = jsonObj.getString("stream_icon");
                        channelList.add(new ChannelStreamData(catName, catID, imageurl));
                        ChannelList.add("http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/live/" + MainActivity.userName + "/" + MainActivity.password + "/" + catID + ".ts");
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
            VideoAdapter adaptorumuz = new VideoAdapter(ChannelListActivity.this, channelList);
            lv.setAdapter(adaptorumuz);

            lv.setSelection(0);
            lv.requestFocus();
            pDialog.dismiss();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelStreamData kisi = channelList.get(i);
                    channelIDTS = kisi.getChannelID();
                    getPos = i;

                    if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {


                        if (value == false) {
                            Intent iy = new Intent(ChannelListActivity.this, VideoViewActivity.class);
                            startActivity(iy);
                        } else {
                            isAppInstalled("com.mxtech.videoplayer.ad");

                        }


                    } else {
                        Intent iy = new Intent(ChannelListActivity.this, VideoViewActivity.class);
                        startActivity(iy);
                        Log.e(TAG, "Response from url: " + kisi.getChannelName());
                    }


                }
            });


        }

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

            for (int i = 0; i < ChannelList.size(); i++) {
                Uri videoUris = Uri.parse(String.valueOf(ChannelList.get(i)));
                //   intent.putExtra( EXTRA_VIDEO_LIST, new Parcelable[] {(Parcelable) ChannelList.get(i)} );

            }
            intent.setDataAndType(videoUri, "application/x-mpegURL");

            intent.setPackage("com.mxtech.videoplayer.ad"); // com.mxtech.videoplayer.pro
            intent.putExtra("return_result", true);

            startActivityForResult(intent, 0);
            onActivityResult(200, 200, intent);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "Response from url data: " + data);


        if (resultCode == 200)  // -1 RESULT_OK : Playback was completed or stopped by user request.
            //Activity.RESULT_CANCELED: User canceled before starting any playback.
            //RESULT_ERROR (=Activity.RESULT_FIRST_USER): Last playback was ended with an error.

            if (data.getAction().equals("android.intent.action.VIEW")) {
                //data.getData()
                int pos = data.getIntExtra("position", -1); // Last playback position in milliseconds. This extra will not exist if playback is completed.
                int dur = data.getIntExtra("duration", -1); // Duration of last played video in milliseconds. This extra will not exist if playback is completed.
                String cause = data.getStringExtra("end_by"); //  Indicates reason of activity closure.


            }
    }

}
