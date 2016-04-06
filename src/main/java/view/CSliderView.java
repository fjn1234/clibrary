package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hugh.clibrary.R;

import obj.CustomAttrs;


/**
 * Created by Administrator on 2015/4/21.
 */
public class CSliderView extends CHorizontalScrollView {

    private boolean isScrollable = true,disableScroll=false;
    private RelativeLayout mLyoRoot;
    private CRelativeLayout mLyoLeftMenu, mLyoContent, mLyoRightMenu;
    private int contentWidth = 0, leftWidth, rightWidth;
    private boolean initSlider = false;
    private MenuStatus menuStatus = MenuStatus.Close;
    private boolean isLeftScrollable = true, isRightScrollable = true;

    private final float ACTION_MOVE_NUM = 50.0f;

    public enum MenuStatus {
        OpenRight, OpenLeft, Close
    }

    public CSliderView(Context context) {
        super(context);
        init();
    }

    public CSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mLyoRoot = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mLyoRoot.setLayoutParams(params);
        this.addView(mLyoRoot);
    }

    public void initSlider(CRelativeLayout mLyoLeftMenu, CRelativeLayout mLyoContent) {
        contentWidth = CustomAttrs.getScreenWidth();
        leftWidth = mLyoLeftMenu.getCustomAttrs().getWidth();
        rightWidth = 0;
        this.mLyoLeftMenu = mLyoLeftMenu;
        this.mLyoContent = mLyoContent;
        this.mLyoRightMenu = null;
        mLyoRoot.removeAllViews();
        mLyoLeftMenu.setId(R.id.slider_left_menu);
        mLyoRoot.addView(mLyoLeftMenu);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(contentWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, mLyoLeftMenu.getId());
        mLyoRoot.addView(mLyoContent, layoutParams);
        initSlider = true;
    }

    public void initSlider(CRelativeLayout mLyoLeftMenu, CRelativeLayout mLyoContent, CRelativeLayout mLyoRightMenu) {
        contentWidth = CustomAttrs.getScreenWidth();
        leftWidth = mLyoLeftMenu.getCustomAttrs().getWidth();
        rightWidth = mLyoRightMenu.getCustomAttrs().getWidth();
        this.mLyoLeftMenu = mLyoLeftMenu;
        this.mLyoContent = mLyoContent;
        this.mLyoRightMenu = mLyoRightMenu;

        mLyoRoot.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(leftWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mLyoLeftMenu.setId(R.id.slider_left_menu);
        mLyoRoot.addView(this.mLyoLeftMenu, layoutParams);


        layoutParams = new RelativeLayout.LayoutParams(rightWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, this.mLyoLeftMenu.getId());
        layoutParams.setMargins(contentWidth, 0, 0, 0);
        mLyoRoot.addView(this.mLyoRightMenu, layoutParams);

        layoutParams = new RelativeLayout.LayoutParams(contentWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, this.mLyoLeftMenu.getId());
        mLyoRoot.addView(this.mLyoContent, layoutParams);
        initSlider = true;
        hideMenu();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (initSlider) {
            initSlider = false;
            this.scrollTo(leftWidth, 0);
        }
    }

    private boolean mIsBeingDragged;
    private float mTouchSlop = CustomAttrs.getScreenWidth() * 0.05f;
    private long mTouchTime = 200;
    //    private VelocityTracker mVelocityTracker;
    private float startX, startY;
    private long startTime;

    private float dispatchX = 0, dispatchY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dispatchX = ev.getX();
                dispatchY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float moveX = ev.getX();
                float moveY = ev.getY();

                float resultX = Math.abs(moveX - startX);
                float resultY = Math.abs(moveY - startY);

                if (resultX < resultY && resultY > ACTION_MOVE_NUM) {
                    mLyoContent.dispatchTouchEvent(ev);
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isScrollable) return false;
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                startTime = System.currentTimeMillis();
                mIsBeingDragged = false;

//                if (mVelocityTracker == null) {
//                    mVelocityTracker = VelocityTracker.obtain();
//                }
//                mVelocityTracker.addMovement(ev);

                isScroll = true;
                initX = ev.getX();
                initTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                final float dx = x - startX;
                final float xDiff = Math.abs(dx);
                final float yDiff = Math.abs(y - startY);
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    mIsBeingDragged = true;
                } else if (System.currentTimeMillis() - startTime > mTouchTime) {
                    mIsBeingDragged = true;
                }
//                Log.e("**************", xDiff+"");
//                Log.e("**************", System.currentTimeMillis()-startTime+"");
                break;
        }
        super.onInterceptTouchEvent(ev);
        return mIsBeingDragged;
    }


    private float initX, offsetX;
    private boolean isScroll = false;
    private long initTime;
    private static final float MAX_SPEED = 0.35f;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

//        if (mVelocityTracker == null) {
//            mVelocityTracker = VelocityTracker.obtain();
//        }
//        mVelocityTracker.addMovement(ev);

        offsetX = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScroll) {
                    initX = ev.getX();
                    initTime = System.currentTimeMillis();
                    isScroll = true;
                }
//                if (Math.abs(offsetX - initX) > CustomAttrs.getScreenWidth() * 0.05f) {
//                    isScroll = true;
//                }
                break;
            case MotionEvent.ACTION_UP:
                //---------------------------------
//                final VelocityTracker velocityTracker = mVelocityTracker;
//                int mMaximumVelocity = ViewConfiguration.get(getContext())
//                        .getScaledMaximumFlingVelocity();
//                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);

                //------------------------------------------

                mIsBeingDragged = false;
                isScroll = false;
                long time = System.currentTimeMillis() - initTime;
                double speed = (offsetX - initX) / time;

