package cn.addapp.androidpickers;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.addapp.androidpicker.R;
import cn.addapp.pickers.listeners.OnMoreWheelListener;
import cn.addapp.pickers.picker.CarNumberPicker;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelView;

/**
 * 内嵌选择器
 * <br />
 * Author:matt : addapp.cn
 * DateTime:2016/12/16 00:42
 *
 */
public class NextActivity extends BaseActivity {
    private CarNumberPicker picker;

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_nest);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        final TextView textView = findView(R.id.wheelview_tips);
        WheelView wheelView = findView(R.id.wheelview_single);
        wheelView.setItems(new String[]{"少数民族", "贵州穿青人", "不在56个少数民族之列", "第57个民族"}, 1);
        wheelView.setSelectedTextColor(0xFFFF00FF);
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFFF00FF);//线颜色
        config.setAlpha(100);//线透明度
        config.setRatio((float) (1.0 / 10.0));//线比率
        config.setThick(ConvertUtils.toPx(this, 10));//线粗
        wheelView.setLineConfig(config);
        wheelView.setOnWheelChangeListener(new WheelView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(boolean isUserScroll, int index, String item) {
                textView.setText("index=" + index + ",item=" + item);
            }
        });

        picker = new CarNumberPicker(this);
        picker.setOffset(3);
        picker.setOnMoreWheelListener(new OnMoreWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                textView.setText(item + ":" + picker.getSelectedSecondItem());

            }

            @Override
            public void onSecondWheeled(int index, String item) {
                textView.setText(picker.getSelectedFirstItem() + ":" + item);
            }

            @Override
            public void onThirdWheeled(int index, String item) {

            }
        } );
        ViewGroup viewGroup = findView(R.id.wheelview_container);
        viewGroup.addView(picker.getContentView());

        findViewById(R.id.nest_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.nest_carnumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show();
            }
        });
    }

}
