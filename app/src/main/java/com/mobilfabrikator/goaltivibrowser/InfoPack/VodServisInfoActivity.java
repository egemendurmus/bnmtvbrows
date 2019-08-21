package com.mobilfabrikator.goaltivibrowser.InfoPack;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.DataPack.MovieData;
import com.mobilfabrikator.goaltivibrowser.DataPack.VodSeriesInfoModel;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.RoomDB.DatabaseClient;
import com.mobilfabrikator.goaltivibrowser.RoomDB.Task;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerVideoActivity;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerYoutubeActivity;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.extensionContainer;

public class VodServisInfoActivity extends AppCompatActivity {
    public static String channelCatID, title;
    private static String urlString;
    private final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    TextView txtYonetmen, txtYayinTarihi, txtTur, txtOzet;
    Button btnBolum, btnSezon, btnFragman, btnFavori, btnDetay;
    ImageView imageCover;
    RatingBar ratingBar;
    private ProgressDialog pDialog;
    private JSONObject jsonObject, movieExtensionJsonObject;
    Task task;
    List<Task> taskList;
    private VodSeriesInfoModel.Info seriesInfoModel;
    private MovieData movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.film_bilgilendirme);
        txtYonetmen = findViewById(R.id.textView_yonetmen);
        txtYayinTarihi = findViewById(R.id.textView_yayinlanma_tarihi);
        txtTur = findViewById(R.id.textView_tur);
        txtOzet = findViewById(R.id.textView_ozet);
        // btnBolum = findViewById(R.id.button_bolumler);
        btnFavori = findViewById(R.id.buttonFavoriler);
        btnFragman = findViewById(R.id.button_fragman);
        btnSezon = findViewById(R.id.button_sezonlar);
        btnDetay = findViewById(R.id.button_detay);

        imageCover = findViewById(R.id.imageView_cover);
        ratingBar = findViewById(R.id.ratingBar);
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                MainActivity.userName + "&password=" + MainActivity.password + "&action=get_vod_info&vod_id=" + channelCatID;
        task = new Task();
        taskList = new ArrayList<>();
        getTasks();
        new GetChannelCategory().execute();
        btnFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnFavori.getText().toString().equals("Favorilerimden Kaldır")) {
                    deleteTask(task);
                } else {
                    saveTask();
                }
            }
        });

      /*  btnSezon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent is = new Intent(VodServisInfoActivity.this, SeriesInfoList.class);
                startActivity(is);
                ;
            }
        });*/
      /*  btnBolum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent is = new Intent(VodServisInfoActivity.this, AllSeriesActivity.class);
                startActivity(is);
            }
        });
*/
//        btnBolum.setVisibility(View.GONE);
        btnFragman.setVisibility(View.VISIBLE);
        btnFragman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isNotEmpty(seriesInfoModel.youtubeTrailer)) {
                    Intent intent = new Intent(VodServisInfoActivity.this, ExoPlayerYoutubeActivity.class);
                    ExoPlayerYoutubeActivity.YOUTUBE_VIDEO_ID = seriesInfoModel.youtubeTrailer;
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Fragman Bulunmuyor", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSezon.setText("Oynat");


    }

    private void loadData(JSONObject jsonObject) {

        try {
            JSONObject jsonObjectInfo = new JSONObject();
            final Gson gson = new Gson();
            jsonObjectInfo = jsonObject.getJSONObject("info");
            movieExtensionJsonObject = jsonObject.getJSONObject("movie_data");

            String strObj = jsonObjectInfo.toString();
            String strObjMovie = movieExtensionJsonObject.toString();


            seriesInfoModel = gson.fromJson(strObj, VodSeriesInfoModel.Info.class);
            movieData = gson.fromJson(strObjMovie, MovieData.class);

            //       final SeriesInfoModel seriesInfoModel = seriesInfoModelList.get(pst);


            if (!StringUtils.isBlank(seriesInfoModel.movieImage)) {
                Picasso.with(VodServisInfoActivity.this)
                        .load(seriesInfoModel.movieImage)
                        .placeholder(R.drawable.tv_icon_adapter)
                        .fit().centerInside()
                        .into(imageCover);
            }


            if (StringUtils.isNotEmpty(seriesInfoModel.director)) {
                txtYonetmen.setText(seriesInfoModel.director);
            } else {
                txtYonetmen.setText("N/A");
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.plot)) {
                txtOzet.setText(seriesInfoModel.plot);
                btnDetay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOzetDialog(seriesInfoModel.plot);
                    }
                });
            } else {
                txtOzet.setText("N/A");
                btnDetay.setVisibility(View.INVISIBLE);
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.releasedate)) {
                txtYayinTarihi.setText(seriesInfoModel.releasedate);
            } else {
                txtYayinTarihi.setText("N/A");
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.genre)) {
                txtTur.setText(seriesInfoModel.genre);
            } else {
                txtTur.setText("N/A");
            }

            if (!StringUtils.isBlank(seriesInfoModel.rating)) {
                double rates = Double.parseDouble(seriesInfoModel.rating);
                rates = rates / 2;
                ratingBar.setRating((float) rates);
            }


            btnFragman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isNotEmpty(seriesInfoModel.youtubeTrailer)) {
                        Intent intent = new Intent(VodServisInfoActivity.this, ExoPlayerYoutubeActivity.class);
                        ExoPlayerYoutubeActivity.YOUTUBE_VIDEO_ID = seriesInfoModel.youtubeTrailer;
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Fragman Bulunmuyor", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnSezon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {

                        isAppInstalled("com.mxtech.videoplayer.ad");


                    } else {
                        Intent is = new Intent(VodServisInfoActivity.this, ExoPlayerVideoActivity.class);
                        ExoPlayerVideoActivity.videoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort
                                + "/movie/" +
                                MainActivity.userName + "/" + MainActivity.password + "/"
                                + movieData.streamId + "." + movieData.containerExtension;
                        startActivity(is);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
        getTasks();


    }

    private void saveTask() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.streamID = Integer.parseInt((channelCatID));
                task.tipi = "film";
                task.eklimi = true;
                task.cover = seriesInfoModel.movieImage;
                task.header = title;
                task.extension = extensionContainer;
                //    task.seriesInfoModel = seriesInfoModel;
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //   finish();
                //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //  Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                getTasks();
                btnFavori.setText("Favorilerimden Kaldır");

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    private void showOzetDialog(String plot) {

        if (StringUtils.isNotEmpty(plot)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VodServisInfoActivity.this);
            builder.setTitle("GoalTivi");
            builder.setMessage(plot);
            builder.setNegativeButton("İPTAL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

                }
            });


            builder.setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Tamam butonuna basılınca yapılacaklar

                }
            });


            builder.show();
        }
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);

                try {
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).streamID == (Integer.parseInt(channelCatID))) {
                            //    taskList.add(tasks.get(i));
                            task = tasks.get(i);
                            if (task.eklimi == true) {
                                btnFavori.setText("Favorilerimden Kaldır");
                            } else {
                                btnFavori.setText("Favorilerime Ekle");
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void deleteTask(final Task task) {
        class DeleteTask extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .delete(task);
                return null;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                try {
                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).streamID == (Integer.parseInt(channelCatID)) && task.eklimi == true) {
                            btnFavori.setText("Favorilerimden Kaldır");
                        } else {
                            btnFavori.setText("Favorilerime Ekle");
                        }
                    }
                } catch (Exception e) {

                }

                // Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                btnFavori.setText("Favorilerime Ekle");
            }
        }
        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(VodServisInfoActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);
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
            loadData(jsonObject);
            getTasks();


        }

    }

    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        String strSavedMem1 = sharedPreferences.getString(key, "");
        return strSavedMem1;

        // TODO Auto-generated method stub
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
            urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort
                    + "/movie/" +
                    MainActivity.userName + "/" + MainActivity.password + "/"
                    + movieData.streamId + "." + movieData.containerExtension;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.mxtech.videoplayer.ad");
            intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
            Uri videoUri = Uri.parse(urlString);
            intent.setDataAndType(videoUri, "application/x-mpegURL");
            intent.setPackage("com.mxtech.videoplayer.ad"); // com.mxtech.videoplayer.pro
            intent.putExtra("return_result", true);
//startActivity(intent);
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException e) {

        }

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

}
