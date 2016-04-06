package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugh.clibrary.R;

import interfaces.IView;
import obj.CustomAttrs;
import utils.StringUtil;
import utils.ViewUtil;

public class CRatingBar extends CLinearLayout implements IView.IMapping {
    private boolean mClickable;
    private OnRatingListener onRatingListener;
    private float starImageSize;
    private int space = 0;
    private int starCount;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarCount(int startCount) {
        this.starCount = starCount;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    private int starCountFill;

    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }

    public void setRatingClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public CRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CRatingBar);
        float ratio = ViewUtil.getFloat(a.getString(R.styleable.CRatingBar_cstarImageByRatio));
        starImageSize = (float) Math.ceil(CustomAttrs.getScreenWidth() * ratio);
        if (starImageSize == 0)
            starImageSize = a.getDimension(R.styleable.CRatingBar_cstarImageSize, 20);
        ratio = ViewUtil.getFloat(a.getString(R.styleable.CRatingBar_cspace));
        space = (int) Math.ceil(CustomAttrs.getScreenWidth() * ratio);
        starCount = a.getInteger(R.styleable.CRatingBar_cstarCount, 5);
        starEmptyDrawable = a.getDrawable(R.styleable.CRatingBar_cstarEmpty);
        starFillDrawable = a.getDrawable(R.styleable.CRatingBar_cstarFill);
        mClickable = a.getBoolean(R.styleable.CRatingBar_crating, false);
        a.recycle();
        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context, attrs);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickable) {
                        setStar(indexOfChild(v) + 1);
                        if (onRatingListener != null) {
                            onRatingListener.onRating(indexOfChild(v) + 1);
                        }
                    }

                }
            });
            addView(imageView);
        }
    }

    private ImageView getStarImageView(Context context, AttributeSet attrs) {
        ImageView imageView = new ImageView(context);
        LayoutParams para = new LayoutParams((int) starImageSize, (int) starImageSize);
        para.setMargins(0, 0, space, 0);
        imageView.setLayoutParams(para);
        imageView.setImageDrawable(starEmptyDrawable);
        return imageView;

    }

    public void setStar(int star) {
        star = star > this.starCount ? this.starCount : star;
        star = star < 0 ? 0 : star;
        this.starCountFill = star;
        for (int i = 0; i < star; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        for (int i = this.starCount - 1; i >= star; --i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
    }

    public int getStarCountFill() {
        return starCountFill;
    }

    /**
     * 该监听器用于监听选中Tab时View的变化
     */
    public interface OnRatingListener {

        void onRating(int ratingScore);

    }

    @Override
    public void setMappingValue(String v) {
        setStar(StringUtil.stringToInteger(v, 0));
    }

    @Override
    public String getMappingValue() {
        return getStarCountFill() + "";
    }
}
