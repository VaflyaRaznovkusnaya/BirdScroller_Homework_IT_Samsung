package ru.samsung.itschool.mdev.funnybirds;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.nancy_song);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        //setContentView(R.layout.activity_main);
        setContentView(new GameView(this));
    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}