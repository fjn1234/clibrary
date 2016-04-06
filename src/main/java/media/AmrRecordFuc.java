package media;

import android.media.MediaRecorder;

import java.io.IOException;

public class AmrRecordFuc extends MediaRecorder {
	private static MediaRecorder mRecorder;

	private AmrRecordFuc() {
	};

	public static MediaRecorder getInstance() {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(AudioSource.MIC);
			mRecorder.setOutputFormat(OutputFormat.DEFAULT);
			mRecorder.setAudioEncoder(AudioEncoder.DEFAULT);
		}
		return mRecorder;
	}

	public void start(String file) {
		try {
			this.setOutputFile(file);
			this.prepare();
			this.start();
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
