package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hugh.clibrary.R;

import obj.CustomAttrs;

public class AnnulusProgressView extends CView implements View.OnClickListener {

    private String TAG = AnnulusProgressView.class.getSimpleName() + ":";

    public AnnulusProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public AnnulusProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnnulusProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnnulusProgressView);
            percent = ta.getFloat(R.styleable.AnnulusProgressView_apv_percent, 0);
            startAngle = ta.getFloat(R.styleable.AnnulusProgressView_apv_startAngle, 225);
            sweepAngle = ta.getFloat(R.styleable.AnnulusProgressView_apv_sweepAngle, -270);
            strokeWidth = ta.getFloat(R.styleable.AnnulusProgressView_apv_strokeWidth, 15);
            strokeProgressWidth = ta.getFloat(R.styleable.AnnulusProgressView_apv_strokeProgressWidth, strokeWidth + 1);
            touchArea = ta.getFloat(R.styleable.AnnulusProgressView_apv_touchArea, 0.7f);
            dotRadius = ta.getFloat(R.styleable.AnnulusProgressView_apv_dotRadius, 22);
            strokeColor = ta.getColor(R.styleable.AnnulusProgressView_apv_strokeColor, Color.BLUE);
            strokeProgressColor = ta.getColor(R.styleable.AnnulusProgressView_apv_strokeProgressColor, Color.GREEN);
            dotColor = ta.getColor(R.styleable.AnnulusProgressView_apv_dotColor, Color.RED);
            showDot = ta.getBoolean(R.styleable.AnnulusProgressView_apv_showDot, true);
            ta.recycle();
        }
        setOnClickListener(this);
    }

    private void initParams() {
        startAngle %= 360;
        sweepAngle %= 360;
        startAngle = startAngle < 0 ? 360 + startAngle : startAngle;
        paint1 = new Paint();
        paint1.setAntiAlias(true);                       //设置画笔为无锯齿
        paint1.setColor(strokeColor);                    //设置画笔颜色
        paint1.setStrokeWidth(strokeWidth);              //线宽
        paint1.setStyle(Paint.Style.STROKE);             //设置实线
        paint1.setStrokeCap(Paint.Cap.ROUND);            //设置画笔为圆头
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(strokeProgressColor);
        paint2.setStrokeWidth(strokeProgressWidth);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paintDot = new Paint();
        paintDot.setAntiAlias(true);
        paintDot.setColor(dotColor);
        oval = new RectF();                     //RectF对象
        oval.left = offsetRadius;   //左开始点
        oval.top = offsetRadius;   //上开始点
        oval.right = 2 * r + offsetRadius;  //右结束点
        oval.bottom = 2 * r + offsetRadius; //底结束点
    }

    private int size;
    private int strokeColor;            //圆环的颜色
    private int strokeProgressColor;    //进度条的颜色
    private int dotColor;           //小圆的颜色
    private float r;                //圆的半径
    private float offsetRadius;     //画圆的偏移量
    private float touchArea;       //触摸有效范围
    private float strokeWidth;      //圆环环宽度
    private float strokeProgressWidth;      //进度条宽度
    private float startAngle;      //圆弧起始角度，单位为度。
    private float sweepAngle;     //圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度
    private float dotRadius;     //圆点的半径
    private RectF oval;
    Paint paint1, paint2, paintDot;
    private float percent;
    private boolean showDot;
    private boolean once = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);                  //透明背景
        canvas.drawArc(oval, startAngle, sweepAngle, false, paint1);    //绘制圆弧
        canvas.drawArc(oval, startAngle, sweepAngle * percent, false, paint2);    //绘制进度圆弧
        if (showDot) {
            float angle = getAngle(percent);
            float x = getX(angle);
            float y = getY(x, angle);
            canvas.drawCircle(getOffset(x), getOffset(y), dotRadius, paintDot);    //绘制进度圆点
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (once || isInEditMode()) {
            once = false;
            if (getCustomAttrs().getScreenDesignWidth() > 0) {
                strokeWidth = strokeWidth / getCustomAttrs().getScreenDesignWidth() * CustomAttrs.getScreenWidth();
                dotRadius = dotRadius / getCustomAttrs().getScreenDesignWidth() * CustomAttrs.getScreenWidth();
                strokeProgressWidth = strokeProgressWidth / getCustomAttrs().getScreenDesignWidth() * CustomAttrs.getScreenWidth();
            }
            offsetRadius = strokeProgressWidth > strokeWidth ? strokeProgressWidth / 2 : strokeWidth / 2;
            offsetRadius = dotRadius > offsetRadius ? dotRadius : offsetRadius;
            r = (getLayoutParams().width - 2 * offsetRadius) / 2;
            size = (int) (2 * r + 2 * offsetRadius);
            initParams();
        }
        setMeasuredDimension(size, size);
    }

    //设置百分比，重绘进度条
    public void setPercent(float percent) {
        this.percent = percent < 0 ? 0.001f : percent > 1 ? 1f : percent;
        invalidate();
        if (onChangeListener != null)
            onChangeListener.onChange(percent);
    }

    //获取当前百分比
    public float getPercent() {
        return percent;
    }

    //获取从0度到圆头的角度，用于计算x,y坐标
    private float getAngle(float percent) {
        return (startAngle + sweepAngle * percent) % 360;
    }

    //获取小圆的圆心在圆环上的x坐标(不包含偏移量)
    private float getX(float angle) {
        float x = (float) (Math.cos(Math.toRadians(angle)) * r) + r;
        return x;
    }

    //获取小圆的圆心在圆环上的y坐标(不包含偏移量)
    private float getY(float x, float angle) {
        x = Math.abs(x - r);
        if ((-180 < angle && angle < 0) || angle > 180)
            return r - (float) Math.sqrt(Math.abs(r * r - x * x));
        else
            return (float) Math.sqrt(Math.abs(r * r - x * x)) + r;
    }

    private float getOffset(float v) {
        return v + offsetRadius;
    }

    //小圆的圆心在圆环上的x坐标（加入偏移量）
    private float getAdjustX(float x) {
        x -= offsetRadius;
        x = x < 0 ? 0 : x;
        return x - r;
    }

    //小圆的圆心在圆环上的y坐标（加入偏移量）
    private float getAdjustY(float y) {
        y -= offsetRadius;
        y = y < 0 ? 0 : y;
        return r - y;
    }

    //获取从0度到触摸点的角度，用于计算进度条的旋转角度
    private float getDegress(float x, float y) {
        float degrees = (float) Math.toDegrees(Math.atan(Math.abs(y / x)));
        if (x > 0 && y > 0) {
            degrees = 360 - degrees;
        } else if (x < 0 && y < 0) {
            degrees = 180 - degrees;
        } else if (x < 0 && y > 0) {
            degrees = 180 + degrees;
        }
        return degrees;
    }

    //计算进度条的旋转角度
    private float getSweep(float degrees) {
        if (sweepAngle < 0) {
            float sweep = startAngle + sweepAngle;
            if (sweep > 0) {
                if (degrees > startAngle || degrees < sweep)
                    return Float.MAX_VALUE;
                else
                    return degrees - sweep + sweepAngle;
            } else {
                float degrees2 = degrees - 360;
                if (degrees > startAngle && degrees2 < sweep)
                    return Float.MAX_VALUE;
                else if (startAngle - degrees > 0) {
                    return -startAngle + degrees;
                } else {
                    return -startAngle + degrees2;
                }
            }
        }
        return degrees;
    }

    //判断是否在有效的触摸空间
    private boolean isValidArea(float x, float y) {
        float validR = r * touchArea;
        return x * x + y * y > validR * validR;
    }

    //把进度条设置条触摸位置
    private void scrollToSweep(float x, float y) {
        float sweep = getSweep(getDegress(x, y));
        if (sweep < Float.MAX_VALUE)
            setPercent(sweep / this.sweepAngle);
    }

    private float clickX, clickY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickX = getAdjustX(event.getX());
                clickY = getAdjustY(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                float clickX = getAdjustX(event.getX());
                float clickY = getAdjustY(event.getY());
                if (isValidArea(clickX, clickY)) {
                    this.clickX = clickX;
                    this.clickY = clickY;
                    scrollToSweep(clickX, clickY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isValidArea(this.clickX, this.clickY)) {
                    scrollToSweep(this.clickX, this.clickY);
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        if (isValidArea(clickX, clickY)) {
            scrollToSweep(clickX, clickY);
        }
    }

    private OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public interface OnChangeListener {
        void onChange(float percent);
    }

    public void setShowDot(boolean showDot) {
        this.showDot = showDot;
        invalidate();
    }
}
