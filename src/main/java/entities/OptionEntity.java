package entities;

import obj.CBaseEntity;
import utils.StringUtil;

public class OptionEntity extends CBaseEntity {


    private boolean isChoice = false;
    private String key;
    private String value;
    private Object object;

    public OptionEntity(String key, String value) {
        this(key, value, false);
    }

    public OptionEntity(String key, String value, boolean isChoice) {
        this.key = key;
        this.value = value;
        this.isChoice = isChoice;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    public String getKey() {
        return key;
    }

    public long getKeyLong() {
        return StringUtil.stringToLong(getKey());
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
