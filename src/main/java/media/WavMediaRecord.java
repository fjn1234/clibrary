package media;

import android.text.TextUtils;

import io.CFile;


public class WavMediaRecord implements IMediaRecord {

	private static WavRecordFunc mRecord;

	@Override
	public void stop() {
		if (mRecord != null) {
			mRecord.stopRecordAndFile();
		}
	}

	@Override
	public void start(String file) {
		if (TextUtils.isEmpty(file)) {
			return;
		}
		if (mRecord != null)
			mRecord.stopRecordAndFile();
		mRecord = WavRecordFunc.getInstance();
		WavFileFunc.setWavFilePath(file);
		CFile soundFile = new CFile(file);
		soundFile.createNewFileAndDirectory();
		mRecord.startRecordAndFile();
	}

}
