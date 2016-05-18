package base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import db.CacheDB;
import entities.CacheEntity;
import obj.CApplication;
import obj.CException;
import utils.SystemUtil;
import utils.ToastUtil;
import view.CFragment;

public abstract class BaseFragment extends CFragment {


    protected boolean loadingNet = false, startingActivity = false, cancelAction = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logi(this.getClass().getSimpleName());
        if (getContentId() < 0) return;
        initView();
        enableDefaultAnim();
    }

    private void initView() {
        // 隐藏软键盘
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    protected ViewGroup getFgmLayout() {
        return ((BaseActivity) getActivity()).getFgmLayout();
    }

    public void setMoveTaskToBack(boolean b) {
        ((BaseActivity) getActivity()).setMoveTaskToBack(b);
    }

    public void startFragmentActivity(Fragment fragment) {
        ((FragmentActivity) getActivity()).startFragmentActivity(fragment);
    }


    //---------------------------------------------------------------------------
    protected boolean checkNetConnect() {
        if (!SystemUtil.isConnect(getActivity())) {
            return false;
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        setBackPressedListener(null);
        cancelAction = true;
    }

    protected boolean hasOperateConflict() {
        return isLoadingNet() || isStartingActivity() || isCancelAction();
    }

    protected synchronized void setStartingFragment(boolean b) {
        this.startingActivity = b;
    }

    protected boolean isCancelAction() {
        return cancelAction;
    }

    protected boolean isLoadingNet() {
        return loadingNet;
    }

    protected boolean isStartingActivity() {
        return startingActivity;
    }

    protected void logi(String... msg) {
        for (String m : msg) {
            Log.e(this.getClass().getName(), m);
        }
    }

    public void setBackPressedListener(BaseActivity.OnBackPressedListener backPressedListener) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setBackPressedListener(backPressedListener);
    }

    public Context getAppContext() {
        return CApplication.getAppContext();
    }

    @Override
    public void finish() {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setBackPressedListener(null);
        super.finish();
    }

    public void finishActivity() {
        getActivity().finish();
    }

    //----------------------------------缓存--------------------------------

    private CacheEntity cacheEntity;

    protected boolean updateCache(String action, String responseJson) {
        return updateCache(action, responseJson, "");
    }

    protected boolean updateCache(String action, String responseJson, String upTime) {
        return updateCache(this.getClass().getSimpleName(), action, responseJson, upTime);
    }

    protected boolean updateCache(String activity, String action, String responseJson, String upTime) {
        boolean b;
        CacheDB db = new CacheDB(CApplication.getAppContext());
        b = db.update(activity, action, responseJson, upTime);
        if (b)
            reloadCache();
        db.close();
        return b;
    }

    protected boolean isCacheChange(String action, String responseJson) {
        CacheEntity.ActionEntity actionEntity = getCacheEntity().get(action);
        if (actionEntity != null && !actionEntity.isChange(responseJson))
            return false;
        else {
            logi("update");
            return updateCache(action, responseJson);
        }
    }

    protected void reloadCache() {
//        if (isLogin()) {
        CacheDB db = new CacheDB(CApplication.getAppContext());
        cacheEntity = db.getCache(this.getClass().getSimpleName());
        db.close();
//        }
    }

    public CacheEntity getCacheEntity() {
        if (cacheEntity == null)
            reloadCache();
        return cacheEntity;
    }


    //-------------------other----------------------------

    public void makeLongToast(String msg) {
        ToastUtil.makeLongSnackbar(contentView, msg);
    }

    public void makeShortToast(String msg) {
        ToastUtil.makeLongSnackbar(contentView, msg);
    }

    public void makeShortToast(int resId) {
        makeShortToast(getString(resId));
    }

    public void makeLongToast(int resId) {
        makeLongToast(getString(resId));
    }

    public void throwEx(Exception e) {
        e.printStackTrace();
        switch (CApplication.getDebugMode()) {
            case Debug:
                ToastUtil.makeShortToast(getAppContext(), "crash");
                break;
            case Test:
                throw new CException(e.getMessage());
            case Release:
//                MobclickHelper.reportError(e);
                break;
        }
    }
}
