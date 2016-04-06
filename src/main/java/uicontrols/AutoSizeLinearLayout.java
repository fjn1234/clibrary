package uicontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import interfaces.IView;
import obj.CustomAttrs;
import view.CLinearLayout;
import view.CTextView;

/**
 * Created by Hugh on 2016/1/29.
 */
public class AutoSizeLinearLayout extends CLinearLayout {

    List<View> list = new ArrayList<>();

    private int width;

    public AutoSizeLinearLayout(Context context) {
        this(context, null);
    }

    public AutoSizeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoSizeLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void addItemView(View v) {
        list.add(v);
    }

    public void create() {
        CLinearLayout lyo;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<View> tempList = list;
        while (tempList.size() > 0) {
            lyo = new CLinearLayout(getContext());
            lyo.setOrientation(LinearLayout.HORIZONTAL);
            lyo.setLayoutParams(layoutParams);
            createSingleLineVariableCell(lyo, tempList);
            for (int i = 0, l = lyo.getChildCount(); i < l; i++) {
                tempList.remove(0);
            }
            addView(lyo);
        }
    }

    private void createSingleLineVariableCell(LinearLayout layout, List<View> tempList) {
        int thisWidth = getCustomAttrs().getWidth();
        int maxWidth = thisWidth;
        int marginLeft, marginRight, length;
        View v;
        interfaces.IView.ICustomAttrs iCustomAttrs;
        for (int i = 0; i < tempList.size(); i++) {
            v = tempList.get(i);
            iCustomAttrs = (interfaces.IView.ICustomAttrs) tempList.get(i);
            iCustomAttrs.loadCustomAttrs();
            marginLeft = iCustomAttrs.getCustomAttrs().getMarginLeft();
            marginRight = iCustomAttrs.getCustomAttrs().getMarginRight();
            length = v.getLayoutParams().width + marginLeft + marginRight;
            if (maxWidth > length) {
                layout.addView(tempList.get(i));
                maxWidth -= length;
            } else {
                if (length >= thisWidth) {
                    layout.addView(tempList.get(i));
                }
                break;
            }
        }
    }


}
