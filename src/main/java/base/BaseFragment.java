package base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;


import db.CacheDB;
import entities.CacheEntity;
import entities.NotifyUpdateEntity;
import obj.CApplication;
import obj.CException;
import utils.AnimUtil;
import utils.SystemUtil;
import utils.ToastUtil;
import view.CFragment;
import view.CImageView;
import view.CRelativeLayout;
import view.CTextView;

public abstract class BaseFragment extends CFragment {


    protected CRelativeLayout mLyoReturn, mLyoMenu;
    protected CTextView mTvTitle, mTvMsg, mBtnOperate;
    private CImageView mCivLoading;

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

    @Override
    public void onResume() {
        super.onResume();
        try {
            startingActivity = cancelAction = false;
            if (!checkNetConnect() && mTvMsg != null) {
                mTvMsg.setVisibility(View.VISIBLE);
                mTvMsg.setText("net can't connect");
            } else if (mTvMsg != null) {
                mTvMsg.setVisibility(View.GONE);
                mTvMsg.setText("");
            }
        } catch (Exception ex) {
            throwEx(ex);
        }
    }

    public synchronized void setLoadingNet(boolean loadingNet) {
        this.loadingNet = loadingNet;
        sendNotifyUpdate(this.getClass(),NOTIFY_NET_LOADING);
    }

    public void showLoading() {
        if (mCivLoading == null) return;
        mCivLoading.setVisibility(View.VISIBLE);
        Animation anim = AnimUtil.getLoopRotate(1000);
        mCivLoading.startAnimation(anim);
    }

    public void hideLoading() {
        if (mCivLoading == null) return;
        mCivLoading.clearAnimation();
        mCivLoading.setVisibility(View.GONE);
    }

    private View.OnClickListener btnReturnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener btnOperateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    protected void setTitle(String title) {
        if (mTvTitle != null)
            mTvTitle.setText(title);
    }

    protected ViewGroup getFgmLayout() {
        return ((BaseActivity) getActivity()).getFgmLayout();
    }

    public void setMoveTaskToBack(boolean b){
        ((BaseActivity) getActivity()).setMoveTaskToBack(b);
    }

    public void startFragmentActivity(Fragment fragment) {
        ((FragmentActivity) getActivity()).startFragmentActivity(fragment);
    }

    //-------------------------------------------------------------------
    private static final String NOTIFY_NET_LOADING="notify_net_loading";
    @Override
    protected void onNotifyUpdate(NotifyUpdateEntity notifyUpdateEntity) {
        super.onNotifyUpdate(notifyUpdateEntity);
        switch (notifyUpdateEntity.getNotifyTag()){
            case NOTIFY_NET_LOADING:
                if (loadingNet)
                    showLoading();
                else
                    hideLoading();
                break;
        }
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

    protected void showMenu() {
        if (mLyoMenu != null)
            mLyoMenu.setVisibility(View.VISIBLE);
    }

    public Context getAppContext(){
        return CApplication.getAppContext();
    }

    @Override
    public void finish() {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.setBackPressedListener(null);
        super.finish();
    }

    //----------------------------------加载block---------------------------
//    public Confirm_Block getConfirmBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getConfirmBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public AppUpdate_Block getAppUpdateBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getAppUpdateBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Edit_Block getEditBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getEditBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Info_Block getInfoBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getInfoBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Loading_Block getLoadingBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getLoadingBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Pay_Block getPayBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getPayBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Pay_PassWord_Block getPassWordBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getPassWordBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Upload_Block getUploadBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getUploadBlock();
//        else
//            throw new NullPointerException();
//    }
//
//    public Share_Block getShareBlock() {
//        if (contentView instanceof BaseRelativeLayout)
//            return ((BaseRelativeLayout) contentView).getShareBlock();
//        else
//            throw new NullPointerException();
//    }

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
        ToastUtil.MakeLongToast(CApplication.getAppContext(), msg);
    }

    public void makeShortToast(String msg) {
        ToastUtil.MakeShortToast(CApplication.getAppContext(), msg);
    }

    public void makeShortToast(int resId) {
        ToastUtil.MakeShortToast(CApplication.getAppContext(), resId);
    }

    public void makeLongToast(int resId) {
        ToastUtil.MakeLongToast(CApplication.getAppContext(), resId);
    }

    public void throwEx(Exception e) {
        e.printStackTrace();
        switch (CApplication.getDebugMode()) {
            case Debug:
                makeShortToast("crash");
                break;
            case Test:
                throw new CException(e.getMessage());
            case Release:
//                MobclickHelper.reportError(e);
                break;
        }
    }
}
