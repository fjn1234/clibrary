package base;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import utils.SystemUtil;
import utils.ToastUtil;
import view.CFragment;
import view.CRelativeLayout;

public abstract class BaseActivity extends Activity {
    private CRelativeLayout fgmLayout;
    private boolean loadingNet = false, startingActivity = false, cancelAction = false, loadingVisible = true;
    protected boolean moveTaskToBack = false;
    private static List<Activity> activityList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logi(this.getClass().getName());
        super.onCreate(savedInstanceState);
        init();
        setFragment(setFragment());
        activityList.add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetConnect();
        startingActivity = cancelAction = false;
    }

    protected boolean checkNetConnect() {
        if (!SystemUtil.isConnect(this)) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelAction = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }

    public void finishAllActivity(){
        for (Activity activity:activityList){
            activity.finish();
        }
        activityList.clear();
    }


    private void init() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        fgmLayout = new CRelativeLayout(this);
        fgmLayout.setLayoutParams(params);
        fgmLayout.setId(BaseFragment.ACTIVITY_ROOT_ID + 0);
        setContentView(fgmLayout);
    }

    private void setFragment(final Fragment fragment) {
        if (getFragmentManager() == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(BaseFragment.ACTIVITY_ROOT_ID + 0, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected abstract Fragment setFragment();

    public CRelativeLayout getFgmLayout() {
        return fgmLayout;
    }

    protected boolean hasOperateConflict() {
        return isLoadingNet() || isStartingActivity() || isCancelAction();
    }

    protected boolean isCancelAction() {
        return cancelAction;
    }


    protected boolean isLoadingNet() {
        return loadingNet;
    }

    protected synchronized void setStartingActivity(boolean b) {
        this.startingActivity = b;
    }

    protected boolean isStartingActivity() {
        return startingActivity;
    }

    protected void logi(String... msg) {
        for (String m : msg) {
            Log.e(this.getClass().getName(), m);
        }
    }

    public void setMoveTaskToBack(boolean moveTaskToBack) {
        this.moveTaskToBack = moveTaskToBack;
    }

    protected OnBackPressedListener backPressedListener;

    @Override
    public void onBackPressed() {
        if (CFragment.isStartingFragment()) return;
        if (backPressedListener != null)
            backPressedListener.onBackPressed();
        else {
            if (getFragmentManager().getBackStackEntryCount() == 0 && moveTaskToBack) {
                moveTaskToBack(true);
            } else {
                super.onBackPressed();
            }
        }
    }

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    public void setBackPressedListener(OnBackPressedListener backPressedListener) {
        this.backPressedListener = backPressedListener;
    }

    protected void makeLongToast(String msg) {
        ToastUtil.makeLongToast(this, msg);
    }

    protected void makeShortToast(String msg) {
        ToastUtil.makeShortToast(this, msg);
    }

    //------------------------ permission ----------------------------

    private OnRequestPermissionsResultListener onRequestPermissionsResultListener;

    public interface OnRequestPermissionsResultListener {
        void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }

    public void checkRequestPermissions(String[] permissions, int requestCode, OnRequestPermissionsResultListener requestPermissionsResultListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(permissions, requestCode);
                this.onRequestPermissionsResultListener = requestPermissionsResultListener;
            }
        } else {
            int[] grantResults = SystemUtil.getGrantResults(this, permissions);
            if (requestPermissionsResultListener != null)
                requestPermissionsResultListener.requestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (onRequestPermissionsResultListener != null)
            onRequestPermissionsResultListener.requestPermissionsResult(requestCode, permissions, grantResults);
    }


    //-------------------------- object params -------------------------------------------
//    private final static String OBJECT_PARAM = "object_param";
//    private final String PARAM_TAG = this.getClass().getName() + this.hashCode();
//    private HashMap<String, Object> paramsList = new HashMap<>(10);
//    private static HashMap<String, HashMap<String, Object>> activityParamsMap = new HashMap<>(20, 10);
//
//    public void putObjectParam(String tag, Object obj) {
//        paramsList.put(tag, obj);
//        activityParamsMap.put(PARAM_TAG, paramsList);
//    }
//
//    protected void removeObjectParam() {
//        String tag = getIntent().getStringExtra(OBJECT_PARAM);
//        if (TextUtils.isEmpty(tag)) return;
//        activityParamsMap.remove(tag);
//    }
//
//    protected Object getObjectParam(String tag) {
//        HashMap<String, Object> params = activityParamsMap.get(getIntent().getStringExtra(OBJECT_PARAM));
//        if (params == null) return null;
//        return params.get(tag);
//    }
//
//    @Override
//    public void startActivity(Intent intent) {
//        intent.putExtra(OBJECT_PARAM, PARAM_TAG);
//        super.startActivity(intent);
//    }

}
