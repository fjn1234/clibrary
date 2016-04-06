package obj;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;


public abstract class CPagerAdapter<T extends View> extends PagerAdapter {

    private ArrayList<T> list;
    private Context context;
    private boolean keepOne = false, autoClear = true;

    public CPagerAdapter(Context context) {
        this.list = new ArrayList<>();
        this.context = context;
    }

    public boolean add(T obj) {
        return list.add(obj);
    }

    public boolean remove(T obj) {
        return list.remove(obj);
    }

    public boolean remove(int index) {
        T obj = list.get(index);
        return remove(obj);
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
            container.removeView((View) object);
            return;
        }
        if (!autoClear) return;
//      只保留当前与之前两张图片
        try {
            if (list.size() > 2) {
                T view = list.get(position % list.size());
                container.removeView(view);
                list.remove(position);
                Class clazz = Class.forName(object.getClass().getName());
                Constructor c1 = clazz.getDeclaredConstructor(new Class[]{Context.class});
                c1.setAccessible(true);
                Object obj = c1.newInstance(new Object[]{this.context});
                list.add(position, (T) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T instantiateItem(ViewGroup container, int position) {
//      加载对应位置的视图
        return instanceItem(container, list, position % list.size());
    }

    public abstract T instanceItem(ViewGroup container, ArrayList<T> list, int position);

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
