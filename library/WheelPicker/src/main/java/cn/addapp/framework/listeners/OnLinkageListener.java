package cn.addapp.framework.listeners;

import cn.addapp.framework.entity.ItemBean;

/**
 * Created by 涛 on 2017-03-14.
 *
 */

public interface OnLinkageListener {
    /**
     * 选择地址
     *
     * @param items1 the province
     * @param items2    the city
     * @param items3   the county ，if {@code hideCounty} is true，this is null
     */
    void onItemPicked(ItemBean items1, ItemBean items2, ItemBean items3);
}
