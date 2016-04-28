package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;


public class CTableLayout extends TableLayout implements IView.ICustomAttrs {
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs=true;

    public CTableLayout(Context context) {
        super(context);
    }

    public CTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }


    private void setCustomAttr(Context context, AttributeSet attrs) {
        mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
    }

    public void loadCustomAttrs() {
        ViewUtil.loadCustomAttrs(this, mAttrs);
    }

    public void loadScreenArr(){
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
