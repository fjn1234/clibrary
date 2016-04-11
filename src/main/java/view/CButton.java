package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.hugh.clibrary.R;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ImageUtil;
import utils.ResourceUtil;
import utils.ViewUtil;

public class CButton extends Button implements IView.ICustomAttrs, IView.IMapping {

    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;
    private int selectOnTextColor, selectOffTextColor, enableTextColor, disableTextColor;
    private String selectOnText, selectOffText, enableText, disableText;
    private int defualt = Integer.MAX_VALUE;

    public CButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttr(context, attrs);
    }

    public CButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CButton(Context context) {
        super(context);
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CButton);
        selectOnTextColor = ta.getColor(R.styleable.CButton_cselectOnTextColor, defualt);
        selectOffTextColor = ta.getColor(R.styleable.CButton_cselectOffTextColor, defualt);
        selectOnText = ta.getString(R.styleable.CButton_cselectOnText);
        selectOffText = ta.getString(R.styleable.CButton_cselectOffText);
        enableTextColor = ta.getColor(R.styleable.CButton_cenableTextColor, defualt);
        disableTextColor = ta.getColor(R.styleable.CButton_cdisableTextColor, defualt);
        enableText = ta.getString(R.styleable.CButton_cenableText);
        disableText = ta.getString(R.styleable.CButton_cdisableText);
        mAttrs.setDrawableLeftResId(ta.getResourceId(R.styleable.CButton_cdrawableLeft, 0));
        mAttrs.setDrawableRightResId(ta.getResourceId(R.styleable.CButton_cdrawableRight, 0));
        mAttrs.setDrawableTopResId(ta.getResourceId(R.styleable.CButton_cdrawableTop, 0));
        mAttrs.setDrawableBottomResId(ta.getResourceId(R.styleable.CButton_cdrawableBottom, 0));
        mAttrs.setDrawableLeftWidthRatio(ta.getString(R.styleable.CButton_cdrawableLeftWidth));
        mAttrs.setDrawableLeftHeightRatio(ta.getString(R.styleable.CButton_cdrawableLeftHeight));
        mAttrs.setDrawableRightWidthRatio(ta.getString(R.styleable.CButton_cdrawableRightWidth));
        mAttrs.setDrawableRightHeightRatio(ta.getString(R.styleable.CButton_cdrawableRightHeight));
        mAttrs.setDrawableTopWidthRatio(ta.getString(R.styleable.CButton_cdrawableTopWidth));
        mAttrs.setDrawableTopHeightRatio(ta.getString(R.styleable.CButton_cdrawableTopHeight));
        mAttrs.setDrawableBottomWidthRatio(ta.getString(R.styleable.CButton_cdrawableBottomWidth));
        mAttrs.setDrawableBottomHeightRatio(ta.getString(R.styleable.CButton_cdrawableBottomHeight));
        mAttrs.setDrawablePaddingRatio(ta.getString(R.styleable.CButton_cdrawablePadding));
        ta.recycle();
    }

    public void loadCustomAttrs() {
        ViewUtil.loadCustomAttrs(this, mAttrs);
    }

    public void loadDrawable() {
        Drawable[] drawables = ViewUtil.loadDrawable(getResources(), mAttrs);
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        setCompoundDrawablePadding(100);
        setCompoundDrawablePadding(mAttrs.getDrawablePadding());
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
            ViewUtil.getParentScreenAttr(mAttrs, this);
            loadCustomAttrs();
            loadDrawable();
        }
    }

    @Override
    public void setMappingValue(String v) {
        setText(v);
    }

    @Override
    public String getMappingValue() {
        return getText().toString();
    }

    public void setTextColorResource(int resId) {
        setTextColor(ResourceUtil.getColor(getContext(), resId));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isSelected()) {
            if (selectOnTextColor != defualt)
                setTextColor(selectOnTextColor);
            if (selectOnText != null)
                setText(selectOnText);
        } else {
            if (selectOnTextColor != defualt)
                setTextColor(selectOffTextColor);
            if (selectOffText != null)
                setText(selectOffText);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isEnabled()) {
            if (enableTextColor != defualt)
                setTextColor(enableTextColor);
            if (enableText != null)
                setText(enableText);
        } else {
            if (disableTextColor != defualt)
                setTextColor(disableTextColor);
            if (disableText != null)
                setText(disableText);
        }
    }

}
