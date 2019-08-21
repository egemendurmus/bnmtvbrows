package com.mobilfabrikator.goaltivibrowser.InfoPack;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.ChannelNameCategoryAdapter;
import com.mobilfabrikator.goaltivibrowser.AdapterPack.ChannelNameCategoryGridAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.DataPack.SeriesInfoModel;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesChannelGridList;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesInfoList;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SezonOynatmaListesi;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerVideoActivity;

import static com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesInfoList.channelCatID;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.VideoURL;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.extensionContainer;

public class AllSeriesActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private GridView lv;
    final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    final List<String> idList = new ArrayList<>();
    final List<String> container_extensionList = new ArrayList<>();

    // ArrayList<String> extensionList = new ArrayList<>();
    private static String urlString;
    // URL to get contacts JSON
    Button bilgi, geri;
    JSONObject jsonObject;
    String sezonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_grid_cat);
     /*   bilgi = (Button) findViewById(R.id.button);
        bilgi.setVisibility(View.INVISIBLE);*/
        geri = (Button) findViewById(R.id.button2);


        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelList.clear();
                onBackPressed();
            }
        });
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                MainActivity.userName + "&password=" + MainActivity.password + "&action=get_series_info&series_id=" + channelCatID;

        lv = (GridView) findViewById(R.id.gridview);
        new AllSeriesActivity.GetChannelCategory().execute();
    }
    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AllSeriesActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);
            Log.e(TAG, urlString+" Response from url: " + jsonStr);
            channelList.clear();
            idList.clear();
            container_extensionList.clear();
            // extensionList.clear();
            if (jsonStr != null) {
                try {
                    jsonObject = new JSONObject(jsonStr);
                    // etting JSON Array node
                    for (int i = 0; i < jsonObject.getJSONObject("episodes").length(); i++) {
                        sezonNo = String.valueOf(i+1);
                        String imageurl = "";
                        for(int y = 0 ; y<jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(i).length();y++){
                            String title=  jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getString("title");
                            String id= jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getString("id");
                            String container_extension= jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getString("container_extension");
                            for(int zz = 0 ; zz<jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getJSONObject("info").length();zz++){
                                if (jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getJSONObject("info").has("movie_image")) {
                                    imageurl=jsonObject.getJSONObject("episodes").getJSONArray(sezonNo).getJSONObject(y).getJSONObject("info").getString("movie_image");
                                }
                            }
                            channelList.add(new ChannelStreamData(title,title,imageurl));
                            idList.add(id);
                            container_extensionList.add(container_extension);
                        }
                        /*   String sezon = String.valueOf(i+1 +".Sezon");
                         */

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
            ChannelNameCategoryGridAdapter adaptorumuz = new ChannelNameCategoryGridAdapter(AllSeriesActivity.this, channelList);
            lv.setAdapter(adaptorumuz);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelStreamData kisi = channelList.get(i);
                    channelIDTS = kisi.getChannelID();
                    // extensionContainer ="."+extensionList.get(i);

                    try {
                        VideoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/series/" + MainActivity.userName + "/" + MainActivity.password + "/"
                                + idList.get(i)  +"."+ container_extensionList.get(i);

                        Intent intent = new Intent(AllSeriesActivity.this, ExoPlayerVideoActivity.class);
                        ExoPlayerVideoActivity.videoURL=VideoURL;
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}
