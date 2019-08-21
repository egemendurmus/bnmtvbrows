package com.mobilfabrikator.goaltivibrowser.InfoPack;


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
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.ChannelNameCategoryAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SezonOynatmaListesi;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.VideoViewActivity;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerVideoActivity;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.VideoURL;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.extensionContainer;

public class SezonFullActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    ArrayList<String> extensionList = new ArrayList<>();
    private static String urlString, videoURL;
    // URL to get contacts JSON
    Button bilgi, geri;
    ;

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
        lv = (ListView) findViewById(R.id.list);
        new SezonFullActivity.GetChannelCategory().execute();
    }


    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SezonFullActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Bundle b = getIntent().getExtras();
            String jsonStr=b.getString("json");
            channelList.clear();
            extensionList.clear();
            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);
                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        for(int y=0; y<jsonObj.length();y++){

                            JSONObject jsonObjSezon = jsonarray.getJSONObject(y);
                            String catName = jsonObjSezon.getString("title");
                            String catID = jsonObjSezon.getString("id");
                            String imageurl=null;

                            try{
                                if(jsonObjSezon.getJSONObject("info").has("movie_image")){
                                    imageurl = jsonObjSezon.getJSONObject("info").getString("movie_image");
                                }
                            }catch (Exception e){

                            }
                            channelList.add(new ChannelStreamData(catName, catID, imageurl));
                            String containerExtension = jsonObjSezon.getString("container_extension");
                            ChannelListActivity. channelList.add(new ChannelStreamData(catName, catID, imageurl));
                            extensionList.add(containerExtension);
                        }
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
            ChannelNameCategoryAdapter adaptorumuz = new ChannelNameCategoryAdapter(SezonFullActivity.this, channelList);
            lv.setAdapter(adaptorumuz);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelStreamData kisi = channelList.get(i);
                    channelIDTS = kisi.getChannelID();
                    extensionContainer ="."+extensionList.get(i);
                    VideoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/series/" + MainActivity.userName + "/" + MainActivity.password + "/"
                            + channelIDTS  + extensionContainer;

                    Intent intent = new Intent(SezonFullActivity.this, ExoPlayerVideoActivity.class);
                    ExoPlayerVideoActivity.videoURL=VideoURL;
                    startActivity(intent);

/*
                    if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {

                        isAppInstalled("com.mxtech.videoplayer.ad");



                    } else {

                        VideoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/movie/" + MainActivity.userName + "/" + MainActivity.password + "/"
                                + channelIDTS  + extensionContainer;
                        Intent iy = new Intent(SezonFullActivity.this, VideoViewActivity.class);
                        Uri videoUri = Uri.parse(VideoURL);
                        iy.setDataAndType(videoUri, "video/*");


                        startActivity(iy);

                    }*/


                }
            });

            /**
             * Updating parsed JSON data into ListView
             * */

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
            videoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/series/" + MainActivity.userName + "/" + MainActivity.password + "/"
                    + channelIDTS + "." + extensionContainer;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        channelList.clear();
        extensionList.clear();
    }
}

