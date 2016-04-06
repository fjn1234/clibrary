package obj;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

//定义抽象类，让实例化的对象必须实现抽象方法
//T是可实例泛形变量
public abstract class CBaseCacheAdapter<T> extends BaseAdapter {

    private ArrayList<T> list = new ArrayList<>();

    public ArrayList<T> getList() {
        return list;
    }

    private SparseArray<View> cacheViews = new SparseArray<>();

    public CBaseCacheAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    public void add(T obj) {
        list.add(obj);
    }

    public void remove(int position) {
        list.remove(position);
    }

    public void remove(T obj) {
        list.remove(obj);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearCache() {
        cacheViews.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int index = cacheViews.indexOfKey(position);
        if (index > -1)
            return cacheViews.get(position);
        CellView cell;
        convertView = InitConvertView(position, convertView, parent);
        cell = new CellView(convertView);
        convertView.setTag(cell);
        setData(position, convertView, parent, cell);
        cacheViews.put(position, convertView);
        return convertView;
    }

    //定义抽象方法，让实例化的对象必须实现该方法
    public abstract View InitConvertView(int position, View convertView, ViewGroup parent);

    public abstract void setData(int position, View convertView, ViewGroup parent, CellView cell);
}
