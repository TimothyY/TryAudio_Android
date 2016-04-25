package timothyyudi.tryaudio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAudio1,btnAudio2, btnEndAudio;

    private AudioService audioSrv;
    private Intent playIntent;
//    private boolean audioBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAudio1 = (Button) findViewById(R.id.btnAudio1);
        btnAudio1.setOnClickListener(this);
        btnAudio2 = (Button) findViewById(R.id.btnAudio2);
        btnAudio2.setOnClickListener(this);
        btnEndAudio = (Button) findViewById(R.id.btnEndAudio);
        btnEndAudio.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, AudioService.class);
            bindService(playIntent, audioConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        audioSrv=null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnAudio1){
            audioSrv.setAudioURI("https://dl.dropboxusercontent.com/u/62237071/a.mp3");
            audioSrv.playAudio();
        }else if(v.getId()==R.id.btnAudio2){
            audioSrv.setAudioURI("https://dl.dropboxusercontent.com/u/62237071/b.mp3");
            audioSrv.playAudio();
        }else if(v.getId()==R.id.btnEndAudio){
            stopService(playIntent);
            audioSrv=null;
            System.exit(0);
        }
    }

    //connect to the service
    private ServiceConnection audioConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder)service;
            //get service
            audioSrv = binder.getService();
            //pass list
            audioSrv.setAudioURI("");
//            audioBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            audioBound = false;
        }
    };

}
