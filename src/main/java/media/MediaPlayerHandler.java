package media;

import android.media.MediaPlayer;

import java.io.IOException;

public class MediaPlayerHandler {
	private static MediaPlayer mediaPlayer;
	private OnStopCallback onStopCallback;
	private OnPreparedCallback onPreparedCallback;
	private OnGotDurationCallback onGotDurationCallback;

	public MediaPlayerHandler() {
	}

	public void getDuration(String filePath) {
		if (mediaPlayer != null) {
			stop();
		}
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(filePath);
			mediaPlayer
			.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					if (onGotDurationCallback != null)
						onGotDurationCallback.onGotDuration(mediaPlayer.getDuration());
				}
			});
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play(String filePath) {
		if (mediaPlayer != null) {
			stop();
		}
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							if (onPreparedCallback != null)
								onPreparedCallback.onPrepared();
							mp.start();
						}
					});
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							if (onStopCallback != null) {
								onStopCallback.onStop();
							}
						}
					});
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void setOnStopCallback(OnStopCallback onStopCallback) {
		this.onStopCallback = onStopCallback;
	}

	public void setOnPreparedCallback(OnPreparedCallback onPreparedCallback) {
		this.onPreparedCallback = onPreparedCallback;
	}
	public void setGotDurationCallback(OnGotDurationCallback onGotDurationCallback) {
		this.onGotDurationCallback = onGotDurationCallback;
	}

	public abstract interface OnStopCallback {
		void onStop();
	}

	public abstract interface OnPreparedCallback {
		void onPrepared();
	}
	
	public abstract interface OnGotDurationCallback {
		void onGotDuration(int duration);
	}

}
