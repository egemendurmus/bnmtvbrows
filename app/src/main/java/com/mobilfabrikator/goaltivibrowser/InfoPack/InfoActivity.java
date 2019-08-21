package com.mobilfabrikator.goaltivibrowser.InfoPack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.mobilfabrikator.goaltivibrowser.DataPack.SeriesInfoModel;
import com.mobilfabrikator.goaltivibrowser.InfoPack.AllSeriesActivity;
import com.mobilfabrikator.goaltivibrowser.RoomDB.DatabaseClient;
import com.mobilfabrikator.goaltivibrowser.RoomDB.Task;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesInfoList;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerYoutubeActivity;
import com.squareup.picasso.Picasso;

public class InfoActivity extends AppCompatActivity {
    TextView txtYonetmen, txtYayinTarihi, txtTur, txtOzet;
    Button btnBolum, btnSezon, btnFragman, btnFavori, btnDetay;
    ImageView imageCover;
    RatingBar ratingBar;
    SeriesInfoModel seriesInfoModel;
    Task task;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seri_bilgilendirme_activity);
        txtYonetmen = findViewById(R.id.textView_yonetmen);
        txtYayinTarihi = findViewById(R.id.textView_yayinlanma_tarihi);
        txtTur = findViewById(R.id.textView_tur);
        txtOzet = findViewById(R.id.textView_ozet);
        btnBolum = findViewById(R.id.button_bolumler);
        btnFavori = findViewById(R.id.buttonFavoriler);
        btnFragman = findViewById(R.id.button_fragman);
        btnSezon = findViewById(R.id.button_sezonlar);
        btnDetay = findViewById(R.id.button_detay);

        imageCover = findViewById(R.id.imageView_cover);
        ratingBar = findViewById(R.id.ratingBar);

        taskList = new ArrayList<>();
        task = new Task();

        loadData();
        //    getTasks();


        btnSezon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent is = new Intent(InfoActivity.this, SeriesInfoList.class);
                startActivity(is);
            }
        });
        btnBolum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent is = new Intent(InfoActivity.this, AllSeriesActivity.class);
                startActivity(is);
            }
        });

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


    }

    private void loadData() {

        try {
            final Gson gson = new Gson();
            String strObj = getIntent().getStringExtra("data");
            int pst = getIntent().getIntExtra("pst", 0);
            final Type type = new TypeToken<List<SeriesInfoModel>>() {
            }.getType();
            final List<SeriesInfoModel> seriesInfoModelList = gson.fromJson(strObj, type);
            seriesInfoModel = seriesInfoModelList.get(pst);

            Picasso.with(InfoActivity.this)
                    .load(seriesInfoModel.cover)
                    .placeholder(R.drawable.tv_icon_adapter)
                    .fit().centerInside()
                    .into(imageCover);

            if (StringUtils.isNotEmpty(seriesInfoModel.director)) {
                txtYonetmen.setText(seriesInfoModel.director);
            } else {
                txtYonetmen.setText("N/A");
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.plot)) {
                txtOzet.setText(seriesInfoModel.plot);
            } else {
                txtOzet.setText("N/A");
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.releaseDate)) {
                txtYayinTarihi.setText(seriesInfoModel.releaseDate);
            } else {
                txtYayinTarihi.setText("N/A");
            }
            if (StringUtils.isNotEmpty(seriesInfoModel.genre)) {
                txtTur.setText(seriesInfoModel.genre);
            } else {
                txtTur.setText("N/A");
            }
            ratingBar.setRating((float) seriesInfoModel.rating5based);

            btnFragman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (StringUtils.isNotEmpty(seriesInfoModel.youtubeTrailer)) {
                        Intent intent = new Intent(InfoActivity.this, ExoPlayerYoutubeActivity.class);
                        ExoPlayerYoutubeActivity.YOUTUBE_VIDEO_ID = seriesInfoModel.youtubeTrailer;
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Fragman Bulunmuyor", Toast.LENGTH_LONG).show();
                    }
                }
            });

            txtOzet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOzetDialog(seriesInfoModel.plot, seriesInfoModel);
                }
            });
            btnDetay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOzetDialog(seriesInfoModel.plot, seriesInfoModel);
                }
            });

            getTasks();


        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private void saveTask() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.streamID = (seriesInfoModel.seriesId);
                task.tipi = "seri";
                task.eklimi = true;
                task.cover = seriesInfoModel.cover;
                task.header = seriesInfoModel.name;

                //    task.seriesInfoModel = seriesInfoModel;
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().taskDao().insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //   finish();
                //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                getTasks();
                btnFavori.setText("Favorilerimden Kaldır");

            }
        }

        SaveTask st = new SaveTask();
        st.execute();
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
                    taskList.clear();

                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).streamID == Integer.parseInt(SeriesInfoList.channelCatID)) {
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
                        if (tasks.get(i).streamID == (seriesInfoModel.seriesId) && task.eklimi == true) {
                            System.out.println(tasks.get(i).streamID + tasks.get(i).tipi + "--" + seriesInfoModel.seriesId + "---" + tasks.get(i).eklimi);
                            btnFavori.setText("Favorilerimden Kaldır");
                        } else {
                            btnFavori.setText("Favorilerime Ekle");
                        }
                    }
                } catch (Exception e) {

                }


                btnFavori.setText("Favorilerime Ekle");
                // getTasks();
            }
        }
        DeleteTask dt = new DeleteTask();
        dt.execute();

    }

    private void showOzetDialog(String plot, SeriesInfoModel seriesInfoModel) {

        if (StringUtils.isNotEmpty(seriesInfoModel.plot)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
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


}
