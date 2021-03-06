package interfaces;

import java.util.List;

/**
 * Created by Hugh on 2015/7/8.
 */
public interface IKeyValue<T> {

    String getKey();

    String getValue();

    Object getObject();

    void setKey(Object key);

    void setValue(Object value);

    void setObject(Object obj);

    List<T> getChildList();
}
