package pidscrypt.world.mutual.mutal.media;

import android.media.MediaRecorder;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pidscrypt.world.mutual.mutal.api.MediaPath;

public class Audio {

    private MediaRecorder mediaRecorder;
    private String mediaLocation = null;
    private String mediaName = null;
    private String fileName;
    private File file;

    public File getFile() {
        return file;
    }

    public String MediaLocation() {
        return this.mediaLocation;
    }

    public String getMediaName() {
        return mediaName;
    }

    public Uri getFileUri(){
        return Uri.fromFile(getFile());
    }

    public Audio(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaName = "/AUD_"+timeStamp+".m4a";
        try {
            mediaLocation = createAudioFile(this.mediaName).getAbsolutePath();
            //fileName = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+mediaName;
        }catch (Exception o){}
    }

    public void startRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(MediaLocation());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch(IOException ex){

        }

        mediaRecorder.start();
    }

    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    public void play(){

    }


    private File createAudioFile(String imageFileName) throws IOException {

        File cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), MediaPath.AUDIO_SENT_FOLDER);

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        file  = new File(cacheDir + "/" + imageFileName);


        return file;
    }

    public boolean fileExists(){

        //FILE CHECK ....
        if (!(new File(mediaLocation).exists())) {
            //Toast.makeText(getContext(), R.string.audio_recording_send_text, Toast.LENGTH_SHORT).show();
            return true;
        }
        //a.sendMessage("audio",mediaLocation, "");
        return false;
    }
}
