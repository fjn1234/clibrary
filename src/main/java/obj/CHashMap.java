package obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hugh on 2016/1/21.
 */
public class CHashMap<K, V> extends HashMap<K, V> {

    public CHashMap() {
    }

    public CHashMap(int capacity) {
        super(capacity);
    }

    public CHashMap(int capacity, float loadFactor) {
        super(capacity, loadFactor);
    }

    public CHashMap(Map<? extends K, ? extends V> map) {
        super(map);
    }

    public void loop(LoopListener loopListener) {
        Set<K> keySet = keySet();
        int i = 0;
        for (K k : keySet) {
            loopListener.loop(i++, k, get(k));
        }
    }

    public void loopSort(LoopListener loopListener) {
        List list = sort();
        int i = 0;
        Map.Entry entry;
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            entry = (Map.Entry) iter.next();
            loopListener.loop(i++, entry.getKey(), entry.getValue());
        }
    }

    public interface LoopListener {
        void loop(int i, Object k, Object v);
    }

    public V getAt(int position) {
        V v = null;
        if (position <= keySet().size() - 1) {
            Set<K> keySet = keySet();
            int i = 0;
            for (K k : keySet) {
                if (i == position) {
                    v = get(k);
                    break;
                }
            }
        }
        return v;
    }

    public List<V> toList() {
        List<V> list = new ArrayList<>(size());
        Set<K> keySet = keySet();
        for (K k : keySet) {
            list.add(get(k));
        }
        return list;
    }

    public List sort() {
        List arrayList = new ArrayList(entrySet());
        Collections.sort(arrayList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;
                return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
            }
        });
        return arrayList;
    }
}
