package obj;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.util.Hashtable;

import io.CFile;
import utils.DecodeUtil;
import utils.ViewUtil;

public class CImageAttrs {

    public static int maxGolbalScale = 250;
    private boolean toCircle = false, cacheToMemory = false, matrixMode = false, toSquare = false;
    private float zoom = 1.0f, corner = -1.0f, autoScaleRatio = 0f;
    private int autoScalePx = 0, scaleByHeightPx = 0, scaleByWidthPx = 0, autoUpdateSpace = -1, customWidth = 0, customHeight = 0, maxCustomScale = 0;
    private String cachePath = "", tempFilePath = "", pathMd5 = "", loadPath = "", customSize = "0,0";
    private LoadType loadType = LoadType.None;
    private Bitmap.Config bitmapConfig;

    public void setBitmapConfig(int bitmapConfig) {
        switch (bitmapConfig) {
            case 1:
                this.bitmapConfig = Bitmap.Config.ALPHA_8;
                break;
            case 3:
                this.bitmapConfig = Bitmap.Config.RGB_565;
                break;
            case 5:
                this.bitmapConfig = Bitmap.Config.ARGB_8888;
                break;
            default:
                this.bitmapConfig = Bitmap.Config.ARGB_4444;
                break;
        }
    }

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    public enum LoadType {
        Disk, Url, Oss, None
    }

    public void setLoadType(int type) {
        switch (type) {
            case 1:
                loadType = LoadType.Disk;
                break;
            case 2:
                loadType = LoadType.Url;
                break;
            case 3:
                loadType = LoadType.Oss;
                break;
            default:
                loadType = LoadType.None;
                break;
        }
    }

    public LoadType getLoadType() {
        return loadType;
    }

    public void setMatrixMode(boolean matrixMode) {
        this.matrixMode = matrixMode;
    }

    public boolean isMatrixMode() {
        Hashtable<String, String> ht = new Hashtable<>(10);
        return matrixMode;
    }

    public void setToSquare(boolean toSquare) {
        this.toSquare = toSquare;
    }

    public boolean isToSquare() {
        return toSquare;
    }

    public int getCustomWidth() {
        return customWidth;
    }

    public void setCustomWidth(int customWidth) {
        this.customWidth = customWidth;
    }

    public int getCustomHeight() {
        return customHeight;
    }

    public void setCustomHeight(int customHeight) {
        this.customHeight = customHeight;
    }

    public String getCustomSize() {
        return customSize;
    }

    public void setCustomSize(String customSize) {
        if (customSize == null || TextUtils.isEmpty(customSize)) return;
        try {
            customWidth = Integer.parseInt(getCustomSize().split(",")[0]);
            customHeight = Integer.parseInt(getCustomSize().split(",")[1]);
        } catch (Exception e) {
        }
    }

    public int getScaleByHeightPx() {
        return scaleByHeightPx;
    }

    public void setScaleByHeightPx(int scaleByHeightPx) {
        this.scaleByHeightPx = scaleByHeightPx;
    }

    public int getScaleByWidthPx() {
        return scaleByWidthPx;
    }

    public void setScaleByWidthPx(int scaleByWidthPx) {
        this.scaleByWidthPx = scaleByWidthPx;
    }

    public int getAutoScalePx() {
        //autoScaleRatio根据屏幕计算出调整比例
        int autoScale = (int) Math.ceil(getAutoScaleRatio() * CustomAttrs.getScreenWidth());
        //如果在大屏低分辨率下，为避免计算值过低，使用autoScalePx
        autoScale = Math.max(autoScale, autoScalePx);
        //没有设置autoScaleRatio或autoScalePx则不进行缩放
        if (autoScale == 0) return 0;
        //如果没有设置最大值则取默认的最大值,防止oom
        int maxScale = maxCustomScale == 0 ? maxGolbalScale : maxCustomScale;
        //如果在高分辨率下，为避免计算值过高，maxScale
        return Math.min(maxScale, autoScale);
    }

    public void setAutoScalePx(int autoScalePx) {
        this.autoScalePx = autoScalePx;
    }

    public void setAutoScaleRatio(String autoScaleRatio) {
        this.autoScaleRatio = ViewUtil.getFloat(autoScaleRatio);
    }

    public float getAutoScaleRatio() {
        return autoScaleRatio;
    }

    public boolean isToCircle() {
        return toCircle;
    }

    public void setToCircle(boolean toCircle) {
        this.toCircle = toCircle;
    }


    public boolean isCacheToMemory() {
        return cacheToMemory;
    }

    public void setCacheToMemory(boolean cacheToMemory) {
        this.cacheToMemory = cacheToMemory;
    }


    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    //    public float getCorner(int height) {
//        if (corner < 0) return 0;
//        else if (corner > 1) return height * 1.f / 2;
//        else return corner * height * 1.f / 2;
//    }
    public float getCorner() {
        return corner;
    }

    public void setCorner(float corner) {
        this.corner = corner;
    }

    public int getMaxCustomScale() {
        return maxCustomScale;
    }

    public void setMaxCustomScale(int maxCustomScale) {
        this.maxCustomScale = maxCustomScale;
    }

    public int getAutoUpdateSpace() {
        return autoUpdateSpace;
    }

    public void setAutoUpdateSpace(int autoUpdateSpace) {
        this.autoUpdateSpace = autoUpdateSpace;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        if (cachePath == null || TextUtils.isEmpty(cachePath)) return;
        CFile file = new CFile(cachePath);
        file.mkdirs();
        this.cachePath = cachePath;
    }

    public String getCacheFilePath() {
        return getCachePath() + "/" + getFilenameMd5();
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        if (TextUtils.isEmpty(tempFilePath)) return;
        CFile file = new CFile(tempFilePath);
        file.mkdirs();
        this.tempFilePath = tempFilePath;
    }

    public String getPathMd5() {
        return pathMd5;
    }

    public void setPathMd5(String pathMd5) {
        this.pathMd5 = pathMd5;
    }

    public String getLoadPath() {
        return loadPath;
    }

    public String getFilenameMd5() {
        return DecodeUtil.getMD5(getLoadPath());
    }

    public void setLoadPath(String loadPath) {
        this.loadPath = loadPath;
    }

    public String getCacheName() {
        if (TextUtils.isEmpty(getLoadPath())) return "";
        return DecodeUtil.getMD5(getCacheFilePath());
    }

    public String getCacheName(String path) {
        setLoadPath(path);
        return getCacheName();
    }

}
