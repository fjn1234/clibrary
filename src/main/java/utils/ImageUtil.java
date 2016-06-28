package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

import compatbility.DisplayRevise;
import io.CFile;
import obj.CApplication;


public class ImageUtil {



    public static int getRadiusHeight(int width, int height, int customWidth) {
        float radius = (customWidth * 1.0f) / (width * 1.0f);
        int customHeight = (int) (height * radius);
        return customHeight;
    }

    public static int getRadiusWidth(int width, int height, int customHeight) {
        float radius = (customHeight * 1.0f) / (height * 1.0f);
        int customWidth = (int) (width * radius);
        return customWidth;
    }

    public static Bitmap scaleByWidth(Bitmap bitmap, int customWidth) {
        Matrix matrix = new Matrix();
        float scale = customWidth * 1.0f / bitmap.getWidth();
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    public static Bitmap scaleByHeight(Bitmap bitmap, int customHeight) {
        Matrix matrix = new Matrix();
        float scale = customHeight * 1.0f / bitmap.getHeight();
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    public static Bitmap scaleByAuto(Bitmap bitmap, int custom) {
        if (bitmap.getWidth() > bitmap.getHeight())
            return scaleByWidth(bitmap, custom);
        else
            return scaleByHeight(bitmap, custom);
    }

    public static Bitmap scaleSmallByAuto(Bitmap bitmap, int custom) {
        int max = Math.max(bitmap.getWidth(), bitmap.getHeight());
        if (max < custom) return bitmap;
        return scaleByAuto(bitmap, custom);
    }

    //按屏幕比例缩小
    public static Bitmap scaleSmallByRatios(Bitmap bitmap, int width, int height) {
        float scale = Math.min(width * 1.0f / bitmap.getWidth(), height * 1.0f / bitmap.getHeight());
        if (scale < 1 && scale > 0) {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    public static Bitmap getBitmap(String imageUri, String tempPath, int adjustPx) {
        Bitmap bitmap = null;
        try {
            // 显示网络上的图片

            System.out.println(imageUri);
            URL myFileUrl = new URL(imageUri);
//            URL myFileUrl = new URL("http://imgphoto.gmw.cn/attachement/jpg/site2/20110812/14feb5e05c0c0faed0694f.jpg");

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            boolean b = FileUtil.inputstreamToFile(is, tempPath);
            if (b == false) {
                return null;
            }
            is.close();
            bitmap = getAdjustBitmap(tempPath, adjustPx);
        } catch (Exception e) {
            e.printStackTrace();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromFilePath(String filePath, int adjustPx) {
        return getAdjustBitmap(filePath, adjustPx);
    }

    public static Bitmap getAdjustBitmap(String filePath, int adjustPx) {
        Bitmap bitmap = null;
        try {
            if (adjustPx <= 0) {
                bitmap = BitmapFactory.decodeFile(filePath);
                return bitmap;
            }
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, op);
            if (op.outWidth == -1 || op.outHeight == -1) {
                bitmap = BitmapFactory.decodeFile(filePath);
                return bitmap;
            }
            int adjustWidth, adjustHeight;
            if (op.outWidth < op.outHeight) {
                adjustHeight = adjustPx;
                adjustWidth = getRadiusWidth(op.outWidth, op.outHeight,
                        adjustHeight);
            } else {
                adjustWidth = adjustPx;
                adjustHeight = getRadiusHeight(op.outWidth, op.outHeight,
                        adjustWidth);
            }
            int wRatio = (int) Math.ceil(op.outWidth / (float) adjustWidth);
            int hRatio = (int) Math.ceil(op.outHeight / (float) adjustHeight);
            if (wRatio > 1 && hRatio > 1) {
                if (wRatio > hRatio) {
                    op.inSampleSize = wRatio;
                } else {
                    op.inSampleSize = hRatio;
                }
            }
            op.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, op);

        } catch (Exception e) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromFilePath(String filePath, int adjWidth, int adjHeight) {
        Bitmap bitmap = null;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, op);
        int wRatio = (int) Math.ceil(op.outWidth / (float) adjWidth);
        int hRatio = (int) Math.ceil(op.outHeight / (float) adjHeight);

        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, op);
        return bitmap;
    }


    public static boolean captureScreen(Activity activity, String path,
                                        View view) {
        FileOutputStream fos = null;
        boolean result = false;
        Bitmap bitmap = null;
        try {
            bitmap = captureViewBitmap(activity, view);
            // 获取屏幕
            View decorview = view;
            decorview.setDrawingCacheEnabled(true);
            bitmap = decorview.getDrawingCache();
            // 保存Bitmap
            CFile file = new CFile(path);
            file.createNewFileAndDirectory();
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null)
                bitmap.recycle();
        }
        return result;
    }

    public static boolean captureScreen(Activity activity, String path) {
        return captureScreen(activity, path, activity.getWindow().getDecorView());
    }

    public static Bitmap captureViewBitmap(Activity activity, View view) {
        // 构建Bitmap
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point point = DisplayRevise.getPoint(display);
        Bitmap bitmap = Bitmap.createBitmap(point.x, point.y, Config.ARGB_8888);
        // 获取屏幕
        View decorview = view;
        decorview.setDrawingCacheEnabled(true);
        bitmap = decorview.getDrawingCache();
        return bitmap;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float zoom, Config config) {
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(width, width, config);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        int radius = (int) (width * zoom);
        int left = width / 2 - radius / 2;
        int top = width / 2 - radius / 2;
        int right = left + radius;
        int bottom = top + radius;
        final Rect rect = new Rect(left, top, right, bottom);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, width, width, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getSquareBitmap(Bitmap bitmap, float zoom, Config config) {
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int high=Math.max(bitmap.getWidth(), bitmap.getHeight());
        int ca=(high-width)/2;
        Bitmap output = Bitmap.createBitmap(width, width, config);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        int radius = (int) (width * zoom);
        int left = width / 2 - radius / 2;
        int top = width / 2 - radius / 2;
        int right = left + radius;
        int bottom = top + radius;
        final Rect rect = new Rect(left, top, right, bottom);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public static Bitmap getCentreBitmap(Bitmap bitmap, float zoom, Config config) {
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int high=Math.max(bitmap.getWidth(), bitmap.getHeight());
        int ca=(high-width)/2;
        Bitmap output = Bitmap.createBitmap(bitmap,0,ca,width,width);
        int radius = (int) (width * zoom);
        int to = width / 2 - radius / 2;
        output=Bitmap.createBitmap(output,to,to,radius,radius);
        return output;
    }

    public static Bitmap getCenterSquareBitmap(Bitmap bitmap, float zoom, Bitmap.Config config) {
        if (bitmap.getWidth() < bitmap.getHeight()) {
            int bWidth = bitmap.getWidth(), bHeight = bitmap.getHeight();
            int width = (int) (bitmap.getWidth() * zoom);
            Bitmap output = Bitmap.createBitmap(width, width, config);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            int left = (bWidth - width) / 2;
            int top = bHeight / 2 - width / 2;
            final Rect rect = new Rect(0, 0, width, width);
            final Rect rects = new Rect(left, top, left + width, top + width);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            canvas.drawBitmap(bitmap, rects, rect, paint);
            return output;
        } else {
            return null;
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float corner, float zoom, Config config) {
        int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(width, width, config);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        int radius = (int) (width * zoom);
        int left = width / 2 - radius / 2;
        int top = width / 2 - radius / 2;
        int right = left + radius;
        int bottom = top + radius;
        final Rect rect = new Rect(left, top, right, bottom);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getCornerBitmap(Bitmap bitmap, float corner, Config config) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getHeight() * 0.5f * corner, bitmap.getHeight() * 0.5f * corner, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getCustomSizeBitmap(Bitmap bitmap, int customwidth, int customheight, Config config) {
        int width = Math.min(bitmap.getWidth(), customwidth);
        int height = Math.min(bitmap.getHeight(), customheight);
        Bitmap output = Bitmap.createBitmap(width, customheight, config);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createQRImage(String qrstr, int width) {
        try {
            int QR_WIDTH = 200, QR_HEIGHT = 200;
            if (width > 0)
                QR_WIDTH = QR_HEIGHT = width;
            if (qrstr == null || "".equals(qrstr) || qrstr.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrstr,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean bitmapToFile(Bitmap bitmap, CFile file) {
        try {
            if (!file.exists())
                file.createNewFileAndDirectory();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasFace(Bitmap bitmap, int numberOfFace) {
        if (hasFaceCount(bitmap, numberOfFace) > 0) return true;
        else
            return false;
    }

    public static boolean hasFace(String path, int numberOfFace) {
        if (hasFaceCount(path, numberOfFace) > 0) return true;
        else
            return false;
    }

    public static int hasFaceCount(Bitmap bitmap, int numberOfFace) {
        CFile file = new CFile(Environment.getExternalStorageDirectory(), "temp");
        ImageUtil.bitmapToFile(bitmap, file);
        return hasFaceCount(file.getAbsolutePath(), numberOfFace);
    }

    public static int hasFaceCount(String path, int numberOfFace) {
        BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
        BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;  //构造位图生成的参数，必须为565
        Bitmap bitmap = BitmapFactory.decodeFile(path, BitmapFactoryOptionsbfo);
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        FaceDetector.Face[] myFace = new FaceDetector.Face[numberOfFace];       //分配人脸数组空间
        FaceDetector myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
        return myFaceDetect.findFaces(bitmap, myFace);    //FaceDetector 构造实例并解析人脸
    }


    public static Bitmap drawableToBitamp(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }


    public static Bitmap doBlur(Bitmap bm, float radius) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return null;
        }

        Bitmap bitmap = bm.copy(bm.getConfig(), true);
        final RenderScript rs = RenderScript.create(CApplication.getAppContext());
        final Allocation input = Allocation.createFromBitmap(rs, bm, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static Bitmap decodeBitmap(Context context, Uri uri) {
        try {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (Exception ex) {
            return null;
        }
    }
}
