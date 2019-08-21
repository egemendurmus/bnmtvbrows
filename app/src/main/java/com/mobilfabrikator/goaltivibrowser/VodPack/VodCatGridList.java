package com.mobilfabrikator.goaltivibrowser.VodPack;

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
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;
import com.mobilfabrikator.goaltivibrowser.infoActivity;
import com.mobilfabrikator.goaltivibrowser.R;

public class VodCatGridList extends AppCompatActivity {
    GridView gridview;
    private ProgressDialog pDialog;
    String urlString;
    final List<ChannelCategoryData> channelList = new ArrayList<ChannelCategoryData>();
    private String TAG = MainActivity.class.getSimpleName();
    MaterialButton inMenuArama, textArama;
    ConstraintLayout aramall;
    EditText edittextArama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
         setContentView(R.layout.activity_grid_cat);

        gridview = findViewById(R.id.gridview);
        urlString = "http://"+MainActivity.MainUrl+":"+MainActivity.MainPort+"/player_api.php?username="+MainActivity.userName+"&password="+MainActivity.password+"&action=get_vod_categories";
        new GetChannelCategory().execute();
        Button b6 = findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VodCatGridList.this, infoActivity.class);
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
        gridview.requestFocus();
        gridview.setSelection(0);

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
                    isKeyBoardOpen(VodCatGridList.this);
                }else{
                    aramall.setVisibility(View.GONE);
                    YoYo.with(Techniques.BounceInUp)
                            .duration(700)
                            .repeat(0)
                            .playOn(aramall);
                    isKeyBoardOpen(VodCatGridList.this);

                }
            }
        });


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
                        Intent is = new Intent(VodCatGridList.this, ChannelListActivity.class);
                        startActivity(is);
                    } else if (kayıtlı_kullanıcı("tip").equals("grid")) {
                        Intent is = new Intent(VodCatGridList.this, GridCatActivity.class);
                        startActivity(is);
                    }*/
                    // ChannelListActivity.channelCatID = kisi.getCategoryID();
                    Intent is = new Intent(VodCatGridList.this, VodChannelGridList.class);
                    startActivity(is);
                } else {
                    Toast.makeText(VodCatGridList.this, "Yanlış Şifre Girdiniz Lütfen Tekrar Deneyiniz", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void deletePreferences(String key) {
        // TODO Auto-generated method stub
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                Context.MODE_PRIVATE);
        // dosyaya yazmamï¿½za yardï¿½mcï¿½ olacak bir tane editï¿½r
        // oluï¿½turduk.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // editï¿½r aracï¿½lï¿½ï¿½ï¿½ ile key value deï¿½erlerini yazdï¿½k.
        // ben string kaydettim ama farklï¿½ veri tipleri de kaydediliyor.

        // Farklï¿½ veri tiplerine ï¿½rnek
        // editor.putBoolean("boolean", true);
        // editor.putFloat("float", (float) 123.23);
        // editor.putInt("int", 23);
        // editor.putLong("long", 123134243);

        editor.putString(key, "");

        editor.commit();

    }

    private void sakla(String key, String value) {
        // shared preferences ile mydocs ad altnda bir tane xml dosya ayor
        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        // key deerini vererek value deerini aldm.
        String strSavedMem1 = sharedPreferences.getString(key, "");
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

    private class GetChannelCategory extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(VodCatGridList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
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
            final GridOzelAdapter adaptorumuz = new GridOzelAdapter(VodCatGridList.this, channelList);
            gridview.setAdapter(adaptorumuz);
            if(adaptorumuz.getCount()==0){
                Toast.makeText(getApplicationContext(), "Kullanıcı Adın ve Şifreni Kontrol Et", Toast.LENGTH_LONG).show();

                Intent i = new Intent(VodCatGridList.this,MainActivity.class);
                startActivity(i);
            }else{
                sakla("password", MainActivity.password);
                sakla("username", MainActivity.userName);
                sakla("port", MainActivity.MainPort);
                sakla("MainUrl", MainActivity.MainUrl);


            }

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ChannelCategoryData kisi = adaptorumuz.getItem(i);
                    ChannelListActivity.channelCatID = kisi.getCategoryID();

                    if(kisi.getCategoryName().equals("FOR ADULTS")){

                        showDialog(VodCatGridList.this,null);
                    }else{
                     /*   if(kayıtlı_kullanıcı("tip").equals("liste")){
                            VodFilmList.channelCatID = kisi.getCategoryID();

                            Intent is = new Intent(VodCatGridList.this, VodFilmList.class);
                            startActivity(is);
                        }else  if(kayıtlı_kullanıcı("tip").equals("grid")){
                            ChannelListActivity.channelCatID = kisi.getCategoryID();
                            Intent is = new Intent(VodCatGridList.this, VodChannelGridList.class);
                            startActivity(is);;
                        }*/

                        ChannelListActivity.channelCatID = kisi.getCategoryID();
                        Intent is = new Intent(VodCatGridList.this, VodChannelGridList.class);
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
