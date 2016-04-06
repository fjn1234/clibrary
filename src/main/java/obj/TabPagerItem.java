package obj;

import android.app.Fragment;

/**
 * Created by Administrator on 2015/9/2.
 */
public class TabPagerItem {

    private String tabName;
    private Fragment fragment;

    public TabPagerItem(String tabName, Fragment fragment) {
        this.tabName = tabName;
        this.fragment = fragment;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
