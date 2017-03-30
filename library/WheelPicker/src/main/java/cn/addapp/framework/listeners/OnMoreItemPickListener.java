package cn.addapp.framework.listeners;

/**
 * Created by 涛 on 2017-03-14.
 * 点击确认按钮选中item的回调
 */

public interface OnMoreItemPickListener<T> {
    void onItemPicked(T t1, T t2,T t3);
}
