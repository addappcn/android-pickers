package cn.addapp.androidpickers;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.addapp.androidpicker.R;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.listeners.OnMoreWheelListener;
import cn.addapp.pickers.picker.CarNumberPicker;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 内嵌选择器
 * @author matt
 * blog: addapp.cn
 */
public class NextActivity extends BaseActivity {
    private CarNumberPicker picker;

    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_next);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        final TextView textView = findView(R.id.wheelview_tips);
        WheelListView wheelListView = findView(R.id.wheelview_single);
        wheelListView.setItems(new String[]{"少数民族", "贵州穿青人", "不在56个少数民族之列", "第57个民族"}, 1);
        wheelListView.setSelectedTextColor(0xFFFF00FF);
        LineConfig config = new LineConfig();
        config.setColor(Color.parseColor("#26A1B0"));//线颜色
        config.setAlpha(100);//线透明度
        config.setThick(ConvertUtils.toPx(this, 3));//线粗
        config.setShadowVisible(false);
        wheelListView.setLineConfig(config);
        wheelListView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(int index, String item) {
                textView.setText("index=" + index + ",item=" + item);
            }
        });

        picker = new CarNumberPicker(this);
        picker.setWeightEnable(true);
        picker.setColumnWeight(0.5f,0.5f,1);
        picker.setWheelModeEnable(true);
        picker.setTextSize(18);
        picker.setSelectedTextColor(0xFF279BAA);//前四位值是透明度
        picker.setUnSelectedTextColor(0xFF999999);
        picker.setCanLoop(true);
        picker.setOffset(3);
        picker.setOnMoreItemPickListener(new OnMoreItemPickListener<String>() {
            @Override
            public void onItemPicked(String s1, String s2, String s3) {
                s3 = !TextUtils.isEmpty(s3) ? ",item3: "+s3 : "";
                Toast.makeText(NextActivity.this, "item1: "+s1 +",item2: "+s2+ s3, Toast.LENGTH_SHORT).show();
            }
        });
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
