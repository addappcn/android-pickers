package cn.addapp.androidpickers;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;

import cn.addapp.androidpicker.R;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnMoreItemPickListener;
import cn.addapp.pickers.listeners.OnMoreWheelListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.CarNumberPicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;

/**
 * 内嵌选择器
 * @author matt
 * blog: addapp.cn
 */
public class NextActivity extends BaseActivity {
    private CarNumberPicker picker;
    TextView textView;
    @Override
    protected View getContentView() {
        return inflateView(R.layout.activity_next);
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        textView = findView(R.id.wheelview_tips);

        ViewGroup viewGroup = findView(R.id.wheelview_single);
        viewGroup.addView(onSinglePicker());

        picker = new CarNumberPicker(this);
        picker.setWeightEnable(true);
        picker.setColumnWeight(0.5f,0.5f,1);
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
        ViewGroup viewGroup1 = findView(R.id.wheelview_container);
        viewGroup1.addView(picker.getContentView());

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


    public View onSinglePicker() {
//        String[] ss = (String[]) list.toArray();
        SinglePicker<String> picker = new SinglePicker<>(this, new String[]{"开封","郑州", "广州", "北京", "成都"});
        picker.setCanLoop(false);//不禁用循环
        picker.setLineVisible(true);
        picker.setLineColor(ContextCompat.getColor(this,R.color.material_green));
        picker.setTextSize(30);
        picker.setSelectedIndex(2);
        //启用权重 setWeightWidth 才起作用
//        picker.setLabel("分");
//        picker.setItemWidth(100);
//        picker.setWeightEnable(true);
//        picker.setWeightWidth(1);
        picker.setOuterLabelEnable(true);
        picker.setSelectedTextColor(ContextCompat.getColor(this,R.color.material_green));//前四位值是透明度
        picker.setUnSelectedTextColor(Color.BLACK);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {
                textView.setText("index=" + index + ", item=" + item);
//                ToastUtils.showShort("index=" + index + ", item=" + item);
            }
        });
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                textView.setText("index=" + index + ", item=" + item);
//                ToastUtils.showShort("index=" + index + ", item=" + item);
            }
        });
//        picker.show();
        return picker.getContentView();
    }
}
