package compatbility;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;

public class ProgressDialogRevise {

	public static void setProgressNumberFormat(ProgressDialog pBarDialog,String format) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			setProgressNumberFormatApi11(pBarDialog,format);
		}
		
	}
	
	@SuppressLint("NewApi") private static void setProgressNumberFormatApi11(ProgressDialog pBarDialog,String format) {
		pBarDialog.setProgressNumberFormat(format);
	}
}
