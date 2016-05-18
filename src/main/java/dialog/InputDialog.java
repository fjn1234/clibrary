package dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;


import com.hugh.clibrary.R;

import base.BaseDialog;
import view.CButton;
import view.CEditText;
import view.CFragment;
import view.CLinearLayout;
import view.CTextView;

/**
 * Created by Hugh on 2016/5/9.
 */
public class InputDialog extends BaseDialog {

    private CTextView mTvTitle;
    private CEditText mEtInput;
    private CLinearLayout mLyoContent;
    private CButton mBtnCancel, mBtnConfirm;
    private CFragment fragment;

    public static InputDialog build(Activity activity, CFragment fragment, String title, String hint, String text, View.OnClickListener confirmClick) {
        InputDialog dialog = new InputDialog(activity, fragment);
        dialog.getTvTitle().setText(title);
        dialog.getEtInput().setHint(hint);
        dialog.getEtInput().setText(text);
        if (confirmClick != null) {
            dialog.getBtnConfirm().setOnClickListener(confirmClick);
        }
        dialog.fragment.addAutoCloseEditText(dialog.mEtInput);
        return dialog;
    }

    public InputDialog(Activity activity, CFragment fragment) {
        super(activity, R.layout.dia_input);
        mLyoContent = (CLinearLayout) findViewById(R.id.lyo_dialog_content);
        mLyoContent.loadCustomAttrs();
        mTvTitle = (CTextView) findViewById(R.id.tv_dialog_title);
        mEtInput = (CEditText) findViewById(R.id.et_dialog_input);
        mBtnCancel = (CButton) findViewById(R.id.btn_app_cancel);
        mBtnCancel.setOnClickListener(closeClickListener);
        mBtnConfirm = (CButton) findViewById(R.id.btn_app_confirm);
        mBtnConfirm.setOnClickListener(closeClickListener);
        mVwDialog.setOnClickListener(closeClickListener);
        this.fragment = fragment;
    }

    public InputDialog(Activity activity) {
        this(activity, null);
    }

    public void show() {
        mLyoContent.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.app_dialog_in));
        mEtInput.requestFocus();
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        super.show();
    }

    public void hide() {
        super.hide();
        if (fragment != null) fragment.closeSoftInput();
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.app_dialog_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hideNoAnim();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLyoContent.startAnimation(animation);
    }

    public CTextView getTvTitle() {
        return mTvTitle;
    }

    public CEditText getEtInput() {
        return mEtInput;
    }

    public CButton getBtnCancel() {
        return mBtnCancel;
    }

    public CButton getBtnConfirm() {
        return mBtnConfirm;
    }

}
