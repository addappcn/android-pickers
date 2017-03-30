package cn.addapp.framework.entity;

/**
 *
 * Author:matt : addapp.cn
 * DateTime:2016-10-15 19:06
 *
 */
public abstract class ItemBean extends JavaBean {
    private String itemId;
    private String itemName;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "itemId=" + itemId + ",itemName=" + itemName;
    }

}