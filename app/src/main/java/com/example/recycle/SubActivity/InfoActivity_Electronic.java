package com.example.recycle.SubActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.recycle.PrivateKeys;
import com.example.recycle.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class InfoActivity_Electronic extends YouTubeBaseActivity {

    private static final String API = PrivateKeys.YT_Key;
    YouTubePlayerView ytPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_electronic);

        ytPlayer1 = findViewById(R.id.ytPlayer1);
        ytPlayer1.initialize(API, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                youTubePlayer.cuePlaylist("PLij-AFK62xo9u8vHcx7ZTO5q8w9g2BQvg");
                youTubePlayer.cueVideo("eIdJ22AfsO8");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(getApplicationContext(), "Video player Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}