package dialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import compatbility.ProgressDialogRevise;


public class ProgressBarDialog extends ProgressDialog {
	
	private ProgressBarDialog pBarDialog;
	private int mSS;
	private int pSS;
	
	public ProgressBarDialog(Context context) {
		super(context);
		pBarDialog=(ProgressBarDialog)this;
		this.mSS=0;
		this.pSS=0;
		//dialog.setProgressPercentFormat(null);
	}
	
	@Override
	public void show() {
		super.show();	
		if (this.mSS>this.pSS)
		{
		handler.sendEmptyMessage(0);
		}
	}
	
 @SuppressLint("NewApi") @Override
public void setMax(int max) {
	int mm=max/60;
	int ss=max%60;
	this.mSS=max;
	//pBarDialog.setProgressNumberFormat(mm+":"+ss);
	ProgressDialogRevise.setProgressNumberFormat(pBarDialog, mm + ":" + ss);
	super.setMax(max);
}
 
 
	
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (pSS == mSS) {
				handler.removeMessages(0);
				pBarDialog.cancel();
				pBarDialog.dismiss();
			}
			else {
				pBarDialog.setProgress(pSS);
				pSS++;
				handler.sendMessageDelayed(new Message(), 1000);
			}
		}
	};

//	public void cancel() {
//		handler.removeMessages(0);
//		super.cancel();
//	};


}
