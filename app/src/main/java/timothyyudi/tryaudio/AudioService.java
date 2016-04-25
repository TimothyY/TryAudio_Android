package timothyyudi.tryaudio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private String audioURI;
    private final IBinder audioBind = new AudioBinder();

    public AudioService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return audioBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer(); //create player
        initAudioPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void initAudioPlayer(){  //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void playAudio(){
        player.reset();
        try{player.setDataSource(getApplicationContext(), Uri.parse(audioURI));}
        catch(Exception e){Log.e("AUIDO SERVICE", "Error setting data source", e);}
        player.prepareAsync();
    }

    public void setAudioURI(String audioURI) {
        this.audioURI = audioURI;
    }

    public class AudioBinder extends Binder { //will be accessed from activity

        AudioService getService() {
            return AudioService.this;
        }

    }
}
