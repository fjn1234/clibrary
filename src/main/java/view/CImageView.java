package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hugh.clibrary.R;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import compatbility.BitmapRevise;
import interfaces.IView;
import io.CFile;
import obj.CImageAttrs;
import obj.CustomAttrs;
import uk.co.senab.photoview.PhotoViewAttacher;
import utils.DecodeUtil;
import utils.ImageUtil;
import utils.ViewUtil;


public class CImageView extends ImageView implements IView.ICustomAttrs, IView.IMapping {

    private CImageAttrs imageAttrs = new CImageAttrs();
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;
    protected static List<String> queues = Collections.synchronizedList(new ArrayList());
    private static int threadCount = 2;

    public CImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CImageView(Context context) {
        super(context);
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
        if (isInEditMode()) return;
        imageAttrs = new CImageAttrs();
        String cachePath = context.getExternalCacheDir().getAbsolutePath();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CImageView);
        imageAttrs.setCacheToMemory(ta.getBoolean(R.styleable.CImageView_ccacheToMemory, false));
        imageAttrs.setToCircle(ta.getBoolean(R.styleable.CImageView_ctoCircleImg, false));
        imageAttrs.setToSquare(ta.getBoolean(R.styleable.CImageView_ctoSquareImg, false));
        imageAttrs.setMatrixMode(ta.getBoolean(R.styleable.CImageView_cmatrixMode, false));
        imageAttrs.setCachePath(cachePath + ta.getString(R.styleable.CImageView_ccacheToDisk));
        imageAttrs.setTempFilePath(cachePath + ta.getString(R.styleable.CImageView_ctempFilePath));
        imageAttrs.setCustomSize(ta.getString(R.styleable.CImageView_ccustomSize));
        imageAttrs.setAutoScaleRatio(ta.getString(R.styleable.CImageView_cautoScaleRatio));
        imageAttrs.setAutoScalePx(ta.getInt(R.styleable.CImageView_cautoScale, 0));
        imageAttrs.setScaleByHeightPx(ta.getInt(R.styleable.CImageView_cscaleByHeight, 0));
        imageAttrs.setScaleByWidthPx(ta.getInt(R.styleable.CImageView_cscaleByWidth, 0));
        imageAttrs.setAutoUpdateSpace(ta.getInt(R.styleable.CImageView_cautoUpdate, -1));
        imageAttrs.setZoom(ta.getFloat(R.styleable.CImageView_czoom, 1));
        imageAttrs.setCorner(ta.getFloat(R.styleable.CImageView_ccornerImg, 0));
        imageAttrs.setMaxCustomScale(ta.getInteger(R.styleable.CImageView_cmaxCustomScale, 0));
        imageAttrs.setLoadType(ta.getInteger(R.styleable.CImageView_loadType, -1));
        imageAttrs.setBitmapConfig(ta.getInteger(R.styleable.CImageView_bitmapConfig, 4));
        ta.recycle();
    }

    public void loadCustomAttrs() {
        ViewUtil.loadCustomAttrs(this, mAttrs);
    }

    public void loadScreenArr() {
        ViewUtil.getParentScreenAttr(mAttrs, this);
        loadCustomAttrs();
    }

    public CustomAttrs getCustomAttrs() {
        return mAttrs;
    }

    public void setCustomAttrs(CustomAttrs mAttrs) {
        this.mAttrs = mAttrs;
        loadCustomAttrs();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (initCustomAttrs) {
            initCustomAttrs = false;
            loadScreenArr();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        setCustomAttr(context, attrs);
        if (keyOrderArray == null)
            keyOrderArray = new ArrayList<>();
        if (bitmapCache == null)
            bitmapCache = new Hashtable<>();
        if (imageAttrs.isMatrixMode()) {
            initMatrix();
        }
    }

    private boolean doAjust = true;
    private CFile file;

    @Override
    public void setMappingValue(String v) {
        switch (imageAttrs.getLoadType()) {
            case Disk:
                loadLocalBitmap(v);
                break;
            case Url:
                loadFromUrl(v);
                break;
        }
    }

    @Override
    public String getMappingValue() {
        return "";
    }

    public enum Result {
        NoTempPath, NoFile, NetError, IOError, Success, Loading, UrlError, UnknowError
    }

    public CImageAttrs getImageAttrs() {
        return imageAttrs;
    }

    public void setImageAttrs(CImageAttrs imageAttrs) {
        this.imageAttrs = imageAttrs;
    }

    private Bitmap adjustBitmap(Bitmap bm) {
        try {
            if (imageAttrs.isToCircle() && doAjust) {
                bm = ImageUtil.getRoundedCornerBitmap(bm, imageAttrs.getZoom(), imageAttrs.getBitmapConfig());
            }
            if (imageAttrs.isToSquare() && doAjust) {
                bm = ImageUtil.getSquareBitmap(bm, imageAttrs.getZoom(), imageAttrs.getBitmapConfig());
            }
            if (imageAttrs.getAutoScalePx() > 0) {
                bm = ImageUtil.scaleSmallByAuto(bm, imageAttrs.getAutoScalePx());
            }
            if (imageAttrs.getCustomWidth() > 0 && imageAttrs.getCustomHeight() > 0) {
                bm = ImageUtil.getCustomSizeBitmap(bm, imageAttrs.getCustomWidth(), imageAttrs.getCustomHeight(), imageAttrs.getBitmapConfig());
            }
            if (imageAttrs.getCorner() > 0 && doAjust) {
                bm = ImageUtil.getCornerBitmap(bm, imageAttrs.getCorner(), imageAttrs.getBitmapConfig());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bm;
    }

    @Override
    public void setImageResource(int resId) {
        setImageBitmap(BitmapFactory.decodeResource(getResources(), resId));
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null) {
            doFailCallback();
            return;
        }
        try {
            super.setImageBitmap(adjustBitmap(bm));
            if (loadImageCallback != null) {
                loadImageCallback.onSuccess(this, bm);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            doFailCallback();
        }
    }

    protected static ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

    public static void setThreadCount(int threadCount) {
        CImageView.threadCount = threadCount;
    }

    public Result loadLocalBitmap(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return Result.NoFile;
        try {
            imageAttrs.setLoadPath(filePath);
            file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
            Bitmap bitmap = loadFromCache();
            if (bitmap != null) {
                setImageBitmap(bitmap);
                return Result.Success;
            }
            if (threadPool.isShutdown())
                threadPool = Executors.newFixedThreadPool(threadCount);
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = loadFromCache();
                    if (bitmap == null) {
                        bitmap = ImageUtil.getBitmapFromFilePath(imageAttrs.getLoadPath(), imageAttrs.getAutoScalePx());
                        if (bitmap != null) {
                            doAjust = false;
                            bitmap = adjustBitmap(bitmap);
                            doAjust = true;
                            if (imageAttrs.isCacheToMemory())
                                cacheBitmap2(imageAttrs.getCacheName(), bitmap);
                            if (!TextUtils.isEmpty(imageAttrs.getCachePath())) {
                                cacheToDisk(bitmap, file);
                            }
                        }
                        setAnycLoadBitmap(bitmap);
                    }
                }
            });
            return Result.Loading;
        } catch (Exception ex) {
            ex.printStackTrace();
            doFailCallback();
            return Result.UnknowError;
        }
    }

    public static void clearThread() {
        if (!threadPool.isShutdown())
            threadPool.shutdownNow();
        if (queues.size() > 0)
            queues.clear();
    }

    public static void clearThreadQueues() {
        if (queues.size() > 0)
            queues.clear();
    }

    public Result loadFromUrl(final String url, boolean reload) {
        if (TextUtils.isEmpty(imageAttrs.getTempFilePath())) return Result.NoTempPath;
        if (TextUtils.isEmpty(url)) return Result.UrlError;
        imageAttrs.setLoadPath(url);
        if (!reload)
            loadFromUrl(url);
        else {
            file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
            loadFromNet();
        }
        return Result.Loading;
    }

    public Result loadFromUrl(final String url) {
        if (TextUtils.isEmpty(imageAttrs.getTempFilePath())) return Result.NoTempPath;
        if (TextUtils.isEmpty(url)) return Result.UrlError;
        try {
            imageAttrs.setLoadPath(url);
            file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
            if (imageAttrs.getAutoUpdateSpace() > -1) {
                long cacheDate = file.lastModified() / 60000;
                long currentDate = (new Date()).getTime() / 60000;
                printLog("cacheDate:" + cacheDate);
                printLog("currentDate:" + currentDate);
                printLog("space minute:" + (currentDate - cacheDate));
                if (currentDate - cacheDate >= imageAttrs.getAutoUpdateSpace()) {
                    loadFromNet();
                    printLog("load from update");
                    return Result.Loading;
                }
            }
            Bitmap bitmap = loadFromCache();
            if (bitmap != null) {
                setImageBitmap(bitmap);
                return Result.Success;
            }
            loadFromNet();
            return Result.Loading;
        } catch (Exception ex) {
            ex.printStackTrace();
            doFailCallback();
            return Result.UnknowError;
        }
    }

    private void loadFromNet() {
        if (threadPool.isShutdown()) {
            threadPool = Executors.newFixedThreadPool(threadCount);
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadFromCache();
                if (bitmap == null) {
                    final CFile file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
                    final String pathMd5 = DecodeUtil.getMD5(file.getAbsolutePath());
                    String tempFile = imageAttrs.getTempFilePath() + System.currentTimeMillis();
                    bitmap = ImageUtil.getBitmap(imageAttrs.getLoadPath(), tempFile, imageAttrs.getAutoScalePx());
                    if (bitmap != null) {
                        doAjust = false;
                        bitmap = adjustBitmap(bitmap);
                        doAjust = true;
                        if (imageAttrs.isCacheToMemory())
                            cacheBitmap2(pathMd5, bitmap);
                        if (!TextUtils.isEmpty(imageAttrs.getCachePath())) {
                            cacheToDisk(bitmap, file);
                        }
                    }
                }
                setAnycLoadBitmap(bitmap);
            }
        });
    }

    public Result loadQRCode(final String str) {
        if (TextUtils.isEmpty(imageAttrs.getTempFilePath())) return Result.NoTempPath;
        if (TextUtils.isEmpty(str)) return Result.UrlError;
        try {
            imageAttrs.setLoadPath(str);
            file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
            Bitmap bitmap = loadFromCache();
            if (bitmap != null) {
                setImageBitmap(bitmap);
                return Result.Success;
            }
            if (threadPool.isShutdown())
                threadPool = Executors.newFixedThreadPool(threadCount);
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = loadFromCache();
                    if (bitmap == null) {
                        final CFile file = new CFile(imageAttrs.getCachePath(), imageAttrs.getFilenameMd5());
                        final String pathMd5 = DecodeUtil.getMD5(file.getAbsolutePath());
                        bitmap = ImageUtil.createQRImage(imageAttrs.getLoadPath(), getCustomAttrs().getWidth());
                        if (bitmap != null) {
                            doAjust = false;
                            bitmap = adjustBitmap(bitmap);
                            doAjust = true;
                            if (imageAttrs.isCacheToMemory())
                                cacheBitmap2(pathMd5, bitmap);
                            if (!TextUtils.isEmpty(imageAttrs.getCachePath())) {
                                cacheToDisk(bitmap, file);
                            }
                        }
                    }
                    setAnycLoadBitmap(bitmap);
                }
            });
            return Result.Loading;
        } catch (Exception ex) {
            ex.printStackTrace();
            doFailCallback();
            return Result.UnknowError;
        }
    }

    private Bitmap loadFromCache() {
        Bitmap bitmap = null;
        if (imageAttrs.isCacheToMemory()) {
            bitmap = getCacheBitmap2(imageAttrs.getCacheName());
            if (bitmap != null) {
                printLog("load from memory");
                printLog("cacheSize:" + cacheSize);
                return bitmap;
            }
        }
        if (!TextUtils.isEmpty(imageAttrs.getCachePath())) {
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    printLog("load from disk");
                    if (imageAttrs.isCacheToMemory())
                        cacheBitmap2(imageAttrs.getCacheName(), bitmap);
                    return bitmap;
                }
            }
        }
        return bitmap;
    }

    private void setAnycLoadBitmap(final Bitmap bitmap) {
        post(new Runnable() {
            @Override
            public void run() {
                setImageBitmap(bitmap);
            }
        });
    }

    private void doFailCallback() {
        if (loadImageCallback == null) return;
        loadImageCallback.onFail();
    }

    protected LoadImageCallback loadImageCallback;

    public interface LoadImageCallback {
        void onBefore();

        void onSuccess(View v, Bitmap bm);

        void onFail();
    }

    public void setLoadImageCallback(LoadImageCallback loadImageCallback) {
        this.loadImageCallback = loadImageCallback;
    }

    // ---------------------图片缓存-------------------------------------------

    private void cacheToDisk(Bitmap bitmap, CFile file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------方法1---------------------------------------------

//    private class BitmapSoftReference extends SoftReference<Bitmap> {
//
//        public BitmapSoftReference(Bitmap r, ReferenceQueue<? super Bitmap> q) {
//            super(r, q);
//        }
//    }
//
//    private static Hashtable<String, BitmapSoftReference> bitmapRefs;
//    private static ReferenceQueue<Bitmap> queue;
//
//    public Bitmap getCacheBitmap(String pathMd5) {
//        Bitmap bitmap = null;
//        if (bitmapRefs.containsKey(pathMd5)) {
//            bitmap = bitmapRefs.get(pathMd5).get();
//        }
//        return bitmap;
//    }
//
//    public void cacheBitmap(String pathMd5, Bitmap bitmap) {
//        if (getCacheBitmap(pathMd5) != null)
//            return;
//        clearRef();
//        BitmapSoftReference bitmapRef = new BitmapSoftReference(bitmap, queue);
//        bitmapRefs.put(pathMd5, bitmapRef);
//        printLog("cache to memory|" + pathMd5 + ",size:"
//                + BitmapRevise.getBitmapSize(bitmap));
//    }
//
//    public void clearRef() {
//        BitmapSoftReference bitmapRef = null;
//        while ((bitmapRef = (BitmapSoftReference) queue.poll()) != null) {
//            bitmapRefs.remove(bitmapRef);
//        }
//    }
//
//    public void clearCache() {
//        clearRef();
//        bitmapRefs.clear();
//        System.gc();
//        System.runFinalization();
//    }

    // ----------------------方法2---------------------------------------------
    private static float clearRatio = 0.2f;
    private static int maxCache = 0;
    private static int cacheSize = 0;
    private static Hashtable<String, Bitmap> bitmapCache;
    private static ArrayList<String> keyOrderArray;

    private Bitmap getCacheBitmap2(String pathMd5) {
        Bitmap bitmap = null;
        if (bitmapCache.containsKey(pathMd5))
            return bitmapCache.get(pathMd5);
        return bitmap;
    }

    private void cacheBitmap2(String pathMd5, Bitmap bitmap) {
        if (maxCache == 0 || clearRatio < 0 || clearRatio > 1)
            return;
        clearRef2();
        Bitmap oldBitmap = bitmapCache.get(pathMd5);
        if (oldBitmap == null) {
            bitmapCache.put(pathMd5, bitmap);
            cacheSize += BitmapRevise.getBitmapSize(bitmap) / 1024;
            keyOrderArray.add(pathMd5);
        } else {
            cacheSize -= BitmapRevise.getBitmapSize(oldBitmap) / 1024;
            bitmapCache.remove(pathMd5);
            bitmapCache.put(pathMd5, bitmap);
            cacheSize += BitmapRevise.getBitmapSize(bitmap) / 1024;
            keyOrderArray.remove(pathMd5);
            keyOrderArray.add(pathMd5);
        }
        printLog("cacheSize:" + cacheSize);
    }

    public static void clearCache(String cacheName) {
        Bitmap oldBitmap = bitmapCache.get(cacheName);
        if (oldBitmap != null) {
            cacheSize -= BitmapRevise.getBitmapSize(oldBitmap) / 1024;
            bitmapCache.remove(cacheName);
            keyOrderArray.remove(cacheName);
        }
    }

    private void clearRef2() {
        if (cacheSize < maxCache)
            return;
        int clearSize = 0;
        ArrayList<String> clearArray = new ArrayList<>();
        for (String key : keyOrderArray) {
            clearSize += BitmapRevise.getBitmapSize(bitmapCache.get(key)) / 1024;
            clearArray.add(key);
            if (clearSize > maxCache * clearRatio)
                break;
        }
        for (String key : clearArray) {
            bitmapCache.remove(key);
            keyOrderArray.remove(key);
            printLog("clear:" + key);
        }
        cacheSize -= clearSize;
        System.gc();
        System.runFinalization();
    }

    public static void clearAllCache() {
        if (bitmapCache != null)
            bitmapCache.clear();
        if (keyOrderArray != null)
            keyOrderArray.clear();
        System.gc();
        System.runFinalization();
    }

    public static void setClearRatio(float clearRatio) {
        CImageView.clearRatio = clearRatio;
    }

    public static float getClearRatio() {
        return clearRatio;
    }

    public static void setMaxCache(int maxCache) {
        CImageView.maxCache = maxCache;
    }

    public static int getMaxCache() {
        return maxCache;
    }

    public static Hashtable<String, Bitmap> getBitmapCache() {
        return bitmapCache;
    }

    protected void printLog(String log) {
        if (false)
            Log.e(this.getClass().getSimpleName(), log);
    }


    //-----------------------拉伸模式---------------------------
    private PhotoViewAttacher mPhotoViewAttacher;

    public void initMatrix() {
        mPhotoViewAttacher = new PhotoViewAttacher(this);
    }

    public void setOnViewTapListener(PhotoViewAttacher.OnViewTapListener listener) {
        if (mPhotoViewAttacher != null) {
            mPhotoViewAttacher.setOnViewTapListener(listener);
        }
    }

    public void clearPhotoViewAttacher() {
        if (mPhotoViewAttacher != null)
            mPhotoViewAttacher.cleanup();
    }

    public PhotoViewAttacher getPhotoViewAttacher() {
        return mPhotoViewAttacher;
    }

}
