package interfaces;

import obj.CustomAttrs;

public class IView {

    public interface ICustomAttrs {
        void setCustomAttrs(CustomAttrs mAttrs);

        CustomAttrs getCustomAttrs();

        void loadCustomAttrs();

        void loadScreenArr();
    }

    public interface IMapping {
        void setMappingValue(String v);

        String getMappingValue();
    }

}
