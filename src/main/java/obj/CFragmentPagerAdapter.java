package obj;

import android.app.Fragment;
import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class CFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mlist;
    private Fragment reloadFragment = null;

    public CFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mlist = new ArrayList<>();
    }

    public List<Fragment> getList() {
        return mlist;
    }

    public void add(Fragment item) {
        mlist.add(item);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void reload(Fragment fragment) {
        reloadFragment = fragment;
    }

}
