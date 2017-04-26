package cn.addapp.pickers.listeners;

/**
 * Created by 涛 on 2017-03-14.
 * 点击确认按钮选中item的回调
 * @author matt
 * blog: addapp.cn
 */

public interface OnMoreItemPickListener<T> {
    void onItemPicked(T t1, T t2,T t3);
}
