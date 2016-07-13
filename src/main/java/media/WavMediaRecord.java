package media;

import android.text.TextUtils;

import io.CFile;


public class WavMediaRecord implements IMediaRecord {

    private MediaPlayerHelper mediaPlayerHelper;
    private String filePath;
    private WavRecordFunc mRecord;

    public WavMediaRecord() {
        mRecord = WavRecordFunc.getInstance();
        mediaPlayerHelper = new MediaPlayerHelper();
    }


    @Override
    public void stop() {
        mRecord.stopRecordAndFile();
    }

    @Override
    public void play() {
        if (TextUtils.isEmpty(filePath)) return;
        mediaPlayerHelper.play(filePath);
    }

    @Override
    public void stopPlay() {
        mediaPlayerHelper.stop();
    }

    @Override
    public void start(String file) {
        if (TextUtils.isEmpty(file)) return;
        mRecord.stopRecordAndFile();
        stopPlay();
        WavFileFunc.setWavFilePath(file);
        filePath = file;
        mRecord.startRecordAndFile();
    }

}
