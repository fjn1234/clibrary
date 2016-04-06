package compatbility;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

public class DisplayRevise {
	public static Point getPoint(Display display) {
		Point point = null;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			point = getPointApi13(display);
		} else {
			point = getPointApi1(display);
		}

		return point;
	}

	@SuppressLint("NewApi") private static Point getPointApi13(Display display) {
		Point point = new Point();
		display.getSize(point);
		return point;
	}
	
	@SuppressWarnings("deprecation")
	private static Point getPointApi1(Display display)
	{
		return new Point(display.getWidth(), display.getHeight());
	}
}
