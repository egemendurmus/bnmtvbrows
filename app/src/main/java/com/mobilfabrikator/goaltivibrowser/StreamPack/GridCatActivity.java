package com.mobilfabrikator.goaltivibrowser.StreamPack;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.GridOzelAdapter;
import com.mobilfabrikator.goaltivibrowser.DataPack.ChannelCategoryData;
import com.mobilfabrikator.goaltivibrowser.HttpHandler;
import com.mobilfabrikator.goaltivibrowser.MainActivity;
import com.mobilfabrikator.goaltivibrowser.R;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.StreamPack.TVplayerActivity;
import com.mobilfabrikator.goaltivibrowser.VodORStreamActivity;
import com.mobilfabrikator.goaltivibrowser.infoActivity;

public class GridCatActivity extends AppCompatActivity {
    final List<ChannelCategoryData> channelList = new ArrayList<ChannelCategoryData>();
    GridView gridview;
    String urlString;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    MaterialButton inMenuArama, textArama;
    ConstraintLayout aramall;
    EditText edittextArama;
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


        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_grid_cat);

        gridview = findViewById(R.id.gridview);
        urlString = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" + MainActivity.userName + "&password=" + MainActivity.password + "&action=get_live_categories";

        gridview.requestFocus();
        gridview.setSelection(0);
        new GetChannelCategory().execute();
        Button b6 = findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GridCatActivity.this, infoActivity.class);
                startActivity(i);
            }
        });
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
                    isKeyBoardOpen(GridCatActivity.this);
                }else{
                    aramall.setVisibility(View.GONE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .repeat(0)
                            .playOn(aramall);
                    isKeyBoardOpen(GridCatActivity.this);

                }
            }
        });


    }

    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        // dosyaya yazmamza yardmc olacak bir tane editr oluturduk.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editr aracl ile key value deerlerini yazdk.
        // ben string kaydettim ama farkl veri tipleri de kaydediliyor.
        editor.putString(key, value);
        // Farkl veri tiplerine rnek
        // editor.putBoolean("boolean", true);
        // editor.putFloat("float", (float) 123.23);
        // editor.putInt("int", 23);
        // editor.putLong("long", 123134243);

        /*
         * Serilize edilmi dosyay aadaki gibi kaydedebilirz.
         * ArrayList<String> al=new SharedPreferencesSampleActivity();
         * editor.putString(ARRAY_LIST_TAG, ObjectSerializer.serialize(al));
         */

        // bilgileri kaydettim.
        editor.commit();
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

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        final EditText et = dialog.findViewById(R.id.editText);

        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = et.getText().toString();
                if (pass.equals("2580")) {
                   /* if (kayıtlı_kullanıcı("tip").equals("liste")) {
                        Intent is = new Intent(GridCatActivity.this, ChannelListActivity.class);
                        startActivity(is);
                    } else if (kayıtlı_kullanıcı("tip").equals("grid")) {
                        Intent is = new Intent(GridCatActivity.this, GridCatActivity.class);
                        startActivity(is);
                    }*/
                    Intent is = new Intent(GridCatActivity.this, TVplayerActivity.class);
                    startActivity(is);

                } else {
                    Toast.makeText(GridCatActivity.this, "Yanlış Şifre Girdiniz Lütfen Tekrar Deneyiniz", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public boolean dispatchKeyEvent(KeyEvent e) {
        Log.d("hello", String.valueOf(e.getKeyCode()));

        try {

            if (e.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                Intent i = new Intent(GridCatActivity.this, VodORStreamActivity.class);
                startActivity(i);
                finish();

                return true;
            }
        } catch (Exception ez) {
            Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
        }


        return super.dispatchKeyEvent(e);
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
            pDialog = new ProgressDialog(GridCatActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);


            if (jsonStr != null) {
                try {
                    JSONArray jsonarray = new JSONArray(jsonStr);

                    // Getting JSON Array node
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        String catName = jsonObj.getString("category_name");
                        String catID = jsonObj.getString("category_id");
                        channelList.add(new ChannelCategoryData(catName, catID));


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
            final GridOzelAdapter adaptorumuz = new GridOzelAdapter(GridCatActivity.this, channelList);
            gridview.setAdapter(adaptorumuz);
            if (adaptorumuz.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "Kullanıcı Adın ve Şifreni Kontrol Et", Toast.LENGTH_LONG).show();

                Intent i = new Intent(GridCatActivity.this, MainActivity.class);
                startActivity(i);
            } else {
                sakla("password", MainActivity.password);
                sakla("username", MainActivity.userName);
                sakla("port", MainActivity.MainPort);
                sakla("MainUrl", MainActivity.MainUrl);


            }
            if (width > height) {
                gridview.setSelection(0);
            }

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelCategoryData kisi = adaptorumuz.getItem(i);
                    ChannelListActivity.channelCatID = kisi.getCategoryID();
                    TVplayerActivity.channelCatID = kisi.getCategoryID();

                    if (kisi.getCategoryName().equals("FOR ADULTS")) {
                        showDialog(GridCatActivity.this, null);
                    } else {
                     /*    if(kayıtlı_kullanıcı("tip").equals("liste")){
                            Intent is = new Intent(GridCatActivity.this, ChannelListActivity.class);
                            startActivity(is);
                        }else  if(kayıtlı_kullanıcı("tip").equals("grid")){
                            Intent is = new Intent(GridCatActivity.this, GridChannelListActivity.class);
                            startActivity(is);;
                        }*/
                        Intent is = new Intent(GridCatActivity.this, TVplayerActivity.class);
                        startActivity(is);

                    }



                    Log.e(TAG, "Response from url: " + kisi.getCategoryName());

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
