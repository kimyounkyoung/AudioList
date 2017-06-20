package com.example.younkyoungkim.audiolist;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {   //객체 선언부
    ListView list;
    Button butPlay, butStop, butPause;
    TextView textMusic, textTime;
    ProgressBar progress;
    String[] musics={"kyarypamyupamyu_ponponpon","kyarypamyupamyu_ninjaribangbang","pinko_stick_luv"};
    int[] musicResIds={R.raw.kyarypamyupamyu_ponponpon,R.raw.kyarypamyupamyu_ninjaribangbang, R.raw.pinko_stick_luv};
    int selectedMusicId;
    MediaPlayer mediaPlayer;
    boolean selectedPauseButton;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.list_music);
        butPlay=(Button)findViewById(R.id.but_play);
        butStop=(Button)findViewById(R.id.but_stop);
        butPause=(Button)findViewById(R.id.but_pause);
        textMusic=(TextView)findViewById(R.id.text_music);
        textTime=(TextView)findViewById(R.id.text_time);
        progress=(SeekBar)findViewById(R.id.progress_music);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, musics);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(0,true);
        selectedMusicId=musicResIds[0];
        mediaPlayer=MediaPlayer.create(this, selectedMusicId);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPauseButton = false;
                mediaPlayer.stop();
                selectedMusicId=musicResIds[position];
                MainActivity.this.position=position;
            }
        });

        butPlay.setOnClickListener(new View.OnClickListener() {
            SimpleDateFormat timeFormat=new SimpleDateFormat("mm:ss");
            @Override
            public void onClick(View v) {
                textMusic.setText(musics[position]);
                mediaPlayer=MediaPlayer.create(MainActivity.this,selectedMusicId);
                mediaPlayer.start();
                if(selectedPauseButton) {
                    selectedPauseButton = false;
                }else
                    mediaPlayer=MediaPlayer.create(MainActivity.this,selectedMusicId);
                mediaPlayer.start();
                Thread musicThread=new Thread(){
                    @Override
                    public void run(){  //run메소드를 오버라이딩
                        if(mediaPlayer==null) return;
                        progress.setMax(mediaPlayer.getDuration()); //전체 음악이 재생되는 길이 반환을 최댓값으로 설정
                        while(mediaPlayer.isPlaying()){
                            progress.setProgress(mediaPlayer.getCurrentPosition()); //음악이 재생되는 지금의 위치
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textTime.setText("진행시간 : "+timeFormat.format(mediaPlayer.getCurrentPosition()));
                                }
                            });
                            SystemClock.sleep(200);
                        }
                    }
                };
                musicThread.start();
            }
        });

        butStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPauseButton=false;
                mediaPlayer.stop();
            }
        });
        }

        protected void onStop(){
            super.onStop();
            mediaPlayer.stop();
    }
}
