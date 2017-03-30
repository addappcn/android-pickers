package cn.addapp.androidpickers;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import cn.addapp.androidpicker.R;
import cn.addapp.framework.listeners.OnSingleWheelListener;
import cn.addapp.framework.picker.SinglePicker;
import cn.addapp.framework.widget.WheelView;

/**
 * 自定义顶部及底部
 * <p>
 * Author:matt : addapp.cn
 *
 */
public class CustomPicker extends SinglePicker<String> implements OnSingleWheelListener {
    private TextView titleView;

    public CustomPicker(Activity activity) {
        super(activity, new String[]{
                "Java/Android", "PHP/MySQL", "HTML/CSS/JS", "C/C++", "Python"
        });
        setSelectedIndex(1);
        setLineConfig(new WheelView.LineConfig(0.06f));
        setOnSingleWheelListener(this);
    }

    @Override
    public void show() {
        super.show();
        ViewAnimator.animate(getRootView())
                .duration(2000)
                .interpolator(new AccelerateInterpolator())
                .slideBottom()
                .start();
    }

    @Override
    public void dismiss() {
        ViewAnimator.animate(getRootView())
                .duration(1000)
                .rollOut()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        CustomPicker.super.dismiss();
                    }
                })
                .start();
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

}
