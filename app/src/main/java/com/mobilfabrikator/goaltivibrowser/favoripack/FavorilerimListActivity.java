package com.mobilfabrikator.goaltivibrowser.favoripack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.GridFavorilerimAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.DataPack.SeriesInfoModel;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.InfoPack.InfoActivity;
import com.mobilfabrikator.goaltivibrowser.InfoPack.VodServisInfoActivity;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.RoomDB.DatabaseClient;
import com.mobilfabrikator.goaltivibrowser.RoomDB.Task;
import com.mobilfabrikator.goaltivibrowser.SeriesPack.SeriesInfoList;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.R;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;

public class FavorilerimListActivity extends AppCompatActivity {
    final List<Task> favoriList = new ArrayList<Task>();
    final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    Intent intentFilters;
    MaterialButton inMenuArama, textArama;
    ConstraintLayout aramall;
    EditText edittextArama;
    GridView gridview;
    String jsonStr, jsonObjStr, urlString;
    ArrayList<String> extensionList = new ArrayList<>();
    GridFavorilerimAdapter adaptorumuz;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorilerim_list);
        getTasks();
        intentFilters = getIntent();
        inMenuArama = findViewById(R.id.button_kanal_arama);
        edittextArama = findViewById(R.id.editText_arama);
        aramall = (findViewById(R.id.arama_ll));
        gridview = findViewById(R.id.gridview);

        inMenuArama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aramall.getVisibility() == View.INVISIBLE || aramall.getVisibility() == View.GONE) {
                    aramall.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .repeat(0)
                            .playOn(aramall);
                    edittextArama.setFocusableInTouchMode(true);
                    edittextArama.requestFocus();
                    isKeyBoardOpen(FavorilerimListActivity.this);

                } else {
                    aramall.setVisibility(View.GONE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .repeat(0)
                            .playOn(aramall);
                    isKeyBoardOpen(FavorilerimListActivity.this);

                }
            }
        });
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
                    loadTaskList(tasks);
                } catch (Exception e) {

                }
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void loadTaskList(List<Task> tasks) {
        favoriList.clear();
        for (int i = 0; i < tasks.size(); i++) {
            /*
            if (intentFilters.getStringExtra("tipi").equals("seri")) {
                favoriList.add(tasks.get(i));
            }
            if (intentFilters.getStringExtra("tipi").equals("film")) {
                favoriList.add(tasks.get(i));
            }*/
            if (intentFilters.getStringExtra("tipi").equals(tasks.get(i).tipi)) {
                favoriList.add(tasks.get(i));
            }
        }


        adaptorumuz = new GridFavorilerimAdapter
                (FavorilerimListActivity.this, favoriList);
        gridview.setAdapter(adaptorumuz);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task favorilistesi = adaptorumuz.getItem(position);


                VodServisInfoActivity.channelCatID = String.valueOf(favorilistesi.streamID);
                channelIDTS = String.valueOf(favorilistesi.streamID);
                SeriesInfoList.channelCatID = String.valueOf(favorilistesi.streamID);

                if (intentFilters.getStringExtra("tipi").equals("film")) {
                    Intent i = new Intent(FavorilerimListActivity.this, VodServisInfoActivity.class);
                    startActivity(i);
                } else {
                    new GetChannelCategory().execute();
                }
            }
        });

        edittextArama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = edittextArama.getText().toString().toLowerCase(Locale.getDefault());
                adaptorumuz.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void isKeyBoardOpen(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
        }
    }

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(FavorilerimListActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                    MainActivity.userName + "&password=" + MainActivity.password + "&action=get_series&category_id=" +
                    ChannelListActivity.channelCatID;


            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(urlString);

            channelList.clear();
            extensionList.clear();

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        jsonObjStr = String.valueOf(jsonObj);
                        String catName = jsonObj.getString("name");
                        String catID = jsonObj.getString("series_id");
                        String imageurl = jsonObj.getString("cover");
                        String containerExtension = jsonObj.getString("youtube_trailer");
                        channelList.add(new ChannelStreamData(catName, catID, imageurl));
                        extensionList.add(containerExtension);


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
            GsonBuilder gsonBuilder = new GsonBuilder();
            final Gson gson = gsonBuilder.create();
            final Type type = new TypeToken<List<SeriesInfoModel>>() {
            }.getType();

            if (pDialog.isShowing())
                pDialog.dismiss();

            final List<SeriesInfoModel> seriesInfoModelList = gson.fromJson(jsonStr, type);

            String jsonData = gson.toJson(seriesInfoModelList, type);
            int passValue = 0;
            for (int z = 0; z < seriesInfoModelList.size(); z++) {
                for (int a = 0; a < favoriList.size(); a++) {
                    if (seriesInfoModelList.get(z).seriesId == Integer.parseInt(VodServisInfoActivity.channelCatID)) {
                        passValue = z;

                    }
                }
            }
            Intent i = new Intent(FavorilerimListActivity.this, InfoActivity.class);
            i.putExtra("data", jsonData);
            i.putExtra("pst", passValue);
            startActivity(i);

        }

    }

}
