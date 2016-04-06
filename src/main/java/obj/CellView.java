package obj;

import android.util.SparseArray;
import android.view.View;

import java.util.HashMap;

import utils.ViewUtil;

public class CellView {
    private View convertView;
    private SparseArray<View> viewArr;
    private HashMap<String, SparseArray<View>> mappingArr = new HashMap<>(3, 3);

    public CellView(View convertView) {
        this.convertView = convertView;
        viewArr = new SparseArray<>();
    }

    public View getView(int viewId) {
        View view = viewArr.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            viewArr.put(viewId, view);
        }
        return view;
    }

    public void put(int viewId, View view) {
        viewArr.put(viewId, view);
    }

    public SparseArray<View> getViewMappingArr(Class clazz) {
        if (!mappingArr.containsKey(clazz.getName())) {
            mappingArr.put(clazz.getName(), ViewUtil.getViewMapEntityList(convertView, clazz));
        }
        return mappingArr.get(clazz.getName());
    }
}
