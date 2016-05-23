package base;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import interfaces.IView;
import obj.CApplication;
import utils.ViewUtil;

/**
 * Created by Hugh on 2016/5/10.
 */
public abstract class BaseDialog {

    protected FrameLayout mVwRoot;
    protected ImageView mIvBg;
    protected View mVwDialog;
    protected Activity activity;

    protected BaseDialog(final Activity activity, int resId) {
        mVwDialog = LayoutInflater.from(activity).inflate(resId, null);
        ((IView.ICustomAttrs)mVwDialog).loadScreenArr();
        ViewUtil.loadSubViewCustomAttrs(mVwDialog);
        mVwDialog.setOnClickListener(closeClickListener);
        this.activity = activity;
    }

    protected View.OnClickListener closeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hide();
        }
    };

    public void show() {
        if (mVwRoot == null)
            mVwRoot = (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mVwRoot.removeView(mVwDialog);
        mVwRoot.addView(mVwDialog);
        if (activity instanceof base.BaseActivity) {
            ((base.BaseActivity) activity).setBackPressedListener(new base.BaseActivity.OnBackPressedListener() {
                @Override
                public void onBackPressed() {
                    hide();
                    ((base.BaseActivity) activity).setBackPressedListener(null);
                }
            });
        }
    }

    public void hide() {
    }

    public void hideNoAnim() {
        mVwRoot.removeView(mVwDialog);
    }

    protected Context getAppContext(){
        return CApplication.getAppContext();
    }

    protected View findViewById(int id){
        return mVwDialog.findViewById(id);
    }

    public ImageView getIvBg() {
        return mIvBg;
    }
}
