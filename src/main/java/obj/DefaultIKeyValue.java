package obj;

import java.util.List;

import interfaces.IKeyValue;

/**
 * Created by Hugh on 2015/7/8.
 */
public class DefaultIKeyValue implements IKeyValue {

    private String key;
    private String value;
    private Object object;

    public DefaultIKeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public void setKey(Object key) {
        this.key = (String) key;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public void setObject(Object obj) {
        this.object = obj;
    }

    @Override
    public List getChildList() {
        return null;
    }
}
