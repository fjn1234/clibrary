package view;

import android.util.SparseArray;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import interfaces.IObject;
import interfaces.IView;
import utils.ViewUtil;

public class ViewMapping implements IObject.IViewMapping {

    private HashMap<String, SparseArray<View>> bindViewMapList = new HashMap<>(4, 2);
    private HashMap<String, String> backupDataMap = new HashMap<>(30, 30);
    private Object obj;

    public ViewMapping(Object obj) {
        this.obj = obj;
    }

    @Override
    public void addMappingView(View view) {
        addMappingView(view.hashCode() + "", view);
    }

    @Override
    public void addMappingView(String tag, View view) {
        if (bindViewMapList.containsKey(tag)) return;
        SparseArray<View> viewMapList = ViewUtil.getViewMapEntityList(view, obj.getClass());
        bindViewMapList.put(tag, viewMapList);
    }

    @Override
    public void removeMappingView(View view) {
        removeMappingView(view.hashCode() + "");
    }

    @Override
    public void removeMappingView(String tag) {
        bindViewMapList.remove(tag);
    }

    @Override
    public void synchronizeObjectToView() {
        for (String k : bindViewMapList.keySet()) {
            synchronizeObjectToView(k);
        }
    }

    @Override
    public void synchronizeObjectToView(View view) {
        synchronizeObjectToView(view.hashCode() + "");
    }

    @Override
    public void synchronizeObjectToView(String tag) {
        if (!bindViewMapList.containsKey(tag)) return;
        fillObjectToView(bindViewMapList.get(tag));
    }

    @Override
    public void fillObjectToView(SparseArray<View> viewArr) {
        View view;
        Method method;
        IView.ICustomAttrs ICustomAttrs;
        IView.IMapping iMapping;
        Class[] classes = new Class[0];
        Object[] objects = new Object[]{};
        boolean visible, select;
        for (int i = 0; i < viewArr.size(); i++) {
            try {
                view = viewArr.get(viewArr.keyAt(i));
                if (view instanceof IView.ICustomAttrs) {
                    ICustomAttrs = (IView.ICustomAttrs) view;
                    if (ICustomAttrs.getCustomAttrs().hasVisibleMapping()) {
                        String mapping = ICustomAttrs.getCustomAttrs().getVisibleMapping();
                        boolean negate = false;
                        if (mapping.charAt(0) == '!') {
                            negate = true;
                            mapping = mapping.substring(1);
                        }
                        method = obj.getClass().getMethod(mapping, classes);
                        visible = (Boolean) method.invoke(obj, objects);
                        visible = negate ? !visible : visible;
                        if (visible) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            ViewUtil.setVisible(view, ICustomAttrs.getCustomAttrs().getHideMode());
                        }
                    }
                    if (ICustomAttrs.getCustomAttrs().hasSelectMapping()) {
                        String mapping = ICustomAttrs.getCustomAttrs().getSelectMapping();
                        boolean negate = false;
                        if (mapping.charAt(0) == '!') {
                            negate = true;
                            mapping = mapping.substring(1);
                        }
                        method = obj.getClass().getMethod(mapping, classes);
                        select = (Boolean) method.invoke(obj, objects);
                        select = negate ? !select : select;
                        view.setSelected(select);
                    }
                    if (view instanceof IView.IMapping) {
                        iMapping = (IView.IMapping) view;
                        if (ICustomAttrs.getCustomAttrs().hasGetMapping()) {
//                            System.out.println("++++++++++++++++" + ICustomAttrs.getCustomAttrs().getGetMapping());
                            method = obj.getClass().getMethod(ICustomAttrs.getCustomAttrs().getGetMapping(), classes);
                            iMapping.setMappingValue(method.invoke(obj, objects).toString());
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void fillObjectToView(View view) {
        SparseArray<View> viewMapList = ViewUtil.getViewMapEntityList(view, obj.getClass());
        fillObjectToView(viewMapList);
    }

    @Override
    public void synchronizeViewToObject(View view) {
        synchronizeViewToObject(view.hashCode() + "");
    }

    @Override
    public void synchronizeViewToObject(String tag) {
        if (!bindViewMapList.containsKey(tag)) return;
        fillViewToObject(bindViewMapList.get(tag));
    }

    @Override
    public void fillViewToObject(SparseArray<View> viewArr) {
        View view;
        Method method;
        IView.ICustomAttrs iCustomAttrs;
        IView.IMapping iMapping;
        for (int i = 0; i < viewArr.size(); i++) {
            try {
                view = viewArr.get(viewArr.keyAt(i));
                if (view instanceof IView.ICustomAttrs && view instanceof IView.IMapping) {
                    iCustomAttrs = (IView.ICustomAttrs) view;
                    iMapping = (IView.IMapping) view;
                    if (iCustomAttrs.getCustomAttrs().hasSetMapping()) {
                        method = obj.getClass().getMethod(iCustomAttrs.getCustomAttrs().getSetMapping(), String.class);
                        method.invoke(obj, iMapping.getMappingValue());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void fillViewToObject(View view) {
        SparseArray<View> viewMapList = ViewUtil.getViewMapEntityList(view, obj.getClass());
        fillViewToObject(viewMapList);
    }

    @Override
    public boolean backupObject() {
        try {
            HashMap<String, String> tempMap = new HashMap<>(30, 30);
            Field[] fields = this.obj.getClass().getFields();
            String fieldName;
            for (Field f : fields) {
                fieldName = f.getName();
                if (!Character.isLowerCase(fieldName.toCharArray()[0])) continue;
                if (f.getType().equals(String.class)) {
                    tempMap.put(fieldName, f.get(obj).toString());
                }
            }
            backupDataMap = tempMap;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean restoreObject() {
        try {
            Field field;
            for (String k : backupDataMap.keySet()) {
                field = obj.getClass().getField(k);
                field.set(obj, backupDataMap.get(k));
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public IObject.IViewMapping getViewMapping() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Field[] fields = this.obj.getClass().getFields();
        String fieldName;
        try {
            for (Field f : fields) {
                fieldName = f.getName();
                if (!Character.isLowerCase(fieldName.toCharArray()[0])) continue;
                if (f.getGenericType().toString().equals("class java.lang.String")) {
                    builder.append(fieldName + ":" + f.get(obj).toString() + "\n");
                }
            }
        } catch (Exception ex) {
        }
        return builder.toString();
    }
}
