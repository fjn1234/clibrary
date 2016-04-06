package obj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import utils.ViewUtil;

public abstract class CRecyclerAdapter<T> extends RecyclerView.Adapter {

    private ArrayList<T> list = new ArrayList<>();

    public ArrayList<T> getList() {
        return list;
    }

    public CRecyclerAdapter(Context context) {
        this.context = context;
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

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return list.get(position);
    }

    public int size(){
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CellView cellView;

        public ViewHolder(View itemView) {
            super(itemView);
            cellView = new CellView(itemView);
        }

        public CellView getCellView() {
            return cellView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        convertView = InitConvertView(parent);
        ViewUtil.loadSubViewCustomAttrs(convertView);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setData(position, ((ViewHolder) holder).getCellView());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //定义抽象方法，让实例化的对象必须实现该方法
    public abstract View InitConvertView(ViewGroup parent);

    public abstract void setData(int position, CellView cell);

    private View convertView;

    public View getConvertView() {
        return convertView;
    }
}
