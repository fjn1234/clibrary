package obj;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Hugh on 2016/1/21.
 */
public class CArrayList<E> extends ArrayList<E> {

    public CArrayList(int capacity) {
        super(capacity);
    }

    public CArrayList() {
    }

    public CArrayList(Collection<? extends E> collection) {
        super(collection);
    }

    public void loop(LoopListener loopListener) {
        for (int i = 0, size = size(); i < size; i++) {
            loopListener.loop(i, get(i));
        }
    }

    public interface LoopListener {
        void loop(int index, Object obj);
    }
}
