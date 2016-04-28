package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

public class CScrollView extends ScrollView implements IView.ICustomAttrs {
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CScrollView(Context context) {
        super(context);
    }

    public CScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttr(context, attrs);
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
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

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onCScrollChangedListener != null)
            onCScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnCScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private OnCScrollChangedListener onCScrollChangedListener;

    public void setOnCScrollChangedListener(OnCScrollChangedListener onCScrollChangedListener) {
        this.onCScrollChangedListener = onCScrollChangedListener;
    }
}
