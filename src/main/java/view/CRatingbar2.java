package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugh.clibrary.R;

import java.text.DecimalFormat;

import interfaces.IView;
import obj.CustomAttrs;
import utils.StringUtil;
import utils.ViewUtil;


/**
 * Created by lin on 2016/3/29.
 */
public class CRatingbar2 extends CRelativeLayout implements IView.IMapping {


    public static final Integer BARTYPE_ALL = 1;
    public static final Integer BARTYPE_HALF = 2;
    public static final Integer BARTYPE_DECIMAL = 3;

    private OnRatingChangeListern onRatingChangeListern;

    private Drawable barImageOff, barImageOn;
    private boolean isTouch = false;
    private int mBarMax = 5;      //默认显示5个星星
    private int barItemWidth = 40;  //每一个星号宽度
    private int starMargin = 5;     //每个星号地图间距
    private LinearLayout mLyoOff, mLyoOn;
    private float rating = 0;  //显示多少（例如4.5个）
    private int LyoOffWidth = 0;
    private Integer barType = 3;  //默认的星号类型是小数点（1表示显示整颗星星，2表示半颗，3显示——例如0.3颗）


    private Context mContext;

    public CRatingbar2(Context context) {
        this(context, null);
    }


    public CRatingbar2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        removeAllViews();
        initAttrs(context, attrs);
        initStarOffView(mBarMax, context, barItemWidth, starMargin);

        initStarOnView(mBarMax, context, barItemWidth, starMargin);
        addView(mLyoOff);

