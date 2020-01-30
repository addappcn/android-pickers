package cn.addapp.androidpickers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import cn.addapp.androidpicker.R;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * 自定义顶部及底部
 * @author matt
 * blog: addapp.cn
 */
public class CustomPicker extends SinglePicker<String> implements OnSingleWheelListener {
    private TextView titleView;

    public CustomPicker(Activity activity) {
        super(activity, new String[]{
                "Java/Android", "PHP/MySQL", "HTML/CSS/JS", "C/C++"
        });
        setSelectedIndex(1);
        setLineConfig(new LineConfig(0.06f));
        setOnSingleWheelListener(this);
    }

    @Override
    protected void showAfter() {
        View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(rootView, "alpha", 0, 1);
        ObjectAnimator translation = ObjectAnimator.ofFloat(rootView, "translationY", 300, 0);
        animatorSet.playTogether(alpha, translation);
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();
    }

    @Override
    public void dismiss() {
        View rootView = getRootView();
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(rootView, "alpha", 1, 0);
        ObjectAnimator translation = ObjectAnimator.ofFloat(rootView, "translationX", 0, rootView.getWidth());
        ObjectAnimator rotation = ObjectAnimator.ofFloat(rootView, "rotation", 0, 120);
        animatorSet.playTogether(alpha, translation, rotation);
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_header, null);
        titleView = (TextView) view.findViewById(R.id.picker_title);
        titleView.setText(titleText);
        view.findViewById(R.id.picker_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.picker_footer, null);
        Button submitView = (Button) view.findViewById(R.id.picker_submit);
        submitView.setText(submitText);
        submitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSubmit();
            }
        });
        Button cancelView = (Button) view.findViewById(R.id.picker_cancel);
        cancelView.setText(cancelText);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onCancel();
            }
        });
        return view;
    }

    @Override
    public void onWheeled(int index, String item) {
        if (titleView != null) {
            titleView.setText(item);
        }
    }

    @Override
    public void onSubmit() {
        super.onSubmit();
    }

    @Override
    protected void onCancel() {
        super.onCancel();
    }
}
