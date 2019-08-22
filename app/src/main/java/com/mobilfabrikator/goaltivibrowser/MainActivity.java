package com.mobilfabrikator.goaltivibrowser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class MainActivity extends Activity {
    public static String userName, password, MainUrl, MainPort;
    String authUrl, expDate, status;
    private EditText inputEmail, inputPassword, inputURL, inputPort;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutUrl, inputLayoutPort;
    private Button btnSignUp;
    private TextView bitisText;
    private ProgressDialog pDialog;

    // String VideoURL = "http://iptv.nexa.space:8000/get.php?username=egementst&password=564fdtr&type=m3u&output=ts";

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    @SuppressLint("ResourceAsColor")
    public static String getTime(String longV) {

        long millisecond = Long.parseLong(longV);
        // or you already have long value of date, use this instead of milliseconds variable.
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond * 1000)).toString();
        System.out.println("Title : " + " = " + dateString);

        return dateString;
    }

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
        // its landscape
        setContentView(R.layout.new_activity_main);


        inputLayoutEmail = findViewById(R.id.input_layout_email);
        inputLayoutPassword = findViewById(R.id.input_layout_password);
        inputLayoutUrl = findViewById(R.id.input_layout_url);
        inputLayoutPort = findViewById(R.id.input_layout_port);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputURL = findViewById(R.id.input_url);
        inputPort = findViewById(R.id.input_port);
        inputLayoutPort.setVisibility(View.GONE);
        inputLayoutUrl.setVisibility(View.GONE);
        btnSignUp = findViewById(R.id.btn_signup);
        bitisText = findViewById(R.id.bitis_text);



     /*   inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputURL.addTextChangedListener(new MyTextWatcher(inputURL));
        inputPort.addTextChangedListener(new MyTextWatcher(inputPort));
        inputURL.requestFocus();

*/

        if (!StringUtils.isEmpty(kayıtlı_kullanıcı("username")) &&
                !StringUtils.isEmpty(kayıtlı_kullanıcı("password"))) {
            setContentView(R.layout.loading_view);
            hideSoftKeyboard();
            submitForm();
        } else {
            //  setContentView(R.layout.new_activity_main);

        }

        if (!kayıtlı_kullanıcı("bitis").equals("")) {
            bitisText.setText("Son Kullanım Tarihiniz : " + (kayıtlı_kullanıcı("bitis").toString()));
            bitisText.setTextColor(Color.parseColor("#CD5C5C"));
            bitisText.setTextSize(8f);

        }

        if (kayıtlı_kullanıcı("username") != null) {
            inputEmail.setText(kayıtlı_kullanıcı("username"));
            userName = String.valueOf(kayıtlı_kullanıcı("username"));
        }
        if (kayıtlı_kullanıcı("password") != null) {
            inputPassword.setText(kayıtlı_kullanıcı("password"));
            password = String.valueOf(kayıtlı_kullanıcı("password"));

        }
        if (kayıtlı_kullanıcı("port") != null) {
            inputPort.setText(kayıtlı_kullanıcı("port"));
            MainPort = String.valueOf(kayıtlı_kullanıcı("port"));

        }
        if (kayıtlı_kullanıcı("MainUrl") != null) {
            inputURL.setText(kayıtlı_kullanıcı("MainUrl"));
            MainUrl = String.valueOf(kayıtlı_kullanıcı("MainUrl"));

        }
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        if (kayıtlı_kullanıcı("username") != null && kayıtlı_kullanıcı("password") != null) {
            submitForm();
        }

  /*      inputPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_SHIFT_LEFT:
                        // txtPlaka.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                       submitForm();
                       //isKeyBoardOpen(MainActivity.this);
                        break;

                }
                return false;
            }
        });*/
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

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
            System.out.println(" yüklü");

            OpenVideoLink();
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
            System.out.println(" değil");
            OpenStoreIntent();

        }
        return installed;
    }

    private void OpenVideoLink() {
        try {
            Intent myIntent;
            Intent i = new Intent(Intent.ACTION_VIEW);
            PackageManager pm = getPackageManager();
            try {
                myIntent = pm.getLaunchIntentForPackage("org.videolan.vlc&hl=tr");
                if (null != myIntent)
                    //   myIntent.setDataAndType(Uri.parse(com.mxtech.videoplayer.ad), "video/mp4");

                    i.setPackage("org.videolan.vlc&hl=tr");
                //  i.setDataAndType(Uri.parse(VideoURL), "video/*");
                startActivity(i);

                this.startActivity(myIntent);
            } catch (ActivityNotFoundException e) {

            }
        } catch (Exception e) {

        }
    }

    private void OpenMxPayer() {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.mxtech.videoplayer.ad");
            intent.setClassName("com.mxtech.videoplayer.ad", "com.mxtech.videoplayer.ad.ActivityScreen");
            // Uri videoUri = Uri.parse(VideoURL);
            //  intent.setDataAndType(videoUri, "application/x-mpegURL");
            intent.setPackage("com.mxtech.videoplayer.ad"); // com.mxtech.videoplayer.pro
            intent.putExtra("return_result", true);
//startActivity(intent);
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)  // -1 RESULT_OK : Playback was completed or stopped by user request.
            //Activity.RESULT_CANCELED: User canceled before starting any playback.
            //RESULT_ERROR (=Activity.RESULT_FIRST_USER): Last playback was ended with an error.

            if (data.getAction().equals("com.mxtech.intent.result.VIEW")) {
                //data.getData()
                int pos = data.getIntExtra("position", -1); // Last playback position in milliseconds. This extra will not exist if playback is completed.
                int dur = data.getIntExtra("duration", -1); // Duration of last played video in milliseconds. This extra will not exist if playback is completed.
                String cause = data.getStringExtra("end_by"); //  Indicates reason of activity closure.
            }
    }

    private void submitForm() {
        inputURL.setText(BuildConfig.SERVER_URL);
        inputPort.setText("1881");

     /*   if (!validatePort()) {
            return;
        }
        if (!validateUrl()) {
            return;
        }*/
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }


        //  isAppInstalled("org.videolan.vlc");
        //  isAppInstalled("com.mxtech.videoplayer.ad");
       /* Intent i = new Intent(MainActivity.this, VodORStreamActivity.class);
        startActivity(i);*/
        userName = inputEmail.getText().toString();
        password = inputPassword.getText().toString();
        MainPort = "1881";
        MainUrl = BuildConfig.SERVER_URL;

        new getAuthTv().execute();
        //   isKeyBoardOpen(MainActivity.this);


    }

    private boolean validateUrl() {
        String email = inputURL
                .getText().toString().trim();


        if (email.isEmpty()) {
            inputLayoutUrl.setError("Verilen Url'Yi Giriniz");
            requestFocus(inputURL);
            return false;
        } else {
            inputLayoutUrl.setErrorEnabled(false);

        }

        return true;
    }

    private boolean validatePort() {
        String email = inputPort
                .getText().toString().trim();


        if (email.isEmpty()) {
            inputLayoutPort.setError("Verilen Url'Yi Giriniz");
            requestFocus(inputURL);
            return false;
        } else {
            inputLayoutPort.setErrorEnabled(false);

        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();


        if (email.isEmpty()) {
            inputLayoutEmail.setError("Kullanıcı Adını Gir");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);

        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Şifreni Gir");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);

        }

        return true;
    }

    private void requestFocus(View view) {

        if (kayıtlı_kullanıcı("username") != null && kayıtlı_kullanıcı("password") != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            if (view.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

    }

    private CharSequence kayıtlı_kullanıcı(String key) {

        SharedPreferences sharedPreferences = getSharedPreferences("mydocs",
                MODE_PRIVATE);
        // key deerini vererek value deerini aldm.
        String strSavedMem1 = sharedPreferences.getString(key, "");


        return strSavedMem1;

        // TODO Auto-generated method stub
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

        editor.commit();
    }

    public void showDialog(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("GoalTivi Browser");
        builder.setMessage(msg);
        builder.setNegativeButton("Tamam", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //İptal butonuna basılınca yapılacaklar.Sadece kapanması isteniyorsa boş bırakılacak

            }
        });
        builder.show();
    }

    public void isKeyBoardOpen(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT); // show
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private class getAuthTv extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Yükleniyor..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            authUrl = "http://" + MainActivity.MainUrl + ":" + MainActivity.MainPort + "/player_api.php?username=" +
                    MainActivity.userName + "&password=" + MainActivity.password;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(authUrl);
            System.out.println(jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject jsonuserInfoObject = jsonObject.getJSONObject("user_info");
                    status = jsonuserInfoObject.getString("status");

                    String createdAt = jsonuserInfoObject.getString("created_at");
                    expDate = jsonuserInfoObject.getString("exp_date");
                    hideSoftKeyboard();

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("hata "+e);

                }
            }


            return null;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                if (!StringUtils.isEmpty(expDate)&&!expDate.equals("null")) {
                    bitisText.setText("Son Kullanım Tarihiniz : " + getTime(expDate));
                    bitisText.setTextColor(Color.parseColor("#CD5C5C"));
                    bitisText.setTextSize(8f);
                    //  sakla("bitis", getTime(expDate));
                    hideSoftKeyboard();
                    Intent i = new Intent(MainActivity.this, VodORStreamActivity.class);
                    i.putExtra("bitis", getTime(expDate));
                    startActivity(i);
                    finish();
                }else{
                    hideSoftKeyboard();
                    Intent i = new Intent(MainActivity.this, VodORStreamActivity.class);
                    startActivity(i);
                    finish();
                }

            } catch (Exception e) {

                showDialog(MainActivity.this, "Kullanıcı Adı ve Şifrenizi Kontrol Ediniz");

            }


        }

    }
}
