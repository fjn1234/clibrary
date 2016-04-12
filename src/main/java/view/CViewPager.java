package view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

public class CViewPager extends LazyViewPager implements IView.ICustomAttrs {

    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CViewPager(Context context) {
        super(context);
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);

    }

    public void loadCustomAttrs() {
        ViewUtil.loadCustomAttrs(this, mAttrs);
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
        }
    }
}
