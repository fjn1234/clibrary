package view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import obj.CustomAttrs;
import utils.ViewUtil;

/**
 * Created by Hugh on 2016/7/13.
 */

public class CWebView extends WebView {

    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CWebView(Context context) {
        super(context);
    }

    public CWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        setCustomAttr(context, attrs);
    }

    public CWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
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
}
