package com.mobilfabrikator.goaltivibrowser.players;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ui.PlayerView;
import com.mobilfabrikator.goaltivibrowser.R;

import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;

public class ExoPlayerVideoActivity extends AppCompatActivity {

    public static String videoURL;
    private ImageView mFullScreenIcon,mBacwardTenSec,mForwardTenSec;
    IjkMediaCodecInfo mediaCodecInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playVideo(videoURL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExoPlayerManager.getSharedInstance(ExoPlayerVideoActivity.this).stopPlayer(true);
    }

    private void playVideo(String downloadUrl) {
        final PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(ExoPlayerVideoActivity.this).getPlayerView().getPlayer());
        mFullScreenIcon = mPlayerView.findViewById(R.id.exo_fullscreen_icon);
        mBacwardTenSec= mPlayerView.findViewById(R.id.exo_backward_icon);
        mForwardTenSec= mPlayerView.findViewById(R.id.exo_forward_icon);
        mFullScreenIcon.setVisibility(View.GONE);

        mForwardTenSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.getPlayer().seekTo( mPlayerView.getPlayer().getCurrentPosition() + 10000);
            }
        });
        mBacwardTenSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerView.getPlayer().seekTo( mPlayerView.getPlayer().getCurrentPosition() - 10000);
            }
        });


        ExoPlayerManager.getSharedInstance(ExoPlayerVideoActivity.this).playVideoStream(downloadUrl);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoURL=null;
    }
}
