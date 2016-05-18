package dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;


import com.hugh.clibrary.R;

import base.BaseDialog;
import entities.OptionEntity;
import view.CTextView;

/**
 * Created by Hugh on 2016/5/9.
 */
public class OptionDialog extends BaseDialog {

    private LinearLayout mLyoOption;

    public OptionDialog(Activity activity) {
        super(activity, R.layout.dia_options);
        mLyoOption = (LinearLayout) findViewById(R.id.lyo_dialog_options);
        mVwDialog.findViewById(R.id.btn_app_cancel).setOnClickListener(closeClickListener);
    }

    public void addOption(final OptionEntity option) {
        CTextView tv = (CTextView) LayoutInflater.from(activity).inflate(R.layout.cell_option, null);
        tv.loadScreenArr();
        tv.setText(option.getValue());
        mLyoOption.addView(tv, mLyoOption.getChildCount() - 1);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (optionListener != null)
                    optionListener.onOptionSelected(option);
            }
        });
    }

    public void removeAllOption(){
        mLyoOption.removeAllViews();
    }

    @Override
    public void show() {
        mLyoOption.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.app_dialog_option_in));
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.app_dialog_option_out);
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
        mLyoOption.startAnimation(animation);
    }

    private OptionListener optionListener;

    public void setOptionListener(OptionListener optionListener) {
        this.optionListener = optionListener;
    }

    public interface OptionListener {
        void onOptionSelected(OptionEntity optionEntity);
    }

}
