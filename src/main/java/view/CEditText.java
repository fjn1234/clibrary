package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.hugh.clibrary.R;

import interfaces.IView;
import obj.CustomAttrs;
import utils.LogUtil;
import utils.StringUtil;
import utils.ViewUtil;

public class CEditText extends EditText implements IView.ICustomAttrs, IView.IMapping {

    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true, parentClick = false, disableEmoji;
    private int maxLength = -1, maxNotAsciiLength = -1;

    public CEditText(Context context) {
        super(context);
    }

    public CEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttr(context, attrs);
    }

    public void loadDrawable() {
        Drawable[] drawables = ViewUtil.loadDrawable(getResources(), mAttrs);
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        setCompoundDrawablePadding(100);
        setCompoundDrawablePadding(mAttrs.getDrawablePadding());
    }

    private void setCustomAttr(Context context, AttributeSet attrs) {
        try {
            mAttrs = ViewUtil.initCustomAttrs(context, attrs, this);
            mAttrs.setTextSizePx((int) getTextSize());
            addTextChangedListener(textChangeListener);
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CEditText);
            mAttrs.setDrawableLeftResId(ta.getResourceId(R.styleable.CEditText_cdrawableLeft, 0));
            mAttrs.setDrawableRightResId(ta.getResourceId(R.styleable.CEditText_cdrawableRight, 0));
            mAttrs.setDrawableTopResId(ta.getResourceId(R.styleable.CEditText_cdrawableTop, 0));
            mAttrs.setDrawableBottomResId(ta.getResourceId(R.styleable.CEditText_cdrawableBottom, 0));
            mAttrs.setDrawableLeftWidthRatio(ta.getString(R.styleable.CEditText_cdrawableLeftWidth));
            mAttrs.setDrawableLeftHeightRatio(ta.getString(R.styleable.CEditText_cdrawableLeftHeight));
            mAttrs.setDrawableRightWidthRatio(ta.getString(R.styleable.CEditText_cdrawableRightWidth));
            mAttrs.setDrawableRightHeightRatio(ta.getString(R.styleable.CEditText_cdrawableRightHeight));
            mAttrs.setDrawableTopWidthRatio(ta.getString(R.styleable.CEditText_cdrawableTopWidth));
            mAttrs.setDrawableTopHeightRatio(ta.getString(R.styleable.CEditText_cdrawableTopHeight));
            mAttrs.setDrawableBottomWidthRatio(ta.getString(R.styleable.CEditText_cdrawableBottomWidth));
            mAttrs.setDrawableBottomHeightRatio(ta.getString(R.styleable.CEditText_cdrawableBottomHeight));
            mAttrs.setDrawablePaddingRatio(ta.getString(R.styleable.CEditText_cdrawablePadding));
            disableEmoji = ta.getBoolean(R.styleable.CEditText_cdisableEmoji, true);
            maxLength = ta.getInteger(R.styleable.CEditText_cmaxLength, -1);   //不限制的中文和英文或者其他特殊符号总数不能超过的字节（例如：“优秘123”就是7个长度）
            maxNotAsciiLength = ta.getInteger(R.styleable.CEditText_cmaxAsciiElseLength, -1); //中文和英文或者其他特殊符号总数不能超过的字节（例如：“优秘123”就是5个长度）
            int maxAsciiLength = ta.getInteger(R.styleable.CEditText_cmaxAsciiLength, -1);
            ta.recycle();
            if (maxAsciiLength > 0) {
                setMaxAsciiLength(maxAsciiLength);
            } else if (maxLength >= 0) {
                setMaxLength(maxLength);
            }
        } catch (Exception ex) {
            LogUtil.printStackTrace(this.getClass(), ex);
        }
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
            loadDrawable();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !parentClick && super.onTouchEvent(event);
    }

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            try {
                saveBeforeTextChanged(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (resetText) {
                    resetText = false;
                    return;
                }
                if (disableEmoji)
                    if (isEmoji(s, count)) return;
                if (maxNotAsciiLength > -1)
                    if (checkLength(s)) return;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void setParentClick(boolean parentClick) {
        this.parentClick = parentClick;
    }

    public void disableEmoji(boolean disableEmoji) {
        this.disableEmoji = disableEmoji;
    }

    //输入表情前的光标位置
    private int cursorPos;    //输入表情前EditText中的文本
    private String inputAfterText;    //是否重置了EditText的内容
    private boolean resetText;

    private void saveBeforeTextChanged(CharSequence s) {
        if (!resetText) {
            cursorPos = getSelectionEnd();
            inputAfterText = s.toString();
        }
    }

    private boolean isEmoji(CharSequence s, int count) {
        if (!resetText) {
            if (count >= 2) {//表情符号的字符长度最小为2
                CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                if (containsEmoji(input.toString())) {
                    resetText = true;
                    setText(inputAfterText);
                    CharSequence text = getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                }
            }
        } else {
            resetText = false;
        }
        return resetText;
    }

    private boolean checkLength(CharSequence s) {
        int length = 0;
        char[] cArr = s.toString().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : cArr) {
            if (StringUtil.isAscii(c))
                length += 1;
            else
                length += 2;
            if (length <= maxLength)
                builder.append(c);
        }
        if (length > maxLength) {
            resetText = true;
            setText(builder.toString());
            setSelection(builder.length());
        }
        return resetText;
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public void setMaxAsciiLength(int maxLength) {
        this.maxLength = maxLength;
        this.maxNotAsciiLength = (int) Math.floor(maxLength * 1f / 2);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setMaxNotAsciiLength(int maxNotAsciiLength) {
        this.maxNotAsciiLength = maxNotAsciiLength;
    }

    public void setPassWordVisible(boolean b) {
        int index = getSelectionStart();
        if (b)
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        else
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        postInvalidate();
        setSelection(index);
    }

    @Override
    public void setMappingValue(String v) {
        setText(v);
    }

    @Override
    public String getMappingValue() {
        return getText().toString();
    }
}
