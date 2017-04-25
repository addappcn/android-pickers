package cn.addapp.pickers.picker;

import android.app.Activity;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.addapp.pickers.adapter.ArrayWheelAdapter;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelListView;
import cn.addapp.pickers.widget.WheelView;

/**
 * 单项选择器
 *
 * @author matt : addapp.cn
 * @since 2015/9/29
 */
public class SinglePicker<T> extends WheelPicker {
    private static final int ITEM_WIDTH_UNKNOWN = -99;
    private List<T> items = new ArrayList<>();
    private List<String> itemStrings = new ArrayList<>();
    private WheelListView wheelListView;
    private WheelView wheelView ;
    private float weightWidth = 0.0f;
    private OnSingleWheelListener onSingleWheelListener;
    private OnItemPickListener<T> onItemPickListener;
    private int selectedItemIndex = 0;
    private String selectedItem = "";
    private String label = "";
    private int itemWidth = ITEM_WIDTH_UNKNOWN;
    public SinglePicker(Activity activity, T[] items) {
        this(activity, Arrays.asList(items));
    }

    public SinglePicker(Activity activity, List<T> items) {
        super(activity);
        setItems(items);
    }

    /**
     * 添加数据项
     */
    public void addItem(T item) {
        items.add(item);
        itemStrings.add(formatToString(item));
    }
    /**
     * 移除数据项
     */
    public void removeItem(T item) {
        items.remove(item);
        itemStrings.remove(formatToString(item));
    }

    /**
     * 设置数据项
     */
    public void setItems(T[] items) {
        setItems(Arrays.asList(items));
    }

    /**
     * 设置数据项
     */
    public void setItems(List<T> items) {
        if (null == items || items.size() == 0) {
            return;
        }
        this.items = items;
        for (T item : items) {
            itemStrings.add(formatToString(item));
        }
        if(wheelModeEnable){
            if (null != wheelView) {
                wheelView.setAdapter(new ArrayWheelAdapter<>(itemStrings));
                wheelView.setCurrentItem(selectedItemIndex);
            }
        }else{
            if (null != wheelListView) {
                wheelListView.setItems(itemStrings, selectedItemIndex);
            }
        }

    }

    /**
     * 设置显示的单位，如身高为cm、体重为kg
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * 设置默认选中的项的索引
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < items.size()) {
            selectedItemIndex = index;
        }
    }

    /**
     * 设置默认选中的项
     */
    public void setSelectedItem(@NonNull T item) {
        setSelectedIndex(itemStrings.indexOf(formatToString(item)));
    }
    /**
     * 设置view的权重，总权重数为1 ,weightWidth范围（0.0f-1.0f）
     * */
    public void setWeightWidth(@FloatRange(from = 0, to = 1)float weightWidth) {
        if(weightWidth<0){
            weightWidth = 0;
        }
        if(!TextUtils.isEmpty(label)){
            if(weightWidth>=1){
                weightWidth = 0.5f;
            }
        }
        this.weightWidth = weightWidth;
    }
    /**
     * 设置选项的宽(dp)
     */
    public void setItemWidth(int itemWidth) {
        if(wheelModeEnable){
            if (null != wheelView) {
                int width = ConvertUtils.toPx(activity, itemWidth);
                wheelView.setLayoutParams(new LinearLayout.LayoutParams(width, wheelListView.getLayoutParams().height));
            }else{
                this.itemWidth = itemWidth;
            }
        }
        else {
            if (null != wheelListView) {
                int width = ConvertUtils.toPx(activity, itemWidth);
                wheelListView.setLayoutParams(new LinearLayout.LayoutParams(width, wheelListView.getLayoutParams().height));
            }else{
                this.itemWidth = itemWidth;
            }
        }
    }

    /**
     * 设置滑动监听器
     */
    public void setOnSingleWheelListener(OnSingleWheelListener onSingleWheelListener) {
        this.onSingleWheelListener = onSingleWheelListener;
    }

    public void setOnItemPickListener(OnItemPickListener<T> listener) {
        this.onItemPickListener = listener;
    }

    @Override
    @NonNull
    protected View makeCenterView() {
        if (items.size() == 0) {
            throw new IllegalArgumentException("please initial items at first, can't be empty");
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams wheelParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        if(weightEnable){
            layout.setWeightSum(1);
            //按权重分配宽度
            wheelParams.weight = weightWidth;
        }

        //判断是选择ios滚轮模式还是普通模式
        if(wheelModeEnable){
            wheelView = new WheelView(activity);
            wheelView.setAdapter(new ArrayWheelAdapter<>(itemStrings));
            wheelView.setCurrentItem(selectedItemIndex);
            wheelView.setCanLoop(canLoop);
            wheelView.setLineConfig(lineConfig);
            wheelView.setDividerType(LineConfig.DividerType.FILL);
            wheelView.setOnItemPickListener(new OnItemPickListener<String>() {
                @Override
                public void onItemPicked(int i,String item) {
                    selectedItem = item;
                    selectedItemIndex = i;
                    if (onSingleWheelListener != null) {
                        onSingleWheelListener.onWheeled(selectedItemIndex, selectedItem);
                    }
                }
            });
            if (TextUtils.isEmpty(label)) {
                wheelView.setLayoutParams(wheelParams);
                layout.addView(wheelView);
            } else {
                wheelView.setLayoutParams(wheelParams);
                layout.addView(wheelView);
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(label);
                layout.addView(labelView);

            }
            if (itemWidth != ITEM_WIDTH_UNKNOWN) {
                int width = ConvertUtils.toPx(activity, itemWidth);
                wheelView.setLayoutParams(new LinearLayout.LayoutParams(width, wheelView.getLayoutParams().height));
            }

        }else{
            wheelListView = new WheelListView(activity);
            wheelListView.setTextSize(textSize);
            wheelListView.setSelectedTextColor(textColorFocus);
            wheelListView.setUnSelectedTextColor(textColorNormal);
            wheelListView.setLineConfig(lineConfig);
            wheelListView.setOffset(offset);
            wheelListView.setCanLoop(canLoop);
            wheelListView.setItems(itemStrings, selectedItemIndex);
            wheelListView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener(){
                @Override
                public void onItemSelected(boolean isUserScroll, int index, String item) {
                    selectedItemIndex = index;
                    if (onSingleWheelListener != null) {
                        onSingleWheelListener.onWheeled(selectedItemIndex, item);
                    }
                }
            });


            if (TextUtils.isEmpty(label)) {
                wheelListView.setLayoutParams(wheelParams);
                layout.addView(wheelListView);
            } else {
                wheelListView.setLayoutParams(wheelParams);
                layout.addView(wheelListView);
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(label);
                layout.addView(labelView);
            }
            if (itemWidth != ITEM_WIDTH_UNKNOWN) {
                int width = ConvertUtils.toPx(activity, itemWidth);
                wheelListView.setLayoutParams(new LinearLayout.LayoutParams(width, wheelListView.getLayoutParams().height));
            }
        }

        return layout;
    }

    private String formatToString(T item) {
        if (item instanceof Float || item instanceof Double) {
            return new DecimalFormat("0.00").format(item);
        }
        return item.toString();
    }

    @Override
    public void onSubmit() {
        if (onItemPickListener != null) {
            onItemPickListener.onItemPicked(getSelectedIndex(), getSelectedItem());
        }
    }

    private  T getSelectedItem() {
        return items.get(selectedItemIndex);
    }

    public int getSelectedIndex() {
        return selectedItemIndex;
    }

//    public View getWheelListView() {
//        return wheelListView;
//    }



}
