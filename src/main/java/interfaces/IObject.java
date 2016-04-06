package interfaces;

import android.util.SparseArray;
import android.view.View;

public class IObject {

    public interface IViewMapping extends IMapping.IObjectMapping {

        void addMappingView(View view);

        void addMappingView(String tag, View view);

        void removeMappingView(View view);

        void removeMappingView(String tag);

        void synchronizeObjectToView();

        void synchronizeObjectToView(View view);

        void synchronizeObjectToView(String tag);

        void fillObjectToView(SparseArray<View> viewArr);

        void fillObjectToView(View view);

        void synchronizeViewToObject(View view);

        void synchronizeViewToObject(String tag);

        void fillViewToObject(SparseArray<View> viewArr);

        void fillViewToObject(View view);

        boolean backupObject();

        boolean restoreObject();

    }
}
