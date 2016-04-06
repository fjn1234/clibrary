package entities;

import obj.CBaseEntity;

public class PhotoEntity extends CBaseEntity {


    protected boolean isChoice = false;
    private String uri = "";
    private String file = "";
    private String key = "";
    private Object object;

    public PhotoEntity(String uri) {
        this.uri = uri;
    }

    public PhotoEntity(String uri, String file) {
        this.uri = uri;
        this.file = file;
    }

    public PhotoEntity(String uri, boolean isChoice) {
        this.uri = uri;
        this.isChoice = isChoice;
    }

    public PhotoEntity(String uri, String file, boolean isChoice) {
        this.uri = uri;
        this.file = file;
        this.isChoice = isChoice;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setIsChoice(boolean isChoice) {
        this.isChoice = isChoice;
    }

    public String getUri() {
        return uri;
    }

    public String getFile() {
        return file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
