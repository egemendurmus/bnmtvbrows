package com.mobilfabrikator.goaltivibrowser.players;


import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Arrays;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import com.mobilfabrikator.goaltivibrowser.R;

public class ExoPlayerYoutubeActivity extends AppCompatActivity {
    // Replace video id with your video Id
    public static String YOUTUBE_VIDEO_ID;
    private String BASE_URL = "https://www.youtube.com";
    private String mYoutubeLink = BASE_URL + "/watch?v=" + YOUTUBE_VIDEO_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            extractURL();
        } catch (Exception e) {
            System.out.println("hata: " + e);
        }
    }

    private void extractURL() {
        new YouTubeExtractor(ExoPlayerYoutubeActivity.this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    // 720, 1080, 480
                    List<Integer> iTags = Arrays.asList(22, 137, 18);
                    for (Integer iTag : iTags) {
                        YtFile ytFile = ytFiles.get(iTag);
                        if (ytFile != null) {
                            String downloadUrl = ytFile.getUrl();
                            if (downloadUrl != null && !downloadUrl.isEmpty()) {
                                Uri youtubeUri = Uri.parse(downloadUrl);
                                playVideo(downloadUrl);
                                //   Log.d("OnSuccess", "Got a result with the best url: " + youtubeUri);
                                return;
                            }
                        }
                    }
                }
            }
        }.extract(mYoutubeLink, true, true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ExoPlayerManager.getSharedInstance(ExoPlayerYoutubeActivity.this).stopPlayer(true);
    }

    private void playVideo(String downloadUrl) {
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(ExoPlayerYoutubeActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(ExoPlayerYoutubeActivity.this).playStream(downloadUrl);
    }

}