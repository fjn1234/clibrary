package view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

public class CRecyclerView extends RecyclerView implements IView.ICustomAttrs {

    private CustomAttrs mAttrs;
    private boolean initCustomAttrs = true;
    private boolean isScrolling = false;
    private float fastVelocityY = 0f;

    public CRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttr(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        addOnScrollListener(scrollListener);
    }

    public CRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
        setLayoutManager(new LinearLayoutManager(context));
        addOnScrollListener(scrollListener);
    }

    public CRecyclerView(Context context) {
        super(context);
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

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setFastVelocityY(float fastVelocityY) {
        this.fastVelocityY = fastVelocityY;
    }

    private ScrollActionListener scrollActionListener;

    public interface ScrollActionListener {
        void onStopScroll(int first, int last);
    }

    public void setScrollActionListener(ScrollActionListener scrollActionListener) {
        this.scrollActionListener = scrollActionListener;
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE: //Idle态，进行实际数据的加载显示
                    if (!isScrolling) return;
                    isScrolling = false;
                    int first = -1, last = -1;
                    if (getLayoutManager() instanceof LinearLayoutManager) {
                        first = ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                        last = ((LinearLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    }
                    if (first > -1 && last > -1)
                        scrollActionListener.onStopScroll(first, last);
                    break;
//            case RecyclerView.SCROLL_STATE_DRAGGING:
//                isScrolling = true;
//                break;
                default:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        detector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    private GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (fastVelocityY == 0) return false;
            if (Math.abs(velocityY) < fastVelocityY)
                isScrolling = false;
            else
                isScrolling = true;
            return false;
        }
    });

}
