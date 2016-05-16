package obj;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public abstract class CPagerAdapter<T> extends PagerAdapter {

    private List<T> list = new ArrayList<>();
    private List<View> vlist = new ArrayList<>();
    private int contentViewId;
    private Context context;
    private boolean keepOne = false, autoClear = true;
    private LayoutInflater inflater;

    public CPagerAdapter(Context context, int contentViewId) {
        this.contentViewId = contentViewId;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public boolean add(T obj) {
        vlist.add(inflater.inflate(contentViewId, null));
        return list.add(obj);
    }

    public boolean remove(T obj) {
        vlist.remove(list.indexOf(obj));
        return list.remove(obj);
    }

    public T remove(int index) {
        vlist.remove(index);
        return list.remove(index);
    }

    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (destroyItemListener != null)
            destroyItemListener.onDestroy(position, object);
        if (keepOne) {
            int index = position % list.size();
            container.removeView(vlist.get(index));
            vlist.remove(position);
            vlist.add(position, inflater.inflate(contentViewId, null));
            return;
        }
        if (!autoClear) return;
//      只保留当前与之前两张图片
        try {
            if (list.size() > 2) {
                int index = position % list.size();
                container.removeView(vlist.get(index));
                vlist.remove(position);
                vlist.add(position, inflater.inflate(contentViewId, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
//      加载对应位置的视图
        int index = position % list.size();
        View view = vlist.get(index);
        instanceItem(container, view, index);
        container.addView(view, 0);
        return view;
    }

    public abstract void instanceItem(ViewGroup container, View view, int position);

    public void setAutoClear(boolean autoClear) {
        this.autoClear = autoClear;
    }

    public void setKeepOne(boolean keepOne) {
        this.keepOne = keepOne;
    }

    public interface OnDestroyItemListener {
        void onDestroy(int position, Object object);
    }

    private OnDestroyItemListener destroyItemListener = null;

    public void setDestroyItemListener(OnDestroyItemListener destroyItemListener) {
        this.destroyItemListener = destroyItemListener;
    }
}
