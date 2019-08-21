package com.mobilfabrikator.goaltivibrowser.SeriesPack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.mobilfabrikator.goaltivibrowser.AdapterPack.ChannelNameCategoryGridAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.DataPack.SeriesInfoModel;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.InfoPack.InfoActivity;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.R;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;
import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.extensionContainer;

public class SeriesChannelGridList extends AppCompatActivity {
    final List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    GridView gridview;
    String urlString, videoURL;
    ArrayList<String> extensionList = new ArrayList<>();
    String jsonStr, jsonObjStr;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    MaterialButton inMenuArama, textArama;
    ConstraintLayout aramall;
    EditText edittextArama;
    int passValue;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
     /*   if(width>height){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }      */
        //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_grid_cat);
        TextView textView = findViewById(R.id.textView);
        textView.setText("Film Listesi");
        Button b6 = findViewById(R.id.button6);
        b6.setVisibility(View.INVISIBLE);

        gridview = findViewById(R.id.gridview);
        gridview.requestFocus();
        gridview.setSelection(0);
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                MainActivity.userName + "&password=" + MainActivity.password + "&action=get_series&category_id=" +
                ChannelListActivity.channelCatID;

        new GetChannelCategory().execute();
        Button b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        inMenuArama = findViewById(R.id.button_kanal_arama);
        edittextArama = findViewById(R.id.editText_arama);
        aramall = (findViewById(R.id.arama_ll));


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
                    isKeyBoardOpen(SeriesChannelGridList.this);
                } else {
                    aramall.setVisibility(View.GONE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .repeat(0)
                            .playOn(aramall);
                    isKeyBoardOpen(SeriesChannelGridList.this);

                }
            }
        });


    }

    private void deletePreferences(String key) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                Context.MODE_PRIVATE);
        // dosyaya yazmamï¿½za yardï¿½mcï¿½ olacak bir tane editï¿½r
        // oluï¿½turduk.
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putString(key, "");

        editor.commit();

    }

    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        // bilgileri kaydettim.
        editor.commit();
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
            videoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/movie/" + MainActivity.userName + "/" + MainActivity.password + "/"
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

    public void isKeyBoardOpen(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
        }
    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        Log.d("hello", String.valueOf(e.getKeyCode()));

        try {

            if (e.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                Intent i = new Intent(SeriesChannelGridList.this, VodORStreamActivity.class);
                startActivity(i);
                finish();

                return true;
            }
        } catch (Exception ez) {
            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
        }


        return super.dispatchKeyEvent(e);
    }

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(SeriesChannelGridList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

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
            final ChannelNameCategoryGridAdapter adaptorumuz = new ChannelNameCategoryGridAdapter(SeriesChannelGridList.this, channelList);
            gridview.setAdapter(adaptorumuz);
            if (width > height) {
                gridview.setSelection(0);
            }
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelStreamData kisi = adaptorumuz.getItem(i);
                    channelIDTS = kisi.getChannelID();

                    String jsonData = null;
                    for (int z = 0; z < seriesInfoModelList.size(); z++) {
                        if (channelIDTS.equals(String.valueOf(seriesInfoModelList.get(z).seriesId))) {
                            passValue = z;
                        }

                    }
               /*    extensionContainer ="."+extensionList.get(i);
                    if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {
                        isAppInstalled("com.mxtech.videoplayer.ad");
                    } else {
                        VideoURL = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/movie/" + MainActivity.userName + "/" + MainActivity.password + "/"
                                + channelIDTS + extensionContainer;
                        Intent iy = new Intent(SeriesChannelGridList.this, VideoViewActivity.class);
                        Uri videoUri = Uri.parse(VideoURL);
                        iy.setDataAndType(videoUri, "video/*");
                        startActivity(iy);
                    }*/
                /*
                    yeni versiyon için iptal edildi
                    Intent iy = new Intent(SeriesChannelGridList.this, SeriesInfoList.class);
                    SeriesInfoList.channelCatID=kisi.getChannelID();
                    startActivity(iy);*/


                    jsonData = gson.toJson(seriesInfoModelList, type);


                    Intent iy = new Intent(SeriesChannelGridList.this, InfoActivity.class);
                    SeriesInfoList.channelCatID = kisi.getChannelID();
                    iy.putExtra("data", jsonData);
                    iy.putExtra("pst", passValue);
                    startActivity(iy);
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

    }
}