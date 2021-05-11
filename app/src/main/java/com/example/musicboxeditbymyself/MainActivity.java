package com.example.musicboxeditbymyself;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView title , author;
    ImageButton play , stop;
    ImageButton previous , next;

    ActivityReceiver activityReceiver;

    public static final String CTL_ACTION = "org.hzf.action.CTL_ACTION";
    public static final String UPDATE_ACTION = "org.hzf.action.UPDATE_ACTION";

    int status = 0x11;
    String[] titleStrs = new String[] { "Legends Never Die", "约定", "美丽新世界" };
    String[] authorStrs = new String[] { "英雄联盟", "周蕙", "伍佰" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        title = findViewById(R.id.songname);
        author = findViewById(R.id.maker);

        play.setOnClickListener(this);
        stop.setOnClickListener( this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);


        activityReceiver = new ActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        registerReceiver(activityReceiver,filter);
        Intent intent = new Intent(MainActivity.this,MusicService.class);
        startService(intent);



    }

    @Override
    public void onClick(View v) {

        // 创建Intent
        Intent intent = new Intent("org.hzf.action.CTL_ACTION");
        switch (v.getId())
        {
            // 按下播放/暂停按钮
            case R.id.play:
                intent.putExtra("control", 1);
                break;
            // 按下停止按钮
            case R.id.stop:
                intent.putExtra("control", 2);
                break;
            case R.id.previous:
                intent.putExtra("control", 3);
                break;
            // 按下下一首
            case R.id.next:
                intent.putExtra("control", 4);
                break;
        }
        // 发送广播，将被Service组件中的BroadcastReceiver接收到
        sendBroadcast(intent);


    }

    //自定义广播
    public class ActivityReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            int update = intent.getIntExtra("update",-1);
            int current = intent.getIntExtra("current", -1);

            if (current >= 0){
                title.setText(titleStrs[current]);
                author.setText(authorStrs[current]);
            }

            switch (update){
                case 0x11:
                    play.setImageResource(R.drawable.play);
                    status = 0x11;
                    break;
                case 0x12:
                    play.setImageResource(R.drawable.pause);
                    status = 0x12;
                    break;

                case 0x13:
                    play.setImageResource(R.drawable.play);
                    status = 0x13;
                    break;

            }


        }
    }

}
