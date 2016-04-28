package view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

/**
 * Created by Hugh on 2016/4/22.
 */
public class CView extends View implements IView.ICustomAttrs {

    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CView(Context context) {
        super(context);
    }

    public CView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
