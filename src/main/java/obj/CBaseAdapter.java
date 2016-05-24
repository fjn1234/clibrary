package obj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.ViewUtil;

//定义抽象类，让实例化的对象必须实现抽象方法
//T是可实例泛形变量
public abstract class CBaseAdapter<T> extends BaseAdapter {

    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private int convertViewId;

    public CBaseAdapter(Context context, int convertViewId) {
        this.context = context;
        this.convertViewId = convertViewId;
    }

    private Context context;

    public Context getContext() {
        return context;
    }

    public void add(T obj) {
        list.add(obj);
    }

    public void add(int position, T obj) {
        list.add(position, obj);
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

    public int indexOf(T obj) {
        return list.indexOf(obj);
    }

    public void replace(int position, T obj) {
        list.remove(position);
        list.add(position, obj);
        notifyDataSetChanged();
    }

    public void replace(T oldObj, T obj) {
        int position = indexOf(oldObj);
        list.remove(position);
        list.add(position, obj);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CellView cell;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(convertViewId, null);
            ViewUtil.loadSubViewCustomAttrs(convertView);
            cell = new CellView(convertView);
            convertView.setTag(cell);
            initConvertView(convertView,parent,cell);
        } else
            cell = (CellView) convertView.getTag();
        setData(position, convertView, parent, cell);
        return convertView;
    }

    public abstract void initConvertView(View convertView, ViewGroup parent, CellView cell);
    public abstract void setData(int position, View convertView, ViewGroup parent, CellView cell);
}
