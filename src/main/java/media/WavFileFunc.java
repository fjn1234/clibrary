package media;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

public class WavFileFunc {
	public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
	public final static int AUDIO_SAMPLE_RATE =11025; // 44.1KHz
	private final static String AUDIO_RAW_FILENAME = "RawAudio.raw";
	public final static String AUDIO_AMR_FILENAME = "FinalAudio.amr";
	//private static String wavFileName;
	private static String wavFilePath;

//	public static String getWavFileName() {
//		return wavFileName;
//	}
//
//	public static void setWavFileName(String wavFileName) {
//		AudioFileFunc.wavFileName = wavFileName;
//	}

	public static void setWavFilePath(String wavFilePath) {
		WavFileFunc.wavFilePath = wavFilePath;
	}

	public static String getWavFilePath() {
		return wavFilePath;
	}

	public static boolean isSdcardExit() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getRawFilePath() {
		String mAudioRawPath = "";
		if (isSdcardExit()) {
			String fileBasePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			mAudioRawPath = fileBasePath + "/" + AUDIO_RAW_FILENAME;
		}

		return mAudioRawPath;
	}

	public static String getAMRFilePath() {
		String mAudioAMRPath = "";
		if (isSdcardExit()) {
			String fileBasePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			mAudioAMRPath = fileBasePath + "/" + AUDIO_AMR_FILENAME;
		}
		return mAudioAMRPath;
	}

	public static long getFileSize(String path) {
		File mFile = new File(path);
		if (!mFile.exists())
			return -1;
		return mFile.length();
	}

}
