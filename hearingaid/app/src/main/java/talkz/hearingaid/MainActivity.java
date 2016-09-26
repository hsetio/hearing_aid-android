package talkz.hearingaid;
import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {
    int multiplier=2;
    byte b= (byte) multiplier;

    static final int bufferSize = 200000;
    final short[] buffer = new short[bufferSize];
    short[] readBuffer = new short[bufferSize];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new recordTask().execute();

    }

    private class recordTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            RecordAudio();


            return "this string is passed to postExecute";
        }
    }


    void RecordAudio() {



        boolean isRecording = true;


        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        int buffersize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, buffersize);
        AudioTrack atrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 11025, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM);
        atrack.setPlaybackRate(11025);
        byte[] buffer = new byte[buffersize];



        NoiseSuppressor.create(arec.getAudioSessionId());


        arec.startRecording();
        atrack.play();
        while(isRecording) {
            int count=arec.read(buffer, 0, buffersize);

                for(int i=0;i< count; i++)
                {
                    buffer[i]=(byte)(5* buffer[i]);
                }
            atrack.write(buffer, 0, buffer.length);
        }
    }
}


