package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hugh.clibrary.R;

import compatbility.DrawableRevise;
import interfaces.IView;
import obj.CustomAttrs;
import utils.ResourceUtil;
import utils.ViewUtil;


public class CTextView extends TextView implements IView.ICustomAttrs, IView.IMapping {
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;
    private int selectOnTextColor = 0, selectOffTextColor = 0;
    private String selectOnText, selectOffText;

    public CTextView(Context context) {
        super(context);
    }

    public CTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }


    public CTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttr(context, attrs);
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CTextView);
        getPaint().setFakeBoldText(ta.getBoolean(R.styleable.CTextView_tv_fakeBlod, false));
        if (ta.getBoolean(R.styleable.CTextView_tv_flags, false))
            getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        getPaint().setAntiAlias(ta.getBoolean(R.styleable.CTextView_tv_antiAlias, true));//抗锯齿
        selectOnTextColor = ta.getColor(R.styleable.CTextView_tv_selectOnTextColor, -1);
        selectOffTextColor = ta.getColor(R.styleable.CTextView_tv_selectOffTextColor, -1);
        selectOnText = ta.getString(R.styleable.CTextView_tv_selectOnText);
        selectOffText = ta.getString(R.styleable.CTextView_tv_selectOffText);
        mAttrs.setDrawableLeftResId(ta.getResourceId(R.styleable.CTextView_cdrawableLeft, 0));
        mAttrs.setDrawableRightResId(ta.getResourceId(R.styleable.CTextView_cdrawableRight, 0));
        mAttrs.setDrawableTopResId(ta.getResourceId(R.styleable.CTextView_cdrawableTop, 0));
        mAttrs.setDrawableBottomResId(ta.getResourceId(R.styleable.CTextView_cdrawableBottom, 0));
        mAttrs.setDrawableLeftWidthRatio(ta.getString(R.styleable.CTextView_cdrawableLeftWidth));
        mAttrs.setDrawableLeftHeightRatio(ta.getString(R.styleable.CTextView_cdrawableLeftHeight));
        mAttrs.setDrawableRightWidthRatio(ta.getString(R.styleable.CTextView_cdrawableRightWidth));
        mAttrs.setDrawableRightHeightRatio(ta.getString(R.styleable.CTextView_cdrawableRightHeight));
        mAttrs.setDrawableTopWidthRatio(ta.getString(R.styleable.CTextView_cdrawableTopWidth));
        mAttrs.setDrawableTopHeightRatio(ta.getString(R.styleable.CTextView_cdrawableTopHeight));
        mAttrs.setDrawableBottomWidthRatio(ta.getString(R.styleable.CTextView_cdrawableBottomWidth));
        mAttrs.setDrawableBottomHeightRatio(ta.getString(R.styleable.CTextView_cdrawableBottomHeight));
        mAttrs.setDrawablePaddingRatio(ta.getString(R.styleable.CTextView_cdrawablePadding));
        ta.recycle();
    }

    public void loadCustomAttrs() {
        ViewUtil.loadCustomAttrs(this, mAttrs);
    }

    public void loadDrawable() {
        Drawable[] drawables = ViewUtil.loadDrawable(getResources(), mAttrs);
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
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

    public void setHtmlText(String text) {
        setText(Html.fromHtml(text));
    }

    public void setHtmlText(int resId) {
        setHtmlText(getContext().getString(resId));
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

    public void setSelectOffTextColor(int selectOffTextColor) {
        this.selectOffTextColor = selectOffTextColor;
    }

    public void setSelectOnTextColor(int selectOnTextColor) {
        this.selectOnTextColor = selectOnTextColor;
    }

    public void setSelectOffText(String selectOffText) {
        this.selectOffText = selectOffText;
    }

    public void setSelectOnText(String selectOnText) {
        this.selectOnText = selectOnText;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isSelected()) {
            if (selectOnTextColor != -1)
                setTextColor(selectOnTextColor);
            if (selectOnText != null)
                setText(selectOnText);
        } else {
            if (selectOffTextColor != -1)
                setTextColor(selectOffTextColor);
            if (selectOffText != null)
                setText(selectOffText);
        }
    }

    public int getWidthAtSingleLine() {
        final int paddingLeft = getCustomAttrs().getPaddingLeft();
        final int paddingRight = getCustomAttrs().getPaddingRight();
        final int textSize = getCustomAttrs().getTextSize();
        int width = getText().length() * textSize + paddingLeft + paddingRight;
        return width;
    }
}
