package view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.hugh.clibrary.R;

import interfaces.IView;
import obj.CustomAttrs;
import utils.ViewUtil;

public class CListView extends ListView implements IView.ICustomAttrs {
    private CustomAttrs mAttrs = new CustomAttrs();
    private View mVwHeader, mVwFooter;
    private int scrollSpace, headerPadding, headerHeight;
    private float initY;
    private Status status = Status.Normal;
    private Mode mode = Mode.None;
    private StartPullRefreshListener startPullRefreshListener = null;
    private EndPullRefreshListener endPullRefreshListener = null;
    private OnClickEmptyViewListener onClickEmptyViewListener = null;
    private PullProgressListener pullProgressListener = null;
    private OnLoadingListener onLoadingListener = null;
    private boolean initCustomAttrs = true;
    private boolean isScrolling = false;
    private float fastVelocityY = 0f;

    enum Status {
        Normal, Ready, Loading, Finish
    }

    enum Mode {
        Start, End, None
    }

    public CListView(Context context) {
        this(context, null);
    }

    public CListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        if (attrs != null)
            setCustomAttr(context, attrs);
        init();
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
        if (isInEditMode()) return;
        if (!initCustomAttrs) return;
        initCustomAttrs = false;
        loadCustomAttrs();
    }

    private void init() {
        mVwHeader = initDefualtHeader();
        setFooterDividersEnabled(false);
        setHeaderDividersEnabled(false);
        setDefualtHeader();
        setRefreshFoooter(initDefaultFooter());
        setOnScrollListener(onScrollListener);
    }

    private void setDefualtHeader() {
//		measureView(mVwHeader);
        headerPadding = headerHeight * -1;
        mVwHeader.setPadding(0, headerPadding, 0, 0);
        scrollSpace = 5;
        addHeaderView(mVwHeader);
    }

    protected void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public View getHeaderView() {
        return mVwHeader;
    }

    protected View initDefualtHeader() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.listview_default_header, null);
        CTextView tv = (CTextView) view.findViewById(R.id.tv_list_header);
        tv.loadCustomAttrs();
        setHeaderHeight(tv.getCustomAttrs().getHeight());
        return view;
    }

    protected View initDefaultFooter() {
        View view = LayoutInflater.from(getContext()).inflate(
                android.R.layout.simple_spinner_item, null);
        TextView tvFooter = (TextView) view.findViewById(android.R.id.text1);
        tvFooter.setGravity(Gravity.CENTER);
        tvFooter.setText("loading more...");
        return view;
    }

    public void setRefreshHeader(View v) {
        if (mVwHeader != null)
            removeHeaderView(mVwHeader);
        mVwHeader = v;
//		measureView(mVwHeader);
//        ((CRelativeLayout) mVwHeader).loadCustomAttrs();
        headerHeight = ((CRelativeLayout) mVwHeader).getCustomAttrs().getHeight();
        headerPadding = headerHeight * -1;
        mVwHeader.setPadding(0, headerPadding, 0, 0);
        scrollSpace = 5;
        addHeaderView(mVwHeader);
    }

    public void setRefreshFoooter(View v) {
        if (v == null) return;
        if (mVwFooter != null)
            removeFooterView(mVwFooter);
        mVwFooter = v;
        mVwFooter.setVisibility(GONE);
        addFooterView(mVwFooter);
    }

    public void disableEndPulRefresh() {
        if (mVwFooter != null)
            removeFooterView(mVwFooter);
    }

    public void enableEndPulRefresh() {
        if (mVwFooter != null)
            addFooterView(mVwFooter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        if (ev.getPointerCount() > 1) {
            return true;
        }
        if (status == Status.Loading && mode == Mode.Start) {
            return true;
        }
        if (status == Status.Loading && mode == Mode.End) {
            return super.onTouchEvent(ev);
        }
        float offsetY;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY = ev.getY();
                handler.removeMessages(0);
                break;
            case MotionEvent.ACTION_MOVE:
                offsetY = ev.getY();
                float vailoffsetY = offsetY - initY;
                if (Math.abs(vailoffsetY) < 3)
                    return true;
                getParent().requestDisallowInterceptTouchEvent(true);
                if (offsetY > initY) {
                    initY = offsetY;
                    if (this.getFirstVisiblePosition() == 0) {
                        if (startPullRefreshListener == null)
                            return super.onTouchEvent(ev);
                        mode = Mode.Start;
                        status = Status.Ready;
                        if (mVwHeader.getPaddingTop() == 0)
                            return true;
                        else {
                            if (mVwHeader.getPaddingTop() > 0)
                                mVwHeader.setPadding(0, 0, 0, 0);
                            else
                                mVwHeader.setPadding(0, mVwHeader.getPaddingTop()
                                        + scrollSpace, 0, 0);
                            setProgress();
                            return true;
                        }
                    } else
                        return super.onTouchEvent(ev);
                } else if (offsetY < initY) {
                    initY = offsetY;
                    // System.out.println("move down");
                    if (mode == Mode.Start && status == Status.Ready) {
                        int rollbackSpace = mVwHeader.getPaddingTop() - scrollSpace;
                        if (rollbackSpace <= headerPadding) {
                            rollbackSpace = headerPadding;
                        }
                        mVwHeader.setPadding(0, rollbackSpace, 0, 0);
                        setProgress();
                        return true;
                    } else {
                        if (this.getLastVisiblePosition() > CListView.this
                                .getCount() - 2) {
                            mode = Mode.End;
                            status = Status.Ready;
                            doEndRefresh();
                        }
                        return super.onTouchEvent(ev);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mVwHeader.getPaddingTop() == 0) {
                    doStartRefresh();
                } else {
                    handler.sendEmptyMessage(0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mVwHeader.getPaddingTop() == headerPadding) {
                status = Status.Normal;
                mode = Mode.None;
                return;
            }
            int rollbackSpace = mVwHeader.getPaddingTop() - scrollSpace * 3;
            if (rollbackSpace <= headerPadding) {
                rollbackSpace = headerPadding;
            }
            setProgress();
            mVwHeader.setPadding(0, rollbackSpace, 0, 0);
            handler.sendEmptyMessageDelayed(0, 1);
        }
    };

    private void setProgress() {
        if (pullProgressListener == null)
            return;
        float progress = (headerHeight + mVwHeader.getPaddingTop()) * 1.0f
                / headerHeight;
        pullProgressListener.onPull(progress);
    }

    private void doStartRefresh() {
        if (mode != Mode.Start && status != Status.Ready)
            return;
        if (startPullRefreshListener != null) {
            status = Status.Loading;
            startPullRefreshListener.onRefresh();
            if (onLoadingListener != null)
                onLoadingListener.onLoading();
        }
    }

    private void doEndRefresh() {
        if (mode != Mode.End && status != Status.Ready)
            return;
        if (endPullRefreshListener != null) {
            status = Status.Loading;
            mVwFooter.setVisibility(VISIBLE);
            endPullRefreshListener.onRefresh();
            if (onLoadingListener != null)
                onLoadingListener.onLoading();
        }
    }

    public void completeRefresh() {
        if (mode == Mode.Start) {
            mode = Mode.None;
            // status = Status.Finish;
            handler.sendEmptyMessage(0);
        } else {
            mode = Mode.None;
            status = Status.Normal;
            mVwFooter.setVisibility(INVISIBLE);
        }
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
    }

    public void setEndPullRefreshListener(EndPullRefreshListener listener) {
        if (listener == null)
            removeFooterView(mVwFooter);
        else
            setRefreshFoooter(mVwFooter);
        this.endPullRefreshListener = listener;
    }

    public void setStartPullRefreshListener(StartPullRefreshListener listener) {
        this.startPullRefreshListener = listener;
    }

    public void setOnClickEmptyViewListener(OnClickEmptyViewListener onClickEmptyViewListener) {
        this.onClickEmptyViewListener = onClickEmptyViewListener;
    }

    public void setPullProgressListener(
            PullProgressListener pullProgressListener) {
        this.pullProgressListener = pullProgressListener;
    }

    public interface EndPullRefreshListener {
        void onRefresh();
    }

    public interface StartPullRefreshListener {
        void onRefresh();
    }

    public interface PullProgressListener {
        void onPull(float progress);
    }

    public interface OnLoadingListener {
        void onLoading();
    }

    public interface OnClickEmptyViewListener {
        void onRefresh();
    }


    // fast scroll mode
    private setScrollActionListener scrollActionListener;

    public interface setScrollActionListener {
        void onStopScroll(int first, int last);
    }

    public void setOnScrollActionListener(setScrollActionListener scrollListener) {
        this.scrollActionListener = scrollListener;
    }

    public void setFastVelocityY(float fastVelocityY) {
        this.fastVelocityY = fastVelocityY;
    }

    public boolean isScrolling() {
        return isScrolling;
    }


    private ListView.OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (onScrollListenerInCListview != null)
                onScrollListenerInCListview.onScrollStateChanged(view, scrollState);
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (CListView.this.getLastVisiblePosition() > CListView.this
                            .getCount() - 2
                            && mode != Mode.End
                            && status != Status.Loading) {
                        mode = Mode.End;
                        status = Status.Ready;
                        doEndRefresh();
                    }
                    if (!isScrolling) return;
                    isScrolling = false;
                    int first = getFirstVisiblePosition();
                    int last = getLastVisiblePosition();
                    if (first > -1 && last > -1)
                        scrollActionListener.onStopScroll(first, last);
                    break;
//                case ScrollActionListener.SCROLL_STATE_TOUCH_SCROLL:
//                    isScrolling = true;
//                    break;
//                case ScrollActionListener.SCROLL_STATE_FLING:
//                    isScrolling = true;
//                    break;
                default:
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (onScrollListenerInCListview != null)
                onScrollListenerInCListview.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    };

    private OnCListViewScrollListener onScrollListenerInCListview = null;

    public void setOnScrollListenerInCListview(OnCListViewScrollListener onScrollListenerInCListview) {
        this.onScrollListenerInCListview = onScrollListenerInCListview;
    }

    public interface OnCListViewScrollListener {
        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
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

    @Override
    public void setEmptyView(View newEmptyView) {

        if (null != newEmptyView) {
            // New view needs to be clickable so that Android recognizes it as a
            // target for Touch Events
            newEmptyView.setClickable(true);

            ViewParent newEmptyViewParent = newEmptyView.getParent();
            if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
                ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
            }

            newEmptyView.setVisibility(View.GONE);
            ViewParent parent = getParent();
            if (parent instanceof ViewGroup) {
                if(getEmptyView()!=null) {
                    ((ViewGroup) this.getParent()).removeView(getEmptyView());
                }
                ((ViewGroup) this.getParent()).addView(newEmptyView, ((ViewGroup) parent).indexOfChild(this) + 1);
            }

            newEmptyView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickEmptyViewListener != null)
                        onClickEmptyViewListener.onRefresh();
                }
            });

            newEmptyView.setLayoutParams(getLayoutParams());
        }

        super.setEmptyView(newEmptyView);
    }

}
