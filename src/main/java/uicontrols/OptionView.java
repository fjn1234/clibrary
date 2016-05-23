package uicontrols;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugh.clibrary.R;

import java.util.ArrayList;
import java.util.List;

import entities.OptionEntity;
import interfaces.IView;
import obj.CRecyclerAdapter;
import obj.CellView;
import obj.CustomAttrs;
import view.CButton;
import view.CRecyclerView;
import view.CTextView;

/**
 * Created by Hugh on 2016/1/9.
 */
public class OptionView extends CRecyclerView {

    public OptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OptionView);
        cellViewId = ta.getResourceId(R.styleable.OptionView_ccellViewId, 0);
        singleRowCount = ta.getInteger(R.styleable.OptionView_citemPerRow, 3);
        maxHeightByCell = ta.getInteger(R.styleable.OptionView_cmaxHeightByCell, 0);
        singleSelect = ta.getBoolean(R.styleable.OptionView_csingleSelect, false);
        showIcon = ta.getBoolean(R.styleable.OptionView_cshowIcon, true);
        autoHeight = ta.getBoolean(R.styleable.OptionView_cautoHeight, true);
        int mode = ta.getInteger(R.styleable.OptionView_cmode, 3);
        if (mode == 1) {
            setLayoutManager(new GridLayoutManager(getContext(), singleRowCount));
        } else if (mode == 2) {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        } else {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        }
        ta.recycle();
    }

    public OptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionView(Context context) {
        this(context, null);
    }

    public int getSingleRowCount() {
        return singleRowCount;
    }

    //------------------------------------------------------------------
    private CRecyclerAdapter<OptionEntity> adapter;
    private int cellViewId = -1;
    private int singleRowCount = 1;
    private int cellViewHeight = 0;
    private int maxHeightByCell = 0;
    private boolean singleSelect, autoHeight;
    private boolean isChanged = false, showIcon;
    private OnItemClickListener itemClickListener;

    public void setCellViewId(int cellViewId) {
        this.cellViewId = cellViewId;
    }

    public void setCellViewLayout(int cellViewId) {
        this.cellViewId = cellViewId;
    }

    public int getCellViewHeight() {
        return cellViewHeight;
    }

    public int size() {
        if (adapter == null) return 0;
        return adapter.size();
    }

    public void setHeight() {
        int size = size();
        size = maxHeightByCell > 0 && size > maxHeightByCell ? maxHeightByCell : size;
        int height = cellViewHeight * (int) Math.ceil(size * 1f / getSingleRowCount());
        getCustomAttrs().setHeightRatio(height * 100f / CustomAttrs.getScreenHeight() + "%");
        loadCustomAttrs();
    }

    private void initData() {
        View v = LayoutInflater.from(getContext()).inflate(cellViewId, null).findViewById(R.id.lyo_root);
        if (v instanceof IView.ICustomAttrs) {
            IView.ICustomAttrs iCustomAttrs = (IView.ICustomAttrs) v;
            iCustomAttrs.loadCustomAttrs();
            cellViewHeight = iCustomAttrs.getCustomAttrs().getHeight();
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            if (lp != null)
                cellViewHeight += lp.topMargin + lp.bottomMargin;
        }
    }

    public void addOption(OptionEntity option) {
        if (adapter == null) {
            initAdapter();
            initData();
        }
        adapter.add(option);
        if (autoHeight)
            setHeight();
    }

    public void removeOption(int position) {
        if (adapter == null) return;
        adapter.remove(position);
    }

    public void removeOption(OptionEntity option) {
        if (adapter == null) return;
        adapter.remove(option);
    }

    public void clear() {
        if (adapter == null) return;
        adapter.clear();
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        adapter = new CRecyclerAdapter<OptionEntity>(getContext(), cellViewId) {

            @Override
            public void setData(final int position, CellView cell) {
                try {
                    final OptionEntity entity = adapter.getItem(position);
                    cell.getView(R.id.lyo_root).setSelected(entity.isChoice());
                    cell.getView(R.id.tv_check).setSelected(entity.isChoice());
                    ((CTextView) cell.getView(R.id.tv_check)).setText(entity.getValue());
                    cell.getView(R.id.btn_check).setVisibility(showIcon ? VISIBLE : INVISIBLE);
                    cell.getView(R.id.lyo_root).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (singleSelect)
                                clearSelected();
                            entity.setChoice(!entity.isChoice());
                            isChanged = true;
                            if (itemClickListener != null)
                                itemClickListener.onClick(position, entity);
                            notifyDataSetChanged();
                        }
                    });
                    if (contentViewListener != null)
                        contentViewListener.setData(position, cell, entity);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        setAdapter(adapter);
    }

    public List<OptionEntity> getSelectedOption() {
        List<OptionEntity> entityList = new ArrayList<>();
        if (adapter == null || adapter.size() == 0) return entityList;
        for (OptionEntity entity : adapter.getList()) {
            if (entity.isChoice()) {
                entityList.add(entity);
            }
        }
        return entityList;
    }

    private void clearSelected() {
        for (OptionEntity entity : adapter.getList()) {
            entity.setChoice(false);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, OptionEntity option);
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public interface ContentViewListener {
        void setData(final int position, CellView cell, OptionEntity option);
    }

    private ContentViewListener contentViewListener;

    public void setContentViewListener(ContentViewListener contentViewListener) {
        this.contentViewListener = contentViewListener;
    }
}
