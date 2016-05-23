package obj;

import android.content.Context;

import view.CPinnedListView;

public abstract class CPinnedAdapter<T extends CPinnedAdapter.CellPinned> extends CBaseAdapter<T> implements CPinnedListView.PinnedSectionListAdapter {

    public CPinnedAdapter(Context context,int convertViewId) {
        super(context,convertViewId);
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isPinned())
            return 1;
        else
            return 0;
    }

    public interface CellPinned {
        boolean isPinned();
    }
}
