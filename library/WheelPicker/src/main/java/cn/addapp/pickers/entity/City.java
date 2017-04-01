package cn.addapp.pickers.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 地市
 * <br/>
 * Author:matt : addapp.cn
 * DateTime:2016-10-15 19:07
 *
 */
public class City extends ItemBean {
    private String provinceId;
    private List<County> counties = new ArrayList<County>();

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public List<County> getCounties() {
        return counties;
    }

    public void setCounties(List<County> counties) {
        this.counties = counties;
    }

}