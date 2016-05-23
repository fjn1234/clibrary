package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import interfaces.IKeyValue;
import obj.CustomAttrs;
import utils.ViewUtil;

/**
 * Created by Administrator on 2015/7/8.
 */
public class CValuePicker extends NumberPicker {
    private CustomAttrs mAttrs = new CustomAttrs();
    private boolean initCustomAttrs = true;

    public CValuePicker(Context context) {
        super(context);
    }

    public CValuePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs);
    }

    public CValuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomAttr(context, attrs);
    }

    @SuppressLint("NewApi")
    public CValuePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public static final String EMPTY_VALUE = "-";
    public static final String EMPTY_KEY = "0";
    List<IKeyValue> list;
    String[] displayVal;

    public <T extends IKeyValue> void setData(List<T> list, int position) {
        this.list = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (T entity : list) {
                this.list.add(entity);
            }
        }
        if (this.list.size() == 0) {
            this.list.add(new IKeyValue() {
                @Override
                public String getKey() {
                    return EMPTY_KEY;
                }

                @Override
                public String getValue() {
                    return EMPTY_VALUE;
                }

                @Override
                public Object getObject() {
                    return null;
                }

                @Override
                public void setKey(Object key) {

                }

                @Override
                public void setValue(Object value) {

                }

                @Override
                public void setObject(Object obj) {

                }

                @Override
                public List getChildList() {
                    return null;
                }
            });
        }
        this.setDisplayedValues(null);
        displayVal = new String[this.list.size()];
        for (int i = 0, length = this.list.size(); i < length; i++) {
            displayVal[i] = this.list.get(i).getValue();
        }
        this.setMinValue(0);
        this.setMaxValue(this.list.size() - 1);
        if (position < 0) position = 0;
        this.setValue(position);
        this.setDisplayedValues(displayVal);
        this.setWrapSelectorWheel(true);
    }

    public IKeyValue getSelectedValue() {
        if (list == null || list.size() == 0) return null;
        return list.get(this.getValue());
    }

    public <T extends IKeyValue> void setList(List<T> list) {
        if (list == null || list.size() == 0) return;
        this.list = new ArrayList<>();
        for (T entity : list) {
            this.list.add(entity);
        }
    }

    public IKeyValue getValueEntity(int position) {
        if (list == null || list.size() == 0 || position > list.size() - 1) return null;
        return list.get(position);
    }

    public void notifyUpdate() {
        setData(list, 0);
    }


}
