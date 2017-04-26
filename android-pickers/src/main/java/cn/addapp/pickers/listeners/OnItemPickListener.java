package cn.addapp.pickers.listeners;

/**
 * Created by 涛 on 2017-03-14.
 * 点击确认按钮选中item的回调
 * @author matt
 * blog: addapp.cn
 */

public interface OnItemPickListener<T> {
    void onItemPicked(int index, T item);
}
