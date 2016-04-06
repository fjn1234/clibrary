package uicontrols.linkagepicker;

import java.util.List;

/**
 * Created by Hugh on 2015/7/8.
 */
public interface ValueEntity<T> {

    String getKey();

    String getValue();

    void setKey(Object key);

    void setValue(Object value);

    List<T> getChildList();
}
