package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HandshakeCompletedListener;

public class MainActivity extends AppCompatActivity {

    Button prevBtn,playPauseBtn,nextBtn;
    SeekBar seekSong;
    ListView songList;
    MediaPlayer player;
    Context c = this;
    Handler handler = new Handler();
    ContentResolver resolver;
    Cursor allSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prevBtn = findViewById(R.id.back);
        playPauseBtn = findViewById(R.id.play_pause);
        nextBtn = findViewById(R.id.next);
        seekSong = findViewById(R.id.song_seek);
        songList = findViewById(R.id.song_list);

        resolver = getContentResolver();

        Uri externalsong = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        allSongs = resolver.query(externalsong,null,null,null,MediaStore.Audio.Media.DISPLAY_NAME);
        SongAdapter adapter = new SongAdapter(allSongs);

        player = new MediaPlayer();

        songList.setAdapter(adapter);

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TextView songName = view.findViewById(R.id.song_item_name);
                songName.setSelected(true);
                playPauseBtn.setBackground(getDrawable(R.drawable.ic_pause));

                allSongs.moveToPosition(position);
              String path=  allSongs.getString(allSongs.getColumnIndex(MediaStore.Audio.Media.DATA));

              try{


                  if(player.isPlaying()){
                      player.stop();
                      player.release();
                      player = null;
                      player = new MediaPlayer();
                      player.setDataSource(c,Uri.parse(path));
                      player.prepare();
                      player.start();
                  }
                  else {
                      player.setDataSource(c,Uri.parse(path));
                      player.prepare();
                      player.start();
                  }

              }
              catch (Exception e){
                  Log.e("error",e.toString());

              }


            }
        });




        seekSong.setMax(player.getDuration());
       // seekSong.setProgress(498984);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekSong.setProgress(player.getCurrentPosition());
                handler.postDelayed(this,1);
              //  Log.e("song",""+player.getCurrentPosition());
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                switch (id){

                    case R.id.back:
                        player.seekTo(player.getCurrentPosition()-10000);
                        break;
                    case R.id.play_pause:
                        if(player.isPlaying()){
                            player.pause();
                            playPauseBtn.setBackground(getDrawable(R.drawable.ic_play));
                        }
                        else {
                            player.start();
                            playPauseBtn.setBackground(getDrawable(R.drawable.ic_pause));
                        }

                        break;
                    case R.id.next:
                       allSongs.moveToNext();
                        String path=  allSongs.getString(allSongs.getColumnIndex(MediaStore.Audio.Media.DATA));

                        try{


                            if(player.isPlaying()){
                                player.stop();
                                player.release();
                                player = null;
                                player = new MediaPlayer();
                                player.setDataSource(c,Uri.parse(path));
                                player.prepare();
                                player.start();
                            }
                            else {
                                player.setDataSource(c,Uri.parse(path));
                                player.prepare();
                                player.start();
                            }

                        }
                        catch (Exception e){
                            Log.e("error",e.toString());

                        }
                        break;
                }

            }
        };

        prevBtn.setOnClickListener(listener);
        playPauseBtn.setOnClickListener(listener);
        nextBtn.setOnClickListener(listener);
        seekSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                if(fromUser){
                    player.seekTo(position);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}