package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugh.clibrary.R;

import java.lang.reflect.Method;
import java.util.HashMap;

import compatbility.DrawableRevise;
import interfaces.IView;
import obj.CustomAttrs;
import view.CButton;
import view.CEditText;
import view.CTextView;


public class ViewUtil {

    // 默认字体大小--对应标注图24px
    private static final float DEFAULT_TEXT_SIZE = 0.0375f * CustomAttrs.getScreenWidth();

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * <p>
     * （DisplayMetrics类中属性scaledDensity）
     *
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static Point getScreen(Activity aty) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        aty.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return new Point(mDisplayMetrics.widthPixels,
                mDisplayMetrics.heightPixels);
    }


    private static float initX, initY, offsetX, offsetY;

    public static MoveDirection getDirection(MotionEvent event) {
        MoveDirection direction = MoveDirection.UNDOWN;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initX = event.getX();
                initY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                offsetX = event.getX() - initX;
                offsetY = event.getY() - initY;
                if (Math.abs(offsetX) - Math.abs(offsetY) > 0) {
                    if (offsetX > 5) {
                        direction = MoveDirection.RIGHT;
                    } else if (offsetX < -5) {
                        direction = MoveDirection.LEFT;
                    }
                } else {
                    if (offsetY > 5) {
                        direction = MoveDirection.DOWN;
                    } else if (offsetY < -5) {
                        direction = MoveDirection.UP;
                    }
                }
                break;
        }
        return direction;
    }


    public enum MoveDirection {
        LEFT, RIGHT, UP, DOWN, UNDOWN;
    }

    public static float getFloat(String v) {
        if (v == null || TextUtils.isEmpty(v)) return 0;
        return Float.parseFloat(v.split("%")[0]) * 0.01f;
    }

    public static String getString(String v) {
        if (v == null) return "";
        return v;
    }

    public static CustomAttrs initCustomAttrs(Context context, AttributeSet attrs, View v) {
        CustomAttrs mAttrs = new CustomAttrs();
        if (v.isInEditMode()) return mAttrs;
        TypedArray ta, ta2;
        ta = context.obtainStyledAttributes(attrs, R.styleable.CustomAttrs);
        mAttrs.setScreenDesignWidth(ta.getInt(R.styleable.CButton_cdesignScreenWidth, 0));
        mAttrs.setScreenDesignHeight(ta.getInt(R.styleable.CButton_cdesignScreenHeight, 0));
        mAttrs.setWidthRatio(ta.getString(R.styleable.CustomAttrs_cwidth));
//        mAttrs.setWidthPx(ta.getInt(R.styleable.CustomAttrs_cwidthPx, 0));
        mAttrs.setHeightRatio(ta.getString(R.styleable.CustomAttrs_cheight));
//        mAttrs.setHeightPx(ta.getInt(R.styleable.CustomAttrs_cheightPx, 0));
        mAttrs.setMarginLeftRatio(ta.getString(R.styleable.CustomAttrs_cmarginLeft));
        mAttrs.setMarginRightRatio(ta.getString(R.styleable.CustomAttrs_cmarginRight));
        mAttrs.setMarginTopRatio(ta.getString(R.styleable.CustomAttrs_cmarginTop));
        mAttrs.setMarginBottomRatio(ta.getString(R.styleable.CustomAttrs_cmarginBottom));
        ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_margin});
        int margin = ta2.getDimensionPixelOffset(0, 0);
        ta2.recycle();
        if (margin == 0) {
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_marginLeft});
            mAttrs.setMarginLeftPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_marginRight});
            mAttrs.setMarginRightPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_marginTop});
            mAttrs.setMarginTopPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.layout_marginBottom});
            mAttrs.setMarginBottomPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
        } else {
            mAttrs.setMarginLeftPx(margin);
            mAttrs.setMarginRightPx(margin);
            mAttrs.setMarginTopPx(margin);
            mAttrs.setMarginBottomPx(margin);
        }
        mAttrs.setPaddingLeftRatio(ta.getString(R.styleable.CustomAttrs_cpaddingLeft));
        mAttrs.setPaddingRightRatio(ta.getString(R.styleable.CustomAttrs_cpaddingRight));
        mAttrs.setPaddingTopRatio(ta.getString(R.styleable.CustomAttrs_cpaddingTop));
        mAttrs.setPaddingBottomRatio(ta.getString(R.styleable.CustomAttrs_cpaddingBottom));
        ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.padding});
        int padding = ta2.getDimensionPixelOffset(0, 0);
        ta2.recycle();
        if (padding > 0) {
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.paddingLeft});
            mAttrs.setPaddingLeftPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.paddingRight});
            mAttrs.setPaddingRightPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.paddingTop});
            mAttrs.setPaddingTopPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
            ta2 = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.paddingBottom});
            mAttrs.setPaddingBottomPx(ta2.getDimensionPixelOffset(0, 0));
            ta2.recycle();
        } else {
            mAttrs.setPaddingLeftPx(padding);
            mAttrs.setPaddingRightPx(padding);
            mAttrs.setPaddingTopPx(padding);
            mAttrs.setPaddingBottomPx(padding);
        }
        mAttrs.setMinWidthRatio(ta.getString(R.styleable.CustomAttrs_cminWidth));
        mAttrs.setMaxWidthRatio(ta.getString(R.styleable.CustomAttrs_cmaxWidth));
        mAttrs.setMaxWidthPx(ta.getInt(R.styleable.CustomAttrs_cmaxWidthPx, 0));
        mAttrs.setMinHeightRatio(ta.getString(R.styleable.CustomAttrs_cminHeight));
        mAttrs.setHeightByWidthRatio(ta.getString(R.styleable.CustomAttrs_cheightByWidth));
        mAttrs.setWidthByHeightRatio(ta.getString(R.styleable.CustomAttrs_cwidthByHeight));
        mAttrs.setMarginByWidthRatio(ta.getString(R.styleable.CustomAttrs_cmarginByWidth));
        mAttrs.setMarginByHeightRatio(ta.getString(R.styleable.CustomAttrs_cmarginByHeight));
        mAttrs.setMarginTopByWidthRatio(ta.getString(R.styleable.CustomAttrs_cmarginTopByWidth));
        mAttrs.setMarginBottomByWidthRatio(ta.getString(R.styleable.CustomAttrs_cmarginBottomByWidth));
        mAttrs.setPaddingTopByWidthRatio(ta.getString(R.styleable.CustomAttrs_cpaddingTopByWidth));
        mAttrs.setPaddingBottomByWidthRatio(ta.getString(R.styleable.CustomAttrs_cpaddingBottomByWidth));
        mAttrs.setCornerRatio(ta.getString(R.styleable.CustomAttrs_ccorner)); //  需要在设置height之后设置
        mAttrs.setCornerDirection(ta.getInt(R.styleable.CustomAttrs_ccornerDirection, 0xf));
        mAttrs.toSquare(ta.getBoolean(R.styleable.CustomAttrs_ctoSquare, false));
        mAttrs.setOnClickBackground(ta.getDrawable(R.styleable.CustomAttrs_onClickBackground));
        mAttrs.setEntityMapping(ta.getString(R.styleable.CustomAttrs_entityMapping));
        mAttrs.setGetMapping(ta.getString(R.styleable.CustomAttrs_getMapping));
        mAttrs.setSetMapping(ta.getString(R.styleable.CustomAttrs_setMapping));
        mAttrs.setSelectMapping(ta.getString(R.styleable.CustomAttrs_selectMapping));
        mAttrs.setVisibleMapping(ta.getString(R.styleable.CustomAttrs_visibleMapping));
        mAttrs.setHideMode(ta.getInt(R.styleable.CustomAttrs_hideMode, -1));
        ta.recycle();
        return mAttrs;
    }

    public static void getParentScreenAttr(CustomAttrs mAttrs, View v) {
        if (v.isInEditMode() || v.getId() == R.id.root || !(v instanceof IView.ICustomAttrs))
            return;
        if (mAttrs.getScreenDesignWidth() == 0)
            mAttrs.setScreenDesignWidth(getParentScreenWidth(v));
        if (mAttrs.getScreenDesignHeight() == 0)
            mAttrs.setScreenDesignHeight(getParentScreenHeight(v));
        if (mAttrs.getScreenDesignWidth() == 0 || mAttrs.getScreenDesignHeight() == 0) return;
        if (mAttrs.getWidthRatio() == 0)
            mAttrs.setWidthPxRatio(v.getLayoutParams() == null ? 0 : v.getLayoutParams().width);
        if (mAttrs.getHeightRatio() == 0)
            mAttrs.setHeightPxRatio(v.getLayoutParams() == null ? 0 : v.getLayoutParams().height);
        if (mAttrs.getMarginLeftRatio() == 0)
            mAttrs.setMarginLeftPxRatio(mAttrs.getMarginLeftPx());
        if (mAttrs.getMarginRightRatio() == 0)
            mAttrs.setMarginRightPxRatio(mAttrs.getMarginRightPx());
        if (mAttrs.getMarginTopRatio() == 0)
            mAttrs.setMarginTopPxRatio(mAttrs.getMarginTopPx());
        if (mAttrs.getMarginBottomRatio() == 0)
            mAttrs.setMarginBottomPxRatio(mAttrs.getMarginBottomPx());
        if (mAttrs.getPaddingLeftRatio() == 0)
            mAttrs.setPaddingLeftPxRatio(mAttrs.getPaddingLeftPx());
        if (mAttrs.getPaddingRightRatio() == 0)
            mAttrs.setPaddingRightPxRatio(mAttrs.getPaddingRightPx());
        if (mAttrs.getPaddingTopRatio() == 0)
            mAttrs.setPaddingTopPxRatio(mAttrs.getPaddingTopPx());
        if (mAttrs.getPaddingBottomRatio() == 0)
            mAttrs.setPaddingBottomPxRatio(mAttrs.getPaddingBottomPx());
        if (mAttrs.getMaxWidthRatio() == 0)
            mAttrs.setMaxWidthPxRatio(mAttrs.getMaxWidthPx());
        if (mAttrs.getTextSizePx() > 0)
            mAttrs.setTextSizePxRatio(mAttrs.getTextSizePx());
    }

    public static int getParentScreenWidth(View v) {
        if (v.getParent() == null || v.getParent().getClass() == null ||
                v.getParent().getClass().getName() == null ||
                v.getParent().getClass().getName().equals("android.view.ViewRootImpl")) return 0;
        if (!(v.getParent() instanceof IView.ICustomAttrs))
            return getParentScreenWidth((View) v.getParent());
        IView.ICustomAttrs attrs = (IView.ICustomAttrs) v.getParent();
        if (attrs.getCustomAttrs().getScreenDesignWidth() == 0)
            attrs.getCustomAttrs().setScreenDesignWidth(getParentScreenWidth((View) v.getParent()));
        return attrs.getCustomAttrs().getScreenDesignWidth();
    }

    public static int getParentScreenHeight(View v) {
        if (v.getParent() == null || v.getParent().getClass() == null ||
                v.getParent().getClass().getName() == null ||
                v.getParent().getClass().getName().equals("android.view.ViewRootImpl")) return 0;
        if (!(v.getParent() instanceof IView.ICustomAttrs))
            return getParentScreenHeight((View) v.getParent());
        IView.ICustomAttrs attrs = (IView.ICustomAttrs) v.getParent();
        if (attrs.getCustomAttrs().getScreenDesignHeight() == 0)
            attrs.getCustomAttrs().setScreenDesignHeight(getParentScreenHeight((View) v.getParent()));
        return attrs.getCustomAttrs().getScreenDesignHeight();
    }

    private static void loadCommonAttrs(View v, CustomAttrs attrs) {
        int paddingLeft = v.getPaddingLeft();
        int paddingRight = v.getPaddingRight();
        int paddingTop = v.getPaddingTop();
        int paddingBottom = v.getPaddingBottom();
        if (attrs.getPaddingLeft() != 0)
            paddingLeft = attrs.getPaddingLeft();
        if (attrs.getPaddingRight() != 0)
            paddingRight = attrs.getPaddingRight();
        if (attrs.getPaddingTopByWidth() != 0)
            paddingTop = attrs.getPaddingTopByWidth();
        if (attrs.getPaddingTop() != 0)
            paddingTop = attrs.getPaddingTop();
        if (attrs.getPaddingBottomByWidth() != 0)
            paddingBottom = attrs.getPaddingBottomByWidth();
        if (attrs.getPaddingBottom() != 0)
            paddingBottom = attrs.getPaddingBottom();
        v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        if (attrs.getMinWidth() > 0)
            v.setMinimumWidth(attrs.getMinWidth());
        if (attrs.getMinHeight() > 0)
            v.setMinimumHeight(attrs.getMinHeight());
        if (attrs.getCornerRatio() > 0)
            setCorner(v, attrs);
        if (attrs.getTextSize() > 0)
            setTextSize(v, attrs);
//        else setTextSizeDefault(v);
    }

    public static void loadCustomAttrs(View v, CustomAttrs attrs) {
        if (v == null) return;
        if (v.getLayoutParams() instanceof LinearLayout.LayoutParams)
            loadLinearLayoutAttrs(v, attrs);
        if (v.getLayoutParams() instanceof RelativeLayout.LayoutParams)
            loadRelativeLayoutAttrs(v, attrs);
        if (v.getLayoutParams() instanceof FrameLayout.LayoutParams)
            loadFrameLayoutAttrs(v, attrs);
    }

    public static void loadCustomAttrs(View view) {
        if (view == null) return;
        if (view instanceof IView.ICustomAttrs)
            ((IView.ICustomAttrs) view).loadCustomAttrs();
    }

    public static void loadSubViewCustomAttrs(View view) {
        if (view == null) return;
        if (view instanceof IView.ICustomAttrs) {
            getParentScreenAttr(((IView.ICustomAttrs) view).getCustomAttrs(), view);
            loadCustomAttrs(view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, l = viewGroup.getChildCount(); i < l; i++) {
                loadSubViewCustomAttrs(viewGroup.getChildAt(i));
            }
        }
    }

    private static void loadFrameLayoutAttrs(View v, CustomAttrs attrs) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
        if (attrs.getWidth() > 0)
            lp.width = attrs.getWidth();
        if (attrs.getHeight() > 0)
            lp.height = attrs.getHeight();
        if (attrs.getWidthByHeight() > 0) {
            lp.width = attrs.getWidthByHeight();
            attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
        }
        if (attrs.getHeightByWidth() > 0) {
            lp.height = attrs.getHeightByWidth();
            attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
        }
        if (attrs.isToSquare()) {
            if (lp.width >= lp.height) {
                lp.height = lp.width;
                attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
            } else {
                lp.width = lp.height;
                attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
            }
        }
        if (attrs.getMarginByWidth() != 0)
            lp.setMargins(attrs.getMarginByWidth(), attrs.getMarginByWidth(),
                    attrs.getMarginByWidth(), attrs.getMarginByWidth());
        if (attrs.getMarginByHeight() != 0)
            lp.setMargins(attrs.getMarginByHeight(), attrs.getMarginByHeight(),
                    attrs.getMarginByHeight(), attrs.getMarginByHeight());
        if (attrs.getMarginLeft() != 0)
            lp.leftMargin = attrs.getMarginLeft();
        if (attrs.getMarginRight() != 0)
            lp.rightMargin = attrs.getMarginRight();
        if (attrs.getMarginTopByWidth() != 0)
            lp.topMargin = attrs.getMarginTopByWidth();
        if (attrs.getMarginTop() != 0)
            lp.topMargin = attrs.getMarginTop();
        if (attrs.getMarginBottomByWidth() != 0)
            lp.bottomMargin = attrs.getMarginBottomByWidth();
        if (attrs.getMarginBottom() != 0) {
            lp.bottomMargin = attrs.getMarginBottom();
        }
        v.setLayoutParams(lp);
        loadCommonAttrs(v, attrs);
    }

    private static void loadRelativeLayoutAttrs(View v, CustomAttrs attrs) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        if (attrs.getWidth() > 0)
            lp.width = attrs.getWidth();
        if (attrs.getHeight() > 0)
            lp.height = attrs.getHeight();
        if (attrs.getWidthByHeight() > 0) {
            lp.width = attrs.getWidthByHeight();
            attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
        }
        if (attrs.getHeightByWidth() > 0) {
            lp.height = attrs.getHeightByWidth();
            attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
        }
        if (attrs.isToSquare()) {
            if (lp.width >= lp.height) {
                lp.height = lp.width;
                attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
            } else {
                lp.width = lp.height;
                attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
            }
        }
        if (attrs.getMarginByWidth() != 0)
            lp.setMargins(attrs.getMarginByWidth(), attrs.getMarginByWidth(),
                    attrs.getMarginByWidth(), attrs.getMarginByWidth());
        if (attrs.getMarginByHeight() != 0)
            lp.setMargins(attrs.getMarginByHeight(), attrs.getMarginByHeight(),
                    attrs.getMarginByHeight(), attrs.getMarginByHeight());
        if (attrs.getMarginLeft() != 0)
            lp.leftMargin = attrs.getMarginLeft();
        if (attrs.getMarginRight() != 0)
            lp.rightMargin = attrs.getMarginRight();
        if (attrs.getMarginTopByWidth() != 0)
            lp.topMargin = attrs.getMarginTopByWidth();
        if (attrs.getMarginTop() != 0)
            lp.topMargin = attrs.getMarginTop();
        if (attrs.getMarginBottomByWidth() != 0)
            lp.bottomMargin = attrs.getMarginBottomByWidth();
        if (attrs.getMarginBottom() != 0)
            lp.bottomMargin = attrs.getMarginBottom();
        v.setLayoutParams(lp);
        loadCommonAttrs(v, attrs);
    }

    private static void loadLinearLayoutAttrs(View v, CustomAttrs attrs) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
        if (attrs.getWidth() > 0)
            lp.width = attrs.getWidth();
        if (attrs.getHeight() > 0)
            lp.height = attrs.getHeight();
        if (attrs.getWidthByHeight() > 0) {
            lp.width = attrs.getWidthByHeight();
            attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
        }
        if (attrs.getHeightByWidth() > 0) {
            lp.height = attrs.getHeightByWidth();
            attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
        }
        if (attrs.isToSquare()) {
            if (lp.width >= lp.height) {
                lp.height = lp.width;
                attrs.setHeightRatio(lp.height * 100f / CustomAttrs.getScreenHeight() + "%");
            } else {
                lp.width = lp.height;
                attrs.setWidthRatio(lp.width * 100f / CustomAttrs.getScreenWidth() + "%");
            }
        }
        if (attrs.getMarginByWidth() != 0)
            lp.setMargins(attrs.getMarginByWidth(), attrs.getMarginByWidth(),
                    attrs.getMarginByWidth(), attrs.getMarginByWidth());
        if (attrs.getMarginByHeight() != 0)
            lp.setMargins(attrs.getMarginByHeight(), attrs.getMarginByHeight(),
                    attrs.getMarginByHeight(), attrs.getMarginByHeight());
        if (attrs.getMarginLeft() != 0)
            lp.leftMargin = attrs.getMarginLeft();
        if (attrs.getMarginRight() != 0)
            lp.rightMargin = attrs.getMarginRight();
        if (attrs.getMarginTopByWidth() != 0)
            lp.topMargin = attrs.getMarginTopByWidth();
        if (attrs.getMarginTop() != 0)
            lp.topMargin = attrs.getMarginTop();
        if (attrs.getMarginBottomByWidth() != 0)
            lp.bottomMargin = attrs.getMarginBottomByWidth();
        if (attrs.getMarginBottom() != 0)
            lp.bottomMargin = attrs.getMarginBottom();
        v.setLayoutParams(lp);
        loadCommonAttrs(v, attrs);
    }

    public static Drawable[] loadDrawable(Resources resources, CustomAttrs attrs) {
        Drawable drawableLeft = null, drawableRight = null, drawableTop = null, drawableBottom = null;
        int width, height;
        if (attrs.getDrawableLeftResId() != 0) {
            drawableLeft = DrawableRevise.getDrawable(resources, attrs.getDrawableLeftResId(), null);
            width = attrs.getDrawableLeftWidth();
            height = attrs.getDrawableLeftHeight();
            height = height == 0 ? width : height;
            drawableLeft.setBounds(0, 0, width, height);
        }
        if (attrs.getDrawableRightResId() != 0) {
            drawableRight = DrawableRevise.getDrawable(resources, attrs.getDrawableRightResId(), null);
            width = attrs.getDrawableRightWidth();
            height = attrs.getDrawableRightHeight();
            height = height == 0 ? width : height;
            drawableRight.setBounds(0, 0, width, height);
        }
        if (attrs.getDrawableTopResId() != 0) {
            drawableTop = DrawableRevise.getDrawable(resources, attrs.getDrawableTopResId(), null);
            width = attrs.getDrawableTopWidth();
            height = attrs.getDrawableTopHeight();
            height = height == 0 ? width : height;
            drawableTop.setBounds(0, 0, width, height);
        }
        if (attrs.getDrawableBottomResId() != 0) {
            drawableBottom = DrawableRevise.getDrawable(resources, attrs.getDrawableBottomResId(), null);
            width = attrs.getDrawableBottomWidth();
            height = attrs.getDrawableBottomHeight();
            height = height == 0 ? width : height;
            drawableBottom.setBounds(0, 0, width, height);
        }
        return new Drawable[]{drawableLeft, drawableTop, drawableRight, drawableBottom};
    }

    // 根据控件属性将控件的背景转换成带圆角的
    private static void setCorner(View v, CustomAttrs attrs) {
        float length = attrs.getHeight();
        if (length <= 0) length = attrs.getWidth();
        if (length <= 0) length = CustomAttrs.getScreenWidth();
        float[] radii = getCornerRadii(attrs.getCornerRatio() * length * 0.5f, attrs.getDirection());
        Drawable drawable = v.getBackground();// 获取背景颜色
        drawable = getCornerDrawable(drawable, radii);
        Drawable onClickBackground = attrs.getOnClickBackground();
        if (onClickBackground != null) {
            onClickBackground = getCornerDrawable(onClickBackground, radii);
            StateListDrawable sld = new StateListDrawable();
            sld.addState(new int[]{android.R.attr.state_pressed}, onClickBackground);
            sld.addState(new int[]{android.R.attr.state_selected}, onClickBackground);
            sld.addState(new int[]{}, drawable);
            drawable = sld;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    // 只支持ColorDrawable、GradientDrawable、StateListDrawable三种类型
    private static Drawable getCornerDrawable(Drawable drawable, float[] radii) {
        if (radii != null && radii.length >= 8 && drawable != null) {
            if (drawable instanceof ColorDrawable) {
                GradientDrawable gd = new GradientDrawable();
                gd.setCornerRadii(radii);
                gd.setColor(((ColorDrawable) drawable).getColor());
                return gd;
            } else if (drawable instanceof GradientDrawable) {
                ((GradientDrawable) drawable).setCornerRadii(radii);  // 同个页面不能使用同个资源文件
                return drawable;
            } else if (drawable instanceof StateListDrawable) {
                try {
                    Class<StateListDrawable> clazz = StateListDrawable.class;
                    Method countMethod = clazz.getMethod("getStateCount");
                    countMethod.setAccessible(true);
                    int count = (int) countMethod.invoke(drawable);
                    Method drawableMethod = clazz.getMethod("getStateDrawable", int.class);
                    drawableMethod.setAccessible(true);
                    Method setMethod = clazz.getMethod("getStateSet", int.class);
                    setMethod.setAccessible(true);
                    StateListDrawable sld = new StateListDrawable();
                    for (int i = 0; i < count; i++) {
                        Drawable stateDrawable = (Drawable) drawableMethod.invoke(drawable, i);
                        int[] state = (int[]) setMethod.invoke(drawable, i);
                        sld.addState(state, getCornerDrawable(stateDrawable, radii));
                    }
                    return sld;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }


    private static float[] getCornerRadii(float radius, int direction) {
        float[] radii = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
        if (radius <= 0) {
            return null;
        }
        if (direction < 0 || direction > 15) {
            direction = 15;
        }
        if ((direction & 0x8) > 0) {
            radii[0] = radius;
            radii[1] = radius;
        }
        if ((direction & 0x4) > 0) {
            radii[2] = radius;
            radii[3] = radius;
        }
        if ((direction & 0x2) > 0) {
            radii[4] = radius;
            radii[5] = radius;
        }
        if ((direction & 0x1) > 0) {
            radii[6] = radius;
            radii[7] = radius;
        }
        return radii;
    }

    private static void setTextSize(View v, CustomAttrs attrs) {
        if (v instanceof CTextView) {
            ((CTextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, attrs.getTextSize());
        } else if (v instanceof CButton) {
            ((CButton) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, attrs.getTextSize());
        } else if (v instanceof CEditText) {
            ((CEditText) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, attrs.getTextSize());
        }
    }

    public static void setTextSizeDefault(View v) {
        if (v instanceof CTextView) {
            ((CTextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_TEXT_SIZE);
        } else if (v instanceof CButton) {
            ((CButton) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_TEXT_SIZE);
        } else if (v instanceof CEditText) {
            ((CEditText) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, DEFAULT_TEXT_SIZE);
        }
    }

    public static void setVisible(View view, int v) {
        switch (v) {
            case View.VISIBLE:
                view.setVisibility(View.VISIBLE);
                break;
            case View.GONE:
                view.setVisibility(View.GONE);
                break;
            case View.INVISIBLE:
                view.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public static SparseArray<View> getViewMapEntityList(View view, Class entityClazz) {
        HashMap<String, Method> methodMap = new HashMap<>(50, 25);
        Method[] methods = entityClazz.getMethods();
        for (Method m : methods) {
            methodMap.put(m.getName(), m);
        }
        SparseArray<View> viewMapEntityList = new SparseArray<>();
        getViewMapEntity((ViewGroup) view, entityClazz, methodMap, viewMapEntityList);
        return viewMapEntityList;
    }

    private static void getViewMapEntity(ViewGroup view, Class entityClazz, HashMap<String, Method> methodMap, SparseArray<View> viewMapEntityList) {
        View v;
        IView.ICustomAttrs iCustomAttrs;
        boolean add;
        for (int i = 0, l = view.getChildCount(); i < l; i++) {
            v = view.getChildAt(i);
            if (v instanceof IView.ICustomAttrs) {
                iCustomAttrs = ((IView.ICustomAttrs) v);
                if (iCustomAttrs.getCustomAttrs().getEntityMapping().equals(entityClazz.getName())) {
                    add = methodMap.containsKey(iCustomAttrs.getCustomAttrs().getGetMapping())
                            || methodMap.containsKey(iCustomAttrs.getCustomAttrs().getSetMapping())
                            || !TextUtils.isEmpty(iCustomAttrs.getCustomAttrs().getVisibleMapping())
                            || methodMap.containsKey(iCustomAttrs.getCustomAttrs().getSelectMapping());
                    if (add) {
                        viewMapEntityList.append(v.hashCode(), v);
                    }
                }
            }
            if (v instanceof ViewGroup) {
                getViewMapEntity((ViewGroup) v, entityClazz, methodMap, viewMapEntityList);
            }
        }
    }

    //获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
//        else {
//                    Rect rect = new Rect();
//        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//        return rect.top;
//        }
    }
}
