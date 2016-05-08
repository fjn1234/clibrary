package base;

import android.app.Fragment;
import android.content.Intent;

/**
 * Created by Administrator on 2016/5/7.
 */
public class FragmentActivity extends BaseActivity {

    private Fragment fragment;

    @Override
    protected Fragment setFragment() {
        fragment = activity_fragment;
        activity_fragment = null;
        return fragment;
    }

    protected static Fragment activity_fragment;

    public void startFragmentActivity(Fragment fragment) {
        activity_fragment = fragment;
        startActivity(new Intent(this, FragmentActivity.class));
    }

}
