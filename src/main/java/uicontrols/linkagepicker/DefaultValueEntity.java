package uicontrols.linkagepicker;

import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class DefaultValueEntity implements ValueEntity {

    private String key;
    private String value;

    public DefaultValueEntity(String key, String value) {
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
    public void setKey(Object key) {
        this.key=(String)key;
    }

    @Override
    public void setValue(Object value) {
        this.value=(String) value;
    }

    @Override
    public List getChildList() {
        return null;
    }
}
