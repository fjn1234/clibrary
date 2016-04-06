package obj;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CTabPagerAdapter extends FragmentPagerAdapter {

    private List<TabPagerItem> mlist;
    private Fragment reloadFragment = null;

    public CTabPagerAdapter(FragmentManager fm) {
        super(fm);
        mlist = new ArrayList<>();
    }

    public List<TabPagerItem> getList() {
        return mlist;
    }

    public void add(TabPagerItem item) {
        mlist.add(item);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mlist.get(position).getTabName();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position).getFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        Log.e("getItemPosition", "getItemPosition");
//        if (reloadFragment == null) {
//            return super.getItemPosition(object);
//        } else {
//            Log.e("reload", "reload");
//            reloadFragment = null;
            return POSITION_NONE;
//        }
    }

    public void reload(Fragment fragment) {
        reloadFragment = fragment;
    }

}