        setLyoOnView(rating);
        addView(mLyoOn);


    }

    private void initAttrs(Context context, AttributeSet attrs) {
        mLyoOff = new LinearLayout(context);
        mLyoOn = new LinearLayout(context);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CRatingbarView);
        mBarMax = typedArray.getInteger(R.styleable.CRatingbarView_bar_max, mBarMax);
        isTouch = typedArray.getBoolean(R.styleable.CRatingbarView_is_touch, isTouch);
        barType = typedArray.getInteger(R.styleable.CRatingbarView_bar_type, barType);

        float ratio_item_width = ViewUtil.getFloat(typedArray.getString(R.styleable.CRatingbarView_cbar_imageWidth));
        if (ratio_item_width > 0) {
            barItemWidth = (int) Math.ceil(CustomAttrs.getScreenWidth() * ratio_item_width);
        } else {
            barItemWidth = typedArray.getInteger(R.styleable.CRatingbarView_bar_imageWidth, barItemWidth);
        }

        float ratio_item_margin = ViewUtil.getFloat(typedArray.getString(R.styleable.CRatingbarView_cbar_margin));
        if (ratio_item_margin >= 0) {
            starMargin = (int) Math.ceil(CustomAttrs.getScreenWidth() * ratio_item_margin);
        } else {
            starMargin = typedArray.getInteger(R.styleable.CRatingbarView_bar_margin, starMargin);
        }


        rating = typedArray.getFloat(R.styleable.CRatingbarView_bar_rating, rating);
        barImageOff = typedArray.getDrawable(R.styleable.CRatingbarView_bar_offImage);
        barImageOn = typedArray.getDrawable(R.styleable.CRatingbarView_bar_onImage);

        typedArray.recycle();
    }

    private void initStarOffView(int starCount, Context context, int barItemWidth, int sartMargin) {
        for (int i = 0; i < starCount; i++) {
            LyoOffWidth = LyoOffWidth + barItemWidth + sartMargin;
            ImageView imageViewOff = new ImageView(context);
            LinearLayout.LayoutParams paramsOff = new LinearLayout.LayoutParams(barItemWidth, barItemWidth);
            paramsOff.setMargins(0, 0, sartMargin, 0);
            imageViewOff.setLayoutParams(paramsOff);
            //
            imageViewOff.setBackgroundDrawable(barImageOff);
            mLyoOff.addView(imageViewOff);
        }
    }

    private void initStarOnView(float starCount, Context context, int barItemWidth, int sartMargin) {
        for (int i = 0; i < starCount; i++) {
            ImageView imageViewOn = new ImageView(context);
            LinearLayout.LayoutParams paramsOn = new LinearLayout.LayoutParams(barItemWidth, barItemWidth);
            paramsOn.setMargins(0, 0, sartMargin, 0);
            imageViewOn.setLayoutParams(paramsOn);
            //R.drawable.um_ic_star_on
            imageViewOn.setBackgroundDrawable(barImageOn);
            mLyoOn.addView(imageViewOn);
        }
    }


    private float setRatingByType(float rating) {

        int i_r = (int) rating;
        float num = rating - i_r;
        switch (barType) {
            case 1:
                if (num > 0) {
                    rating = i_r + 1;
                } else {
                    rating = i_r;
                }
                this.rating = rating;
                break;
            case 2:
                if (num > 0 && num <= 0.5) {
                    rating = (float) (i_r + 0.5);
                } else if (num > 0.5) {
                    rating = i_r + 1;
                } else {
                    rating = i_r;
                }
                this.rating = rating;
                break;
        }
        return rating;
    }


    public int initLyoOnViewAttr(float rating) {
        rating = setRatingByType(rating);
        int marginRight = 0;
        if (rating >= mBarMax) {
            marginRight = mBarMax * (barItemWidth + starMargin);
        } else {
            double margin = mBarMax - rating;
            if (margin > 0 && margin < mBarMax) {
                int rating2 = (int) rating;
                double rating3 = rating - rating2;
                marginRight = ((int) (rating2 * (barItemWidth + starMargin))) + ((int) (barItemWidth * rating3));
            }
        }
        return marginRight;
    }

    public void setLyoOnView(float rating) {
        rating = setRatingByType(rating);
        int marginRight = initLyoOnViewAttr(rating);
        LinearLayout.LayoutParams paramsOn = new LinearLayout.LayoutParams(marginRight, barItemWidth);
        mLyoOn.setLayoutParams(paramsOn);
    }

    public void setRating(float rating) {
        int marginRight = initLyoOnViewAttr(rating);
        LayoutParams paramsOn = new LayoutParams(marginRight, barItemWidth);
        mLyoOn.setLayoutParams(paramsOn);

        if (onRatingChangeListern != null) {
            onRatingChangeListern.getRatingChange(this.rating);
        }

    }


    DecimalFormat df = new DecimalFormat("0.0");

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isTouch == false) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setRatingByTouch((int) event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                setRatingByTouch((int) event.getX());
                break;
            case MotionEvent.ACTION_UP:
        }
        return true;
    }

    private double downRating;

    public void setRatingByTouch(int xTouch) {
        double touchRating = 0;

        double d_xTouch = (double) xTouch;
        double d_barItemWidth = (double) barItemWidth;
        double d_width = (double) (barItemWidth + starMargin);
        int width = (barItemWidth + starMargin);
        if (xTouch < barItemWidth) {
            touchRating = d_xTouch / d_barItemWidth;
        } else if (xTouch >= barItemWidth && xTouch < width) {
            touchRating = 1;
        } else if (xTouch >= width) {
            int n = xTouch % width;
            int r = xTouch / width;

            if (n == 0) {
                touchRating = r;
            } else {
                double d_n = (double) n;
                if (n < barItemWidth) {
                    touchRating = d_n / d_barItemWidth + r;
                } else if (n >= barItemWidth && n < width) {
                    touchRating = 1 + r;
                }
            }
        }
        downRating = Double.parseDouble(df.format(touchRating));
        downRating = downRating >= mBarMax ? mBarMax : downRating;

        if (rating != (float) downRating) {
            rating = (float) downRating;
            setRating(rating);
        }
    }


    public void setOnRatingChangeListern(OnRatingChangeListern onRatingChangeListern) {
        this.onRatingChangeListern = onRatingChangeListern;
    }

    public interface OnRatingChangeListern {
        public void getRatingChange(float rating);
    }


    public void setBarImageOff(Drawable barImageOff) {
        this.barImageOff = barImageOff;
    }

    public void setBarImageOn(Drawable barImageOn) {
        this.barImageOn = barImageOn;
    }

    public void setBarItemWidth(int barItemWidth) {
        this.barItemWidth = barItemWidth;
    }


    public void setmBarMax(int mBarMax) {
        this.mBarMax = mBarMax;

        removeAllViews();
        mLyoOff = new LinearLayout(mContext);
        mLyoOn = new LinearLayout(mContext);


        LyoOffWidth = 0;
        initStarOffView(mBarMax, mContext, barItemWidth, starMargin);
        initStarOnView(mBarMax, mContext, barItemWidth, starMargin);


        addView(mLyoOff);

        addView(mLyoOn);


        LayoutParams paramsOff = new LayoutParams(LyoOffWidth, barItemWidth);
        mLyoOff.setLayoutParams(paramsOff);
        LayoutParams paramsOn = new LayoutParams(LyoOffWidth, barItemWidth);
        mLyoOn.setLayoutParams(paramsOn);

    }


    public void SetIsTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public Drawable getBarImageOff() {
        return barImageOff;
    }

    public Drawable getBarImageOn() {
        return barImageOn;
    }


    public int getBarItemWidth() {
        return barItemWidth;
    }

    public float getRating() {
        return rating;
    }

    public int getmBarMax() {
        return mBarMax;
    }

    public void setBarType(Integer barType) {
        this.barType = barType;
    }


    @Override
    public void setMappingValue(String v) {
        rating = StringUtil.stringToFloat(v);
        setRating(rating);
    }

    @Override
    public String getMappingValue() {
        return getRating() + "";
    }
}
