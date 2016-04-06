package media;

import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.IOException;

import io.CFile;


public class AmrMediaRecord implements IMediaRecord {

	private static MediaRecorder mediaRecorder;

	@Override
	public void start(String file) {
		if (TextUtils.isEmpty(file)) {
			return;
		}
		CFile soundFile = new CFile(file);
		soundFile.createNewFileAndDirectory();
		try {
			if (mediaRecorder != null)
				mediaRecorder.stop();
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mediaRecorder.setOutputFile(file);
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		if (mediaRecorder != null) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

}
