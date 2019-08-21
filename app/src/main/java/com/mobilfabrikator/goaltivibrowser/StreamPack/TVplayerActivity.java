package com.mobilfabrikator.goaltivibrowser.StreamPack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.VideoAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelEPGModel;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelStreamData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.players.ExoPlayerManager;

import static com.mobilfabrikator.goaltivibrowser.VideoViewActivity.channelIDTS;

public class TVplayerActivity extends AppCompatActivity {

    public static String channelCatID;
    public static int getPos = 0;
    public static ArrayList ChannelList = new ArrayList();
    public static List<ChannelStreamData> channelList = new ArrayList<ChannelStreamData>();
    static PlayerView mPlayerView;
    private static String urlString, videoURL;
    ArrayList<HashMap<String, String>> contactList;
    ListView lv;
    TextView textViewEPG, denmetxt;
    boolean isFullScreen = false;
    View previousSelectedItem;
    long mLastClickTime;
    String jsonEPGStr;
    Button dnemeBtn, eksibtn, aramaBTN;
    boolean disableList = false;
    private int scrollPos = 0;
    private ProgressDialog pDialog;
    private String streamID;
    private Dialog mFullScreenDialog;
    private ImageView mFullScreenIcon;
    private EditText editTextArama;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_tvplayer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        lv = findViewById(R.id.listview_channel);
        mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setUseController(false);
        mPlayerView.hideController();

        textViewEPG = findViewById(R.id.textView_epg);
        denmetxt = findViewById(R.id.textView12);
        aramaBTN = findViewById(R.id.button_kanal_arama);
        editTextArama = findViewById(R.id.editText_arama);


        dnemeBtn = findViewById(R.id.button3);
        eksibtn = findViewById(R.id.button4);
        dnemeBtn.setVisibility(View.GONE);
        eksibtn.setVisibility(View.GONE);
        editTextArama.setVisibility(View.GONE);
        editTextArama.setVisibility(View.GONE);
        imm = (InputMethodManager) TVplayerActivity.this
                .getSystemService(Activity.INPUT_METHOD_SERVICE);

        aramaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextArama.getVisibility() == View.VISIBLE) {
                    if (imm.isActive()) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                    }
                    editTextArama.setVisibility(View.GONE);
                } else {
                    editTextArama.setVisibility(View.VISIBLE);
                    editTextArama.requestFocus();
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
                }
            }
        });


        mFullScreenIcon = mPlayerView.findViewById(R.id.exo_fullscreen_icon);
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort +
                "/player_api.php?username=" + MainActivity.userName + "&password=" + MainActivity.password +
                "&action=get_live_streams&category_id=" + channelCatID;

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

            /*public void onBackPressed() {
                if (isFullScreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }*/
        };

        mFullScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    closeFullscreenDialog();
                } else {
                    openFullScreenDialog();
                }
            }
        });


        lv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() != KeyEvent.ACTION_DOWN)) {

                    onKeyDown(keyCode, event);

                    // onKeyDown(keyCode,event);
                    return true;
                }
                return false;
            }
        });


        mFullScreenDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    onKeyDown(keyCode, event);
                    return true;
                }
                return false;
            }

        });


        new GetChannelCategory().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    public void onItemDoubleClick(AdapterView<?> adapterView, View view, int position, long l) {
        ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();
        if (isFullScreen == false) {
            openFullScreenDialog();
        } else {
            closeFullscreenDialog();
        }
    }

    private void playVideo(final String downloadUrl) {
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(TVplayerActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(TVplayerActivity.this).playStream(downloadUrl);
        //  mPlayerView.hideController();
        new GetEPG().execute();

        mPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup.LayoutParams params = mPlayerView.getLayoutParams();

                if (isFullScreen == false) {
                    openFullScreenDialog();
                } else {
                    closeFullscreenDialog();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExoPlayerManager.getSharedInstance(TVplayerActivity.this).stopPlayer(true);
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println(event.getKeyCode() + " : " + isFullScreen);
        mPlayerView.hideController();

        if (event.getKeyCode() == KeyEvent.KEYCODE_SEARCH) {
            if (editTextArama.getVisibility() == View.VISIBLE) {
                if (imm.isActive()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                }
                editTextArama.setVisibility(View.GONE);
            } else {
                editTextArama.setVisibility(View.VISIBLE);
                editTextArama.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
            }
            return true;
        }


        if (isFullScreen == false) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                openFullScreenDialog();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
                return true;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {

                getPos = lv.getSelectedItemPosition();
                ChannelStreamData kisi = channelList.get(getPos);
                channelIDTS = kisi.getChannelID();
                playVideo(ChannelList.get(getPos).toString());
                streamID = (channelList.get(getPos).getChannelID());
                if (previousSelectedItem != null) {
                    previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                }
                long currTime = System.currentTimeMillis();
                mLastClickTime = currTime;
            }
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                getPos = lv.getSelectedItemPosition();
                ChannelStreamData kisi = channelList.get(getPos);
                channelIDTS = kisi.getChannelID();
                playVideo(ChannelList.get(getPos).toString());
                streamID = (channelList.get(getPos).getChannelID());

                if (previousSelectedItem != null) {
                    previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                }
                long currTime = System.currentTimeMillis();

                mLastClickTime = currTime;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN) {

                getPos = lv.getSelectedItemPosition();
                ChannelStreamData kisi = channelList.get(getPos);
                channelIDTS = kisi.getChannelID();
                playVideo(ChannelList.get(getPos).toString());
                streamID = (channelList.get(getPos).getChannelID());
                if (previousSelectedItem != null) {
                    previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                }
                long currTime = System.currentTimeMillis();
                mLastClickTime = currTime;
            }
            if (event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP) {
                getPos = lv.getSelectedItemPosition();
                ChannelStreamData kisi = channelList.get(getPos);
                channelIDTS = kisi.getChannelID();
                playVideo(ChannelList.get(getPos).toString());
                streamID = (channelList.get(getPos).getChannelID());

                if (previousSelectedItem != null) {
                    previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                }
                long currTime = System.currentTimeMillis();

                mLastClickTime = currTime;
            }

        }

        if (isFullScreen == true) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                closeFullscreenDialog();
                return true;
            }

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                closeFullscreenDialog();
                return true;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                kanalArtir();
                return true;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                kanalAzalt();
                return true;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP) {
                kanalArtir();
                return true;
            }

            if (event.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN) {
                kanalAzalt();
                return true;
            }


            return true;

        }
        return false;
    }

    private void kanalArtir() {
        getPos = getPos + 1;
        ChannelStreamData kisi = channelList.get(getPos);
        channelIDTS = kisi.getChannelID();
        playVideo(ChannelList.get(getPos).toString());
        streamID = (channelList.get(getPos).getChannelID());
        lv.setSelection(getPos);

        if (previousSelectedItem != null) {
            previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
        }
        long currTime = System.currentTimeMillis();

        mLastClickTime = currTime;

    }

    private void kanalAzalt() {
        if (getPos > 0) {
            getPos = getPos - 1;
            ChannelStreamData kisi = channelList.get(getPos);
            channelIDTS = kisi.getChannelID();
            playVideo(ChannelList.get(getPos).toString());
            streamID = (channelList.get(getPos).getChannelID());
            lv.setSelection(getPos);


            if (previousSelectedItem != null) {
                previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
            }
            long currTime = System.currentTimeMillis();

            mLastClickTime = currTime;
        }
    }

    private void openFullScreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mPlayerView.hideController();
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(TVplayerActivity.this, R.drawable.ic_fullscreen_skrink));
        isFullScreen = true;
        mFullScreenDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mFullScreenDialog.show();


    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mPlayerView);
        isFullScreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(TVplayerActivity.this, R.drawable.ic_fullscreen_expand));
    }

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(TVplayerActivity.this);
            pDialog.setMessage("Yükleniyor");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);
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
            final VideoAdapter adaptorumuz = new VideoAdapter(TVplayerActivity.this, channelList);
            lv.setAdapter(adaptorumuz);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            lv.requestFocus();
            pDialog.dismiss();
            mPlayerView.hideController();
            playVideo(ChannelList.get(1).toString());
            streamID = (channelList.get(0).getChannelID());
            getPos = 0;
            lv.setSelection(0);
            lv.setItemChecked(0, true);
            lv.setItemsCanFocus(true);
            lv.setAdapter(adaptorumuz);

            editTextArama.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String text = editTextArama.getText().toString().toLowerCase(Locale.getDefault());
                    adaptorumuz.getFilter().filter(text);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @SuppressLint("ResourceAsColor")
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pst, long l) {
                    ChannelStreamData kisi = adaptorumuz.getItem(pst);
                    channelIDTS = kisi.getChannelID();
                    getPos = pst;
                    //  playVideo(ChannelList.get(pst).toString());
                    String urlCanli = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/live/" +
                            MainActivity.userName + "/" + MainActivity.password + "/" + channelIDTS + ".ts";
                    playVideo(urlCanli);

                    streamID = (adaptorumuz.getItem(pst).getChannelID());

                    if (previousSelectedItem != null) {
                        previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                    }

                    previousSelectedItem = view;
                    view.setBackgroundResource(R.drawable.corner_mavi);
                    long currTime = System.currentTimeMillis();
                   /* if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                        onItemDoubleClick(adapterView, view, pst, l);
                    }*/
                    mLastClickTime = currTime;


               /*     if (kayıtlı_kullanıcı("mxplayer").equals("ok")) {


                        if (value == false) {
                            Intent iy = new Intent(TVplayerActivity.this, VideoViewActivity.class);
                            startActivity(iy);
                        } else {
                            isAppInstalled("com.mxtech.videoplayer.ad");

                        }


                    } else {
                        Intent iy = new Intent(TVplayerActivity.this, VideoViewActivity.class);
                        startActivity(iy);
                        Log.e(TAG, "Response from url: " + kisi.getChannelName());
                    }*/


                }


            });

            dnemeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPos = getPos + 1;
                    ChannelStreamData kisi = channelList.get(getPos);
                    channelIDTS = kisi.getChannelID();
                    playVideo(ChannelList.get(getPos).toString());
                    streamID = (channelList.get(getPos).getChannelID());

                    if (previousSelectedItem != null) {
                        previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                    }
                    long currTime = System.currentTimeMillis();

                    mLastClickTime = currTime;
                }
            });

            eksibtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPos = getPos - 1;
                    ChannelStreamData kisi = channelList.get(getPos);
                    channelIDTS = kisi.getChannelID();
                    playVideo(ChannelList.get(getPos).toString());
                    streamID = (channelList.get(getPos).getChannelID());

                    if (previousSelectedItem != null) {
                        previousSelectedItem.setBackgroundColor(Color.TRANSPARENT);
                    }
                    long currTime = System.currentTimeMillis();

                    mLastClickTime = currTime;
                }
            });
        }

    }

    private class GetEPG extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
       /*     pDialog = new ProgressDialog(TVplayerActivity.this);
            pDialog.setMessage("Yükleniyor");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort +
                    "/player_api.php?username=" + MainActivity.userName +
                    "&password=" + MainActivity.password +
                    "&action=get_short_epg&stream_id=" + streamID;

            jsonEPGStr = sh.makeServiceCall(urlString);
            pDialog.dismiss();
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            String text = null;
            try {
                Gson gson = new Gson();
                ChannelEPGModel epgModel = gson.fromJson(jsonEPGStr, ChannelEPGModel.class);
                //  textViewEPG.setText(epgModel.userInfo.message);
                // textViewEPG.setText("Tv Rehberi Şu an İçin Bulunamadı");
                byte[] data = Base64.decode(epgModel.epgListings.get(0).title, Base64.DEFAULT);
                text = new String(data, StandardCharsets.UTF_8);
                textViewEPG.setText(epgModel.epgListings.get(0).start.substring(10, 16) + "-" +
                        epgModel.epgListings.get(0).end.substring(10, 16)
                        + "  " + text);

            } catch (Exception e) {
                e.printStackTrace();
                textViewEPG.setText("Tv Rehberi Şu an İçin Bulunamadı");

            }
            //  pDialog.dismiss();
        }

    }


}
