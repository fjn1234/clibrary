package obj;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import utils.SystemUtil;
import utils.ViewUtil;

public class CustomAttrs {

    public int screenDesignWidth = 0, screenDesignHeight = 0;
    private static int screenWidth = 0, screenHeight = 0;

    public static void init(Context context) {
        Point point = SystemUtil.getScreenSize(context);
        int width = point.x;
        int height = point.y;
        if (context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setScreenWidth(width);
            setScreenHeight(height);
        } else {
            setScreenWidth(height);
            setScreenHeight(width);
        }
    }


    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        CustomAttrs.screenHeight = screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        CustomAttrs.screenWidth = screenWidth;
    }

    public void setScreenDesignWidth(int screenDesignWidth) {
        this.screenDesignWidth = screenDesignWidth;
    }

    public int getScreenDesignWidth() {
        return screenDesignWidth;
    }

    public void setScreenDesignHeight(int screenDesignHeight) {
        this.screenDesignHeight = screenDesignHeight;
    }

    public int getScreenDesignHeight() {
        return screenDesignHeight;
    }

    private float widthRatio, heightRatio, marginLeftRatio, marginRightRatio, marginTopRatio, marginBottomRatio,
            marginByWidthRatio, marginByHeight, marginTopByWidthRatio, marginBottomByWidthRatio,
            paddingLeftRatio, paddingRightRatio, paddingTopRatio, paddingBottomRatio, paddingTopByWidthRatio, paddingBottomByWidthRatio,
            minWidthRatio, maxWidthRatio, minHeightRatio, maxHeightRatio, heightByWidthRatio, widthByHeightRatio,
            cornerRatio, textSizeRatio, drawableLeftWidthRatio, drawableLeftHeightRatio, drawableRightWidthRatio, drawableRightHeightRatio,
            drawableTopWidthRatio, drawableTopHeightRatio, drawableBottomWidthRatio, drawableBottomHeightRatio, drawablePaddingRatio;
    private int direction, hideMode, drawableLeftResId, drawableRightResId, drawableTopResId, drawableBottomResId;
    private int marginLeftPx, marginRightPx, marginTopPx, marginBottomPx, paddingLeftPx, paddingRightPx, paddingTopPx, paddingBottomPx, maxWidthPx,
            strokeWidth,textSizePx, strokeColor = Integer.MAX_VALUE, solidColor = Integer.MAX_VALUE;
    private Drawable onClickBackground;
    private boolean toSquare = false;
    private String entityMapping = "", getMapping = "", setMapping = "", selectMapping = "", visibleMapping = "";

    public int getDrawablePadding() {
        return (int) Math.ceil(drawablePaddingRatio * screenWidth);
    }

    public void setDrawablePaddingRatio(String ratio) {
        this.drawablePaddingRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableLeftWidth() {
        return (int) Math.ceil(drawableLeftWidthRatio * screenWidth);
    }

    public void setDrawableLeftWidthRatio(String ratio) {
        this.drawableLeftWidthRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableLeftHeight() {
        return (int) Math.ceil(drawableLeftHeightRatio * screenHeight);
    }

    public void setDrawableLeftHeightRatio(String ratio) {
        this.drawableLeftHeightRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableRightWidth() {
        return (int) Math.ceil(drawableRightWidthRatio * screenWidth);
    }

    public void setDrawableRightWidthRatio(String ratio) {
        this.drawableRightWidthRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableRightHeight() {
        return (int) Math.ceil(drawableRightHeightRatio * screenHeight);
    }

    public void setDrawableRightHeightRatio(String ratio) {
        this.drawableRightHeightRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableTopWidth() {
        return (int) Math.ceil(drawableTopWidthRatio * screenWidth);
    }

    public void setDrawableTopWidthRatio(String ratio) {
        this.drawableTopWidthRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableTopHeight() {
        return (int) Math.ceil(drawableTopHeightRatio * screenHeight);
    }

    public void setDrawableTopHeightRatio(String ratio) {
        this.drawableTopHeightRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableBottomWidth() {
        return (int) Math.ceil(drawableBottomWidthRatio * screenWidth);
    }

    public void setDrawableBottomWidthRatio(String ratio) {
        this.drawableBottomWidthRatio = ViewUtil.getFloat(ratio);
    }

    public int getDrawableBottomHeight() {
        return (int) Math.ceil(drawableBottomHeightRatio * screenHeight);
    }

    public void setDrawableBottomHeightRatio(String ratio) {
        this.drawableBottomHeightRatio = ViewUtil.getFloat(ratio);
    }


    public int getDrawableLeftResId() {
        return drawableLeftResId;
    }

    public void setDrawableLeftResId(int drawableLeftResId) {
        this.drawableLeftResId = drawableLeftResId;
    }

    public int getDrawableRightResId() {
        return drawableRightResId;
    }

    public void setDrawableRightResId(int drawableRightResId) {
        this.drawableRightResId = drawableRightResId;
    }

    public int getDrawableTopResId() {
        return drawableTopResId;
    }

    public void setDrawableTopResId(int drawableTopResId) {
        this.drawableTopResId = drawableTopResId;
    }

    public int getDrawableBottomResId() {
        return drawableBottomResId;
    }

    public void setDrawableBottomResId(int drawableBottomResId) {
        this.drawableBottomResId = drawableBottomResId;
    }

    public int getHideMode() {
        return hideMode;
    }

    public void setHideMode(int hideMode) {
        this.hideMode = hideMode;
    }

    public void setEntityMapping(String entityMapping) {
        this.entityMapping = ViewUtil.getString(entityMapping);
    }

    public String getEntityMapping() {
        return entityMapping;
    }

    public String getGetMapping() {
        return getMapping;
    }

    public void setGetMapping(String getMapping) {
        this.getMapping = ViewUtil.getString(getMapping);
    }

    public String getSetMapping() {
        return setMapping;
    }

    public void setSetMapping(String setMapping) {
        this.setMapping = ViewUtil.getString(setMapping);
    }

    public String getSelectMapping() {
        return selectMapping;
    }

    public void setSelectMapping(String selectMapping) {
        this.selectMapping = ViewUtil.getString(selectMapping);
    }

    public String getVisibleMapping() {
        return visibleMapping;
    }

    public void setVisibleMapping(String visibleMapping) {
        this.visibleMapping = ViewUtil.getString(visibleMapping);
    }

    public void toSquare(boolean toSquare) {
        this.toSquare = toSquare;
    }

    public boolean isToSquare() {
        return toSquare;
    }

    public int getTextSize() {
        return (int) Math.ceil(textSizeRatio * screenWidth);
    }

    public void setTextSizeRatio(String textSizeRatio) {
        this.textSizeRatio = ViewUtil.getFloat(textSizeRatio);
    }

    public void setTextSizePx(int textSizePx) {
        this.textSizePx = textSizePx;
    }

    public int getTextSizePx() {
        return textSizePx;
    }

    public void setTextSizePxRatio(int textSize) {
        this.textSizeRatio = getPxToWidthRatio(textSize);
    }

    public int getHeightByWidth() {
        return (int) Math.ceil(heightByWidthRatio * screenWidth);
    }

    public void setHeightByWidthRatio(String widthRatio) {
        this.heightByWidthRatio = ViewUtil.getFloat(widthRatio);
    }

    public int getWidthByHeight() {
        return (int) Math.ceil(widthByHeightRatio * screenHeight);
    }

    public void setWidthByHeightRatio(String heightRatio) {
        this.widthByHeightRatio = ViewUtil.getFloat(heightRatio);
    }

    public int getWidth() {
        return (int) Math.ceil(widthRatio * screenWidth);
    }

    public void setWidthRatio(String widthRatio) {
        this.widthRatio = ViewUtil.getFloat(widthRatio);
    }

    public void setWidthPxRatio(int width) {
        this.widthRatio = getPxToWidthRatio(width);
    }

    public int getHeight() {
        return (int) Math.ceil(heightRatio * screenHeight);
    }

    public void setHeightRatio(String heightRatio) {
        this.heightRatio = ViewUtil.getFloat(heightRatio);
    }

    public void setHeightPxRatio(int height) {
        this.heightRatio = getPxToHeightRatio(height);
    }

    public int getMarginLeftPx() {
        return marginLeftPx;
    }

    public void setMarginLeftPx(int marginLeftPx) {
        this.marginLeftPx = marginLeftPx;
    }

    public int getMarginRightPx() {
        return marginRightPx;
    }

    public void setMarginRightPx(int marginRightPx) {
        this.marginRightPx = marginRightPx;
    }

    public int getMarginTopPx() {
        return marginTopPx;
    }

    public void setMarginTopPx(int marginTopPx) {
        this.marginTopPx = marginTopPx;
    }

    public int getMarginBottomPx() {
        return marginBottomPx;
    }

    public void setMarginBottomPx(int marginBottomPx) {
        this.marginBottomPx = marginBottomPx;
    }

    public int getPaddingLeftPx() {
        return paddingLeftPx;
    }

    public void setPaddingLeftPx(int paddingLeftPx) {
        this.paddingLeftPx = paddingLeftPx;
    }

    public int getPaddingRightPx() {
        return paddingRightPx;
    }

    public void setPaddingRightPx(int paddingRightPx) {
        this.paddingRightPx = paddingRightPx;
    }

    public int getPaddingTopPx() {
        return paddingTopPx;
    }

    public void setPaddingTopPx(int paddingTopPx) {
        this.paddingTopPx = paddingTopPx;
    }

    public int getPaddingBottomPx() {
        return paddingBottomPx;
    }

    public void setPaddingBottomPx(int paddingBottomPx) {
        this.paddingBottomPx = paddingBottomPx;
    }

    public int getMinWidth() {
        return (int) Math.ceil(minWidthRatio * screenWidth);
    }

    public void setMinWidthRatio(String minWidthRatio) {
        this.minWidthRatio = ViewUtil.getFloat(minWidthRatio);
    }

    public int getMaxWidth() {
        return (int) Math.ceil(maxWidthRatio * screenWidth);
    }

    public void setMaxWidthRatio(String maxWidthRatio) {
        this.maxWidthRatio = ViewUtil.getFloat(maxWidthRatio);
    }

    public float getMaxWidthRatio() {
        return maxWidthRatio;
    }

    public void setMaxWidthPxRatio(int maxWidth) {
        this.maxWidthRatio = getPxToWidthRatio(maxWidth);
    }

    public int getMaxWidthPx() {
        return maxWidthPx;
    }

    public void setMaxWidthPx(int maxWidthPx) {
        this.maxWidthPx = maxWidthPx;
    }


    public int getMinHeight() {
        return (int) Math.ceil(minHeightRatio * screenHeight);
    }

    public void setMinHeightRatio(String minHeightRatio) {
        this.minHeightRatio = ViewUtil.getFloat(minHeightRatio);
    }

    public int getMaxHeight() {
        return (int) Math.ceil(maxHeightRatio * screenHeight);
    }


    public int getMarginLeft() {
        return (int) Math.ceil(marginLeftRatio * screenWidth);
    }

    public void setMarginLeftRatio(String marginLeftRatio) {
        this.marginLeftRatio = ViewUtil.getFloat(marginLeftRatio);
    }

    public void setMarginLeftPxRatio(int marginLeft) {
        this.marginLeftRatio = getPxToWidthRatio(marginLeft);
    }

    public int getMarginRight() {
        return (int) Math.ceil(marginRightRatio * screenWidth);
    }

    public void setMarginRightRatio(String marginRightRatio) {
        this.marginRightRatio = ViewUtil.getFloat(marginRightRatio);
    }

    public void setMarginRightPxRatio(int marginRight) {
        this.marginRightRatio = getPxToWidthRatio(marginRight);
    }

    public int getMarginTop() {
        return (int) Math.ceil(marginTopRatio * screenHeight);
    }

    public void setMarginTopRatio(String marginTopRatio) {
        this.marginTopRatio = ViewUtil.getFloat(marginTopRatio);
    }

    public void setMarginTopPxRatio(int marginTop) {
        this.marginTopRatio = getPxToHeightRatio(marginTop);
    }

    public int getMarginBottom() {
        return (int) Math.ceil(marginBottomRatio * screenHeight);
    }

    public void setMarginBottomRatio(String marginBottomRatio) {
        this.marginBottomRatio = ViewUtil.getFloat(marginBottomRatio);
    }

    public void setMarginBottomPxRatio(int marginBottom) {
        this.marginBottomRatio = getPxToHeightRatio(marginBottom);
    }

    public int getMarginTopByWidth() {
        return (int) Math.ceil(marginTopByWidthRatio * screenWidth);
    }

    public void setMarginTopByWidthRatio(String marginTopByWidthRatio) {
        this.marginTopByWidthRatio = ViewUtil.getFloat(marginTopByWidthRatio);
    }

    public int getMarginBottomByWidth() {
        return (int) Math.ceil(marginBottomByWidthRatio * screenWidth);
    }

    public void setMarginBottomByWidthRatio(String marginBottomByWidthRatio) {
        this.marginBottomByWidthRatio = ViewUtil.getFloat(marginBottomByWidthRatio);
    }

    public int getPaddingTopByWidth() {
        return (int) Math.ceil(paddingTopByWidthRatio * screenWidth);
    }

    public void setPaddingTopByWidthRatio(String paddingTopByWidthRatio) {
        this.paddingTopByWidthRatio = ViewUtil.getFloat(paddingTopByWidthRatio);
    }

    public int getPaddingBottomByWidth() {
        return (int) Math.ceil(paddingBottomByWidthRatio * screenWidth);
    }

    public void setPaddingBottomByWidthRatio(String paddingBottomByWidthRatio) {
        this.paddingBottomByWidthRatio = ViewUtil.getFloat(paddingBottomByWidthRatio);
    }

    public int getMarginByWidth() {
        return (int) Math.ceil(marginByWidthRatio * screenWidth);
    }

    public void setMarginByWidthRatio(String marginByWidthRatio) {
        this.marginByWidthRatio = ViewUtil.getFloat(marginByWidthRatio);
    }

    public int getMarginByHeight() {
        return (int) Math.ceil(marginByHeight * screenHeight);
    }

    public void setMarginByHeightRatio(String marginByHeight) {
        this.marginByHeight = ViewUtil.getFloat(marginByHeight);
    }

    public int getPaddingLeft() {
        return (int) Math.ceil(paddingLeftRatio * screenWidth);
    }

    public void setPaddingLeftRatio(String paddingLeftRatio) {
        this.paddingLeftRatio = ViewUtil.getFloat(paddingLeftRatio);
    }

    public void setPaddingLeftPxRatio(int paddingLeft) {
        this.paddingLeftRatio = getPxToWidthRatio(paddingLeft);
    }

    public int getPaddingRight() {
        return (int) Math.ceil(paddingRightRatio * screenWidth);
    }

    public void setPaddingRightRatio(String paddingRightRatio) {
        this.paddingRightRatio = ViewUtil.getFloat(paddingRightRatio);
    }

    public void setPaddingRightPxRatio(int paddingRight) {
        this.paddingRightRatio = getPxToWidthRatio(paddingRight);
    }

    public int getPaddingTop() {
        return (int) Math.ceil(paddingTopRatio * screenHeight);
    }

    public void setPaddingTopRatio(String paddingTopRatio) {
        this.paddingTopRatio = ViewUtil.getFloat(paddingTopRatio);
    }

    public void setPaddingTopPxRatio(int paddingTop) {
        this.paddingTopRatio = getPxToHeightRatio(paddingTop);
    }

    public int getPaddingBottom() {
        return (int) Math.ceil(paddingBottomRatio * screenHeight);
    }

    public void setPaddingBottomRatio(String paddingBottomRatio) {
        this.paddingBottomRatio = ViewUtil.getFloat(paddingBottomRatio);
    }

    public void setPaddingBottomPxRatio(int paddingBottom) {
        this.paddingBottomRatio = getPxToHeightRatio(paddingBottom);
    }

    public float getWidthRatio() {
        return widthRatio;
    }

    public float getHeightRatio() {
        return heightRatio;
    }

    public float getMarginLeftRatio() {
        return marginLeftRatio;
    }

    public float getMarginRightRatio() {
        return marginRightRatio;
    }

    public float getMarginTopRatio() {
        return marginTopRatio;
    }

    public float getMarginBottomRatio() {
        return marginBottomRatio;
    }

    public float getPaddingLeftRatio() {
        return paddingLeftRatio;
    }

    public float getPaddingRightRatio() {
        return paddingRightRatio;
    }

    public float getPaddingTopRatio() {
        return paddingTopRatio;
    }

    public float getPaddingBottomRatio() {
        return paddingBottomRatio;
    }

    public float getCornerRatio() {
        return this.cornerRatio;
    }

    public void setCornerRatio(String cornerRatio) {
        this.cornerRatio = ViewUtil.getFloat(cornerRatio);
    }

    public int getDirection() {
        return this.direction;
    }

    public void setCornerDirection(int direction) {
        this.direction = direction;
    }

    public Drawable getOnClickBackground() {
        return onClickBackground;
    }

    public void setOnClickBackground(Drawable onClickBackground) {
        this.onClickBackground = onClickBackground;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
    }

    public int getSolidColor() {
        return solidColor;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    //----------------------------------------------------------------
    public boolean hasGetMapping() {
        return !TextUtils.isEmpty(getMapping);
    }

    public boolean hasSetMapping() {
        return !TextUtils.isEmpty(setMapping);
    }

    public boolean hasSelectMapping() {
        return !TextUtils.isEmpty(selectMapping);
    }

    public boolean hasVisibleMapping() {
        return !TextUtils.isEmpty(visibleMapping);
    }

    //-------------------------------------------------------------------------------

    public float getPxToWidthRatio(int val) {
        return (float) val / screenDesignWidth;
    }

    public float getPxToHeightRatio(int val) {
        return (float) val / screenDesignHeight;
    }
}
