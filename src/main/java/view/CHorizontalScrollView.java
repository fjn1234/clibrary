package view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

/**
 * Created by Hugh on 2015/5/19.
 */
public class CHorizontalScrollView extends HorizontalScrollView implements IView.ICustomAttrs {
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CHorizontalScrollView(Context context) {
        super(context);
    }

    public CHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
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
        onScrollChangedListener.onScrollChanged(l,t,oldl,oldt);
    }

    private OnScrollChangedListener onScrollChangedListener=new OnScrollChangedListener() {
        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {

        }
    };

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    public interface OnScrollChangedListener{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    //--------------------------------------------------------------------
//
//    private InterceptTouchListener interceptTouchListener;
//
//    public void setInterceptTouchListener(InterceptTouchListener interceptTouchListener) {
//        this.interceptTouchListener = interceptTouchListener;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (interceptTouchListener != null) {
//            interceptTouchListener.onInterceptTouch(ev);
//            return false;
//        } else
//            return super.onInterceptTouchEvent(ev);
//    }
//
//    public interface InterceptTouchListener {
//        void onInterceptTouch(MotionEvent event);
//    }
}
