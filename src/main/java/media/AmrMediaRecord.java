package media;

import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.IOException;

import io.CFile;


public class AmrMediaRecord implements IMediaRecord {

    private MediaRecorder mRecord;
    private String filePath;
    private MediaPlayerHelper mediaPlayerHelper;


    public AmrMediaRecord() {
        mRecord = new MediaRecorder();
    }

    @Override
    public void start(String file) {
        if (TextUtils.isEmpty(file)) {
            return;
        }
        CFile soundFile = new CFile(file);
        soundFile.createNewFileAndDirectory();
        try {
            if (mRecord != null)
                mRecord.stop();
            stopPlay();
            filePath = file;
            mRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecord.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mRecord.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecord.setOutputFile(filePath);
            mRecord.prepare();
            mRecord.start();
        } catch (IllegalStateException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (mRecord != null) {
            mRecord.stop();
            mRecord.release();
            mRecord = null;
        }
    }

    @Override
    public void play() {
        if (TextUtils.isEmpty(filePath))
            if (mediaPlayerHelper == null)
                mediaPlayerHelper = new MediaPlayerHelper();
        mediaPlayerHelper.play(filePath);
    }

    @Override
    public void stopPlay() {
        mediaPlayerHelper.stop();
    }

}
