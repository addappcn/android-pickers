package cn.addapp.pickers.picker;

import android.app.Activity;
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

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.LoopView;
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
    private WheelView wheelView;
    private LoopView loopView;
    private OnSingleWheelListener onSingleWheelListener;
    private OnItemPickListener<T> onItemPickListener;
    private int selectedItemIndex = 0;
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
        if (null != wheelView) {
            wheelView.setItems(itemStrings, selectedItemIndex);
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
     * 设置选项的宽(dp)
     */
    public void setItemWidth(int itemWidth) {
        if (null != wheelView) {
            int width = ConvertUtils.toPx(activity, itemWidth);
            wheelView.setLayoutParams(new LinearLayout.LayoutParams(width, wheelView.getLayoutParams().height));
        } else {
            this.itemWidth = itemWidth;
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
//        layout.setWeightSum(1);
        //判断是选择ios滚轮模式还是普通模式
        if(iosModeEnable){
            loopView = new LoopView(activity);
            loopView.setInitPosition(selectedItemIndex);
            loopView.setCanLoop(canLoop);
            loopView.setLoopListener(new LoopView.LoopScrollListener() {
                @Override
                public void onItemSelect(int position,String item) {
                    selectedItemIndex = position;
                    if (onSingleWheelListener != null) {
                        onSingleWheelListener.onWheeled(selectedItemIndex, item);
                    }
                }
            });
            loopView.setTextSize(textSize);//must be called before setDateList
            loopView.setSelectedTextColor(textColorFocus);
            loopView.setUnSelectedTextColor(textColorNormal);
            loopView.setDataList(itemStrings);
            //按权重分配宽度
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, WRAP_CONTENT);
//            params.weight =0.5f;
            if (TextUtils.isEmpty(label)) {
                loopView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                layout.addView(loopView);
            } else {
                loopView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                layout.addView(loopView);
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextColor(textColorFocus);
                labelView.setTextSize(textSize);
                labelView.setText(label);
                layout.addView(labelView);
            }
            if (itemWidth != ITEM_WIDTH_UNKNOWN) {
                int width = ConvertUtils.toPx(activity, itemWidth);
                loopView.setLayoutParams(new LinearLayout.LayoutParams(width, loopView.getLayoutParams().height));
            }

        }else{
            wheelView = new WheelView(activity);
            wheelView.setTextSize(textSize);
            wheelView.setSelectedTextColor(textColorFocus);
            wheelView.setUnSelectedTextColor(textColorNormal);
            wheelView.setLineConfig(lineConfig);
            wheelView.setOffset(offset);
            wheelView.setCanLoop(canLoop);
            wheelView.setItems(itemStrings, selectedItemIndex);
            wheelView.setOnWheelChangeListener(new WheelView.OnWheelChangeListener(){
                @Override
                public void onItemSelected(boolean isUserScroll, int index, String item) {
                    selectedItemIndex = index;
                    if (onSingleWheelListener != null) {
                        onSingleWheelListener.onWheeled(selectedItemIndex, item);
                    }
                }
            });


            if (TextUtils.isEmpty(label)) {
                wheelView.setLayoutParams(new LinearLayout.LayoutParams(screenWidthPixels, WRAP_CONTENT));
                layout.addView(wheelView);
            } else {
                wheelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
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
            onItemPickListener.onItemPicked(selectedItemIndex, getSelectedItem());
        }
    }

    public T getSelectedItem() {
        return items.get(selectedItemIndex);
    }

    public int getSelectedIndex() {
        return selectedItemIndex;
    }

    public WheelView getWheelView() {
        return wheelView;
    }



}