//                Log.e("speed", speed + "");
//                Log.e("time", time + "");
                if (speed > MAX_SPEED) {
                    if (menuStatus == MenuStatus.Close) {
                        showLeftMenu();
                        return true;
                    } else if (menuStatus == MenuStatus.OpenRight) {
                        hideMenu();
                        return true;
                    }
                }
                if (speed < -MAX_SPEED) {
                    if (menuStatus == MenuStatus.OpenLeft) {
                        hideMenu();
                        return true;
                    } else if (menuStatus == MenuStatus.Close) {
                        showRightMenu();
                        return true;
                    }
                }
                if (this.getScrollX() < leftWidth / 2) {
                    showLeftMenu();
                } else if (this.getScrollX() > leftWidth + rightWidth / 2) {
                    showRightMenu();
                } else {
                    hideMenu();
                }
                return true;
        }

//        if (mVelocityTracker != null) {
//            mVelocityTracker.recycle();
//            mVelocityTracker = null;
//        }

        return super.onTouchEvent(ev);
    }

    public void showLeftMenu() {
        this.smoothScrollTo(0, 0);
        menuStatus = MenuStatus.OpenLeft;
    }

    public void showRightMenu() {
        this.smoothScrollTo(leftWidth + rightWidth, 0);
        menuStatus = MenuStatus.OpenRight;
    }

    public void hideMenu() {
        this.smoothScrollTo(leftWidth, 0);
        menuStatus = MenuStatus.Close;
    }

    public MenuStatus getMenuStatus() {
        return menuStatus;
    }

    public boolean isOpenMenu() {
        return menuStatus != MenuStatus.Close;
    }

    private static final float SCALE = 0.785f;
    private static final float ALPHA_SCALE = 0.6f;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale;
        if (l < leftWidth && isLeftScrollable) {
            scale = l * 1.0f / leftWidth;
            float rightScale = SCALE + (1 - SCALE) * scale;
            float leftScale = 1.0f - (1 - SCALE) * scale;
            float leftAlpha = ALPHA_SCALE + (1 - ALPHA_SCALE) * (1 - scale);
            mLyoLeftMenu.setTranslationX(leftWidth * scale * SCALE);
            mLyoLeftMenu.setScaleX(leftScale);
            mLyoLeftMenu.setScaleY(leftScale);
            mLyoLeftMenu.setAlpha(leftAlpha);

            mLyoContent.setPivotX(0);
            mLyoContent.setPivotY(CustomAttrs.getScreenHeight() * 0.5f);
            mLyoContent.setScaleX(rightScale);
            mLyoContent.setScaleY(rightScale);

            mLyoLeftMenu.setVisibility(VISIBLE);
            if (mLyoRightMenu != null && rightWidth > 0)
                mLyoRightMenu.setVisibility(INVISIBLE);

        } else if (l > leftWidth && isRightScrollable) {
            scale = 1 - (l - leftWidth) * 1.0f / rightWidth;
            float rightScale = SCALE - (1 - SCALE) * scale;
            float leftScale = SCALE + (1 - SCALE) * scale;
            float rightAlpha = ALPHA_SCALE + (1 - ALPHA_SCALE) * (1 - scale);
            mLyoRightMenu.setTranslationX(-rightWidth * scale * SCALE);
            mLyoRightMenu.setScaleX(rightScale);
            mLyoRightMenu.setScaleY(rightScale);
            mLyoRightMenu.setAlpha(rightAlpha);

            mLyoContent.setPivotX(contentWidth);
            mLyoContent.setPivotY(CustomAttrs.getScreenHeight() * 0.5f);
            mLyoContent.setScaleX(leftScale);
            mLyoContent.setScaleY(leftScale);

            if (mLyoLeftMenu != null && leftWidth > 0) {
                mLyoLeftMenu.setVisibility(INVISIBLE);
            }
            mLyoRightMenu.setVisibility(VISIBLE);
        } else {
            scale = 1;
            mLyoContent.setScaleX(1);
            mLyoContent.setScaleY(1);
        }
        if (onSliderChangeListener != null) onSliderChangeListener.onSliderChange(1 - scale);
    }

    public void setLeftScrollable(boolean isLeftScrollable) {
        this.isLeftScrollable = isLeftScrollable;
        if (mLyoLeftMenu != null) {
            if (isLeftScrollable) {
                mLyoLeftMenu.setVisibility(VISIBLE);
                leftWidth = mLyoLeftMenu.getWidth();
            } else {
                mLyoLeftMenu.setVisibility(GONE);
                leftWidth = 0;
            }
        }
    }

    public void setRightScrollable(boolean isRightScrollable) {
        this.isRightScrollable = isRightScrollable;
        if (mLyoRightMenu != null) {
            if (isRightScrollable) {
                mLyoRightMenu.setVisibility(VISIBLE);
                rightWidth = mLyoRightMenu.getWidth();
            } else {
                mLyoRightMenu.setVisibility(GONE);
                rightWidth = 0;
            }
        }
    }

    private OnSliderChangeListener onSliderChangeListener;

    public interface OnSliderChangeListener {
        void onSliderChange(float scale);
    }

    public void setOnSliderChangeListener(OnSliderChangeListener onSliderChangeListener) {
        this.onSliderChangeListener = onSliderChangeListener;
    }

    public boolean isScrollable() {
        return isScrollable;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (disableScroll) return;
        super.scrollTo(x, y);
    }

    public void setDisableScroll(boolean disableScroll) {
        this.disableScroll = disableScroll;
    }
}
