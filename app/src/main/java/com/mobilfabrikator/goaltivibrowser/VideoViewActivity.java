package com.mobilfabrikator.goaltivibrowser;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mobilfabrikator.goaltivibrowser.AdapterPack.*;
import com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity;

import static com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity.*;
import static com.mobilfabrikator.goaltivibrowser.StreamPack.ChannelListActivity.channelList;

public class VideoViewActivity extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    Button playBTN;
    private int currentIndex;
    ListView lv;
    private AudioManager audio;
    boolean centerCTRL;
    // Insert your Video URL
    // String VideoURL = "http://iptv.nexa.space:8000/live/andro/12345/5956.ts";
    public static String channelIDTS,extensionContainer,VideoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Get the layout from video_main.xml
        setContentView(R.layout.activity_video);
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        // pDialog.setTitle("Android Video Streaming Tutorial");
        // Set progressbar message

      /*  if(VodORStreamActivity.vodType=="tv"){
            VideoURL  = "http://" + MainActivity.MainUrl +  "/live/" + MainActivity.userName + "/" + MainActivity.password + "/" +
                    channelIDTS + ".ts";
        }else{
            VideoURL  = "http://" + MainActivity.MainUrl +  "/movie/" + MainActivity.userName + "/" + MainActivity.password + "/"
                    + channelIDTS + extensionContainer;

        }*/


        lv = findViewById(R.id.lv1);
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentIndex = ChannelListActivity.getPos;

        try {

           VideoAdapter adaptorumuz = new VideoAdapter(VideoViewActivity.this, channelList);
            lv.setAdapter(adaptorumuz);
            Uri video = Uri.parse(VideoURL);

            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL

            videoview.setVideoURI(video);



     /*       mediacontroller.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //next button clicked
                    Object randomItem = ChannelList.get(new Random().nextInt(ChannelList.size()));

                    Uri video = Uri.parse(String.valueOf(randomItem));


                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoview.getLayoutParams();
                    params.width = metrics.widthPixels;
                    params.height = metrics.heightPixels;
                    params.leftMargin = 0;
                    videoview.setLayoutParams(params);


                    videoview.setVideoURI(video);
                    videoview.start();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //previous button clicked

                }
            });


            videoview.setMediaController(mediacontroller);*/


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        centerCTRL = false;


        //  videoview.requestFocus();
        videoPreparedlistenerCall();



        videoview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                try{
                    dispatchKeyEvent(keyEvent);
                    System.out.println("key event "+keyEvent);
                }catch (Exception e){
                    System.out.println("key event "+e);
                }


                return false;
            }
        });


    }

    private void videoPreparedlistenerCall() {
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
                lv.setVisibility(View.GONE);


            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {




        try {


            if (centerCTRL == false) {

                if (e.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {

                    if (currentIndex < (ChannelList.size() - 1)) {

                        sumIndex();

                    }

                    return true;
                }

                if (e.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_UP) {

                    if (currentIndex < (ChannelList.size() - 1)) {

                        sumIndex();

                    }

                    return true;
                }


                if (e.getKeyCode() == KeyEvent.KEYCODE_CHANNEL_DOWN) {

                    if (currentIndex > 0) {
                        currentIndex = currentIndex - 1;
                        centerCTRL = false;

                        playLiveVideo(currentIndex);

                    }


                    return true;
                }


                if (e.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {

                    if (currentIndex > 0) {
                        currentIndex = currentIndex - 1;
                        centerCTRL = false;

                        playLiveVideo(currentIndex);

                    }


                    return true;
                }


                if (e.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {

                    centerCTRL = true;
                    lv.setVisibility(View.VISIBLE);
                    lv.requestFocus();
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            centerCTRL = false;
                            playLiveVideo(i);
                            lv.setVisibility(View.GONE);
                        }
                    });


                    return true;
                }

            }
            else{

            }

        }catch (Exception e5){

            playLiveVideo(1);
        }







        if (e.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {

            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

            return true;
        }

        if (e.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {

            audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

            return true;
        }


        return super.dispatchKeyEvent(e);
    }

    private void sumIndex() {
        currentIndex = currentIndex + 1;

        centerCTRL = false;
        playLiveVideo(currentIndex);
    }

    private void playLiveVideo(int currentIndex) {



        pDialog.setMessage("Yükleniyor..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();
        Uri video = Uri.parse(String.valueOf(ChannelList.get(currentIndex)));


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoview.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoview.setLayoutParams(params);


        videoview.setVideoURI(video);
        videoview.start();
        videoPreparedlistenerCall();
        centerCTRL=false;
    }

    ;
}