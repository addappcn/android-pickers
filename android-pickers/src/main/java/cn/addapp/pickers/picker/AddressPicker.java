package cn.addapp.pickers.picker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.adapter.ArrayWheelAdapter;
import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnLinkageListener;
import cn.addapp.pickers.listeners.OnMoreWheelListener;
import cn.addapp.pickers.util.LogUtils;
import cn.addapp.pickers.widget.WheelListView;
import cn.addapp.pickers.widget.WheelView;

/**
 * 地址选择器（包括省级、地级、县级），地址数据见示例项目assets目录下。
 * @author matt
 * blog: addapp.cn
 * @see Province
 * @see City
 * @see County
 */
public class AddressPicker extends LinkagePicker {
    private OnLinkageListener onLinkageListener;
    private OnMoreWheelListener onMoreWheelListener;
    //只显示地市及区县
    private boolean hideProvince = false;
    //只显示省份及地市
    private boolean hideCounty = false;
    //省市区数据
    private ArrayList<Province> provinces = new ArrayList<Province>();

    public AddressPicker(Activity activity, ArrayList<Province> provinces) {
        super(activity, new AddressProvider(provinces));
        this.provinces = provinces;
    }

    /**
     * 设置默认选中的省市县
     */
    public void setSelectedItem(String province, String city, String county) {
        super.setSelectedItem(province, city, county);
    }

    public Province getSelectedProvince() {
        return provinces.get(selectedFirstIndex);
    }

    public City getSelectedCity() {
        return getSelectedProvince().getCities().get(selectedSecondIndex);
    }

    public County getSelectedCounty() {
        return getSelectedCity().getCounties().get(selectedThirdIndex);
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见示例中的“assets/city2.json”
     */
    public void setHideProvince(boolean hideProvince) {
        this.hideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用“assets/city.json” 仅在逻辑上隐藏县级选择框，实际项目中应该去掉县级数据。
     */
    public void setHideCounty(boolean hideCounty) {
        this.hideCounty = hideCounty;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnMoreWheelListener(OnMoreWheelListener onMoreWheelListener) {
        this.onMoreWheelListener = onMoreWheelListener;
    }

    public void setOnLinkageListener(OnLinkageListener listener) {
        this.onLinkageListener = listener;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
//        super.makeCenterView();
        if (null == provider) {
            throw new IllegalArgumentException("please set address provider before make view");
        }
        if (hideCounty) {
            hideProvince = false;
        }
        int[] widths = getColumnWidths(hideProvince || hideCounty);
        int provinceWidth = widths[0];
        int cityWidth = widths[1];
        int countyWidth = widths[2];
        if (hideProvince) {
            provinceWidth = 0;
            cityWidth = widths[0];
            countyWidth = widths[1];
        }
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        //判断是选择ios滚轮模式还是普通模式
        if(wheelModeEnable){
            final WheelView provinceView = new WheelView(activity);
            provinceView.setCanLoop(canLoop);
            provinceView.setLayoutParams(new LinearLayout.LayoutParams(provinceWidth, WRAP_CONTENT));
            provinceView.setTextSize(textSize);
            provinceView.setSelectedTextColor(textColorFocus);
            provinceView.setUnSelectedTextColor(textColorNormal);
            provinceView.setLineConfig(lineConfig);
            provinceView.setAdapter(new ArrayWheelAdapter<>(provider.provideFirstData()));
            provinceView.setCurrentItem(selectedFirstIndex);
            if (hideProvince) {
                provinceView.setVisibility(View.GONE);
            }
            layout.addView(provinceView);

            final WheelView cityView = new WheelView(activity);
            cityView.setCanLoop(canLoop);
            cityView.setTextSize(textSize);
            cityView.setSelectedTextColor(textColorFocus);
            cityView.setUnSelectedTextColor(textColorNormal);
            cityView.setLineConfig(lineConfig);
            cityView.setAdapter(new ArrayWheelAdapter<>(provider.provideSecondData(selectedFirstIndex)));
            cityView.setCurrentItem(selectedSecondIndex);
            cityView.setLayoutParams(new LinearLayout.LayoutParams(cityWidth, WRAP_CONTENT));

            layout.addView(cityView);

            final WheelView countyView = new WheelView(activity);
            countyView.setCanLoop(canLoop);
            countyView.setTextSize(textSize);
            countyView.setSelectedTextColor(textColorFocus);
            countyView.setUnSelectedTextColor(textColorNormal);
            countyView.setLineConfig(lineConfig);
            countyView.setAdapter(new ArrayWheelAdapter<>(provider.provideThirdData(selectedFirstIndex, selectedSecondIndex)));
            countyView.setCurrentItem(selectedThirdIndex);
            countyView.setLayoutParams(new LinearLayout.LayoutParams(countyWidth, WRAP_CONTENT));
            if (hideCounty) {
                countyView.setVisibility(View.GONE);
            }
            layout.addView(countyView);
            provinceView.setOnItemPickListener(new OnItemPickListener<String>() {
                @Override
                public void onItemPicked(int index, String item) {
                    selectedFirstItem = item;
                    selectedFirstIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onFirstWheeled(selectedFirstIndex, selectedFirstItem);
                    }
//                    if (!canLinkage) {
//                        return;
//                    }
                    LogUtils.verbose(this, "change cities after province wheeled");
                    selectedSecondIndex = 0;//重置地级索引
                    selectedThirdIndex = 0;//重置县级索引
                    //根据省份获取地市
                    List<String> cities = provider.provideSecondData(selectedFirstIndex);
                    if (cities.size() > 0) {
                        cityView.setAdapter(new ArrayWheelAdapter<>(cities));
                        cityView.setCurrentItem(selectedSecondIndex);
                    } else {
                        cityView.setAdapter(new ArrayWheelAdapter<>(new ArrayList<String>()));
                    }
                    //根据地市获取区县
                    List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                    if (counties.size() > 0) {
                        countyView.setAdapter(new ArrayWheelAdapter<>(counties));
                        countyView.setCurrentItem(selectedThirdIndex);
                    } else {
                        countyView.setAdapter(new ArrayWheelAdapter<>(new ArrayList<String>()));
                    }
                }
            });

            cityView.setOnItemPickListener(new OnItemPickListener<String>() {
                @Override
                public void onItemPicked( int index, String item) {
                    selectedSecondItem = item;
                    selectedSecondIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onSecondWheeled(selectedSecondIndex, selectedSecondItem);
                    }
//                    if (!canLinkage) {
//                        return;
//                    }
                    LogUtils.verbose(this, "change counties after city wheeled");
                    selectedThirdIndex = 0;//重置县级索引
                    //根据地市获取区县
                    List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                    if (counties.size() > 0) {
                        //若不是用户手动滚动，说明联动需要指定默认项
                        countyView.setAdapter(new ArrayWheelAdapter<>(counties));
                        countyView.setCurrentItem(selectedThirdIndex);
                    } else {
                        countyView.setAdapter(new ArrayWheelAdapter<>(new ArrayList<String>()));
                    }
                }
            });
            countyView.setOnItemPickListener(new OnItemPickListener<String>() {
                @Override
                public void onItemPicked( int index, String item) {
                    selectedThirdItem = item;
                    selectedThirdIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onThirdWheeled(selectedThirdIndex, selectedThirdItem);
                    }
                }
            });
        }else{
            final WheelListView provinceView = new WheelListView(activity);
            provinceView.setCanLoop(canLoop);
            provinceView.setLayoutParams(new LinearLayout.LayoutParams(provinceWidth, WRAP_CONTENT));
            provinceView.setTextSize(textSize);
            provinceView.setSelectedTextColor(textColorFocus);
            provinceView.setUnSelectedTextColor(textColorNormal);
            provinceView.setLineConfig(lineConfig);
            provinceView.setOffset(offset);
            layout.addView(provinceView);
            if (hideProvince) {
                provinceView.setVisibility(View.GONE);
            }

            final WheelListView cityView = new WheelListView(activity);
            cityView.setCanLoop(canLoop);
            cityView.setLayoutParams(new LinearLayout.LayoutParams(cityWidth, WRAP_CONTENT));
            cityView.setTextSize(textSize);
            cityView.setSelectedTextColor(textColorFocus);
            cityView.setUnSelectedTextColor(textColorNormal);
            cityView.setLineConfig(lineConfig);
            cityView.setOffset(offset);
            layout.addView(cityView);

            final WheelListView countyView = new WheelListView(activity);
            countyView.setCanLoop(canLoop);
            countyView.setLayoutParams(new LinearLayout.LayoutParams(countyWidth, WRAP_CONTENT));
            countyView.setTextSize(textSize);
            countyView.setSelectedTextColor(textColorFocus);
            countyView.setUnSelectedTextColor(textColorNormal);
            countyView.setLineConfig(lineConfig);
            countyView.setOffset(offset);
            layout.addView(countyView);
            if (hideCounty) {
                countyView.setVisibility(View.GONE);
            }

            provinceView.setItems(provider.provideFirstData(), selectedFirstIndex);
            provinceView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                @Override
                public void onItemSelected(int index, String item) {
                    selectedFirstItem = item;
                    selectedFirstIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onFirstWheeled(selectedFirstIndex, selectedFirstItem);
                    }
//                    if (!canLinkage) {
//                        return;
//                    }
                    LogUtils.verbose(this, "change cities after province wheeled");
                    selectedSecondIndex = 0;//重置地级索引
                    selectedThirdIndex = 0;//重置县级索引
                    //根据省份获取地市
                    List<String> cities = provider.provideSecondData(selectedFirstIndex);
                    if (cities.size() > 0) {
                        cityView.setItems(cities, selectedSecondIndex);
                    } else {
                        cityView.setItems(new ArrayList<String>());
                    }
                    //根据地市获取区县
                    List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                    if (counties.size() > 0) {
                        countyView.setItems(counties, selectedThirdIndex);
                    } else {
                        countyView.setItems(new ArrayList<String>());
                    }
                }
            });

            cityView.setItems(provider.provideSecondData(selectedFirstIndex), selectedSecondIndex);
            cityView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                @Override
                public void onItemSelected( int index, String item) {
                    selectedSecondItem = item;
                    selectedSecondIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onSecondWheeled(selectedSecondIndex, selectedSecondItem);
                    }
//                    if (!canLinkage) {
//                        return;
//                    }
                    LogUtils.verbose(this, "change counties after city wheeled");
                    selectedThirdIndex = 0;//重置县级索引
                    //根据地市获取区县
                    List<String> counties = provider.provideThirdData(selectedFirstIndex, selectedSecondIndex);
                    if (counties.size() > 0) {
                        //若不是用户手动滚动，说明联动需要指定默认项
                        countyView.setItems(counties, selectedThirdIndex);
                    } else {
                        countyView.setItems(new ArrayList<String>());
                    }
                }
            });

            countyView.setItems(provider.provideThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
            countyView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                @Override
                public void onItemSelected( int index, String item) {
                    selectedThirdItem = item;
                    selectedThirdIndex = index;
                    if (onMoreWheelListener != null) {
                        onMoreWheelListener.onThirdWheeled(selectedThirdIndex, selectedThirdItem);
                    }
                }
            });
        }

        return layout;
    }

    @Override
    public void onSubmit() {
        if (onLinkageListener != null) {
            Province province = getSelectedProvince();
            City city = getSelectedCity();
            County county = null;
            if (!hideCounty) {
                county = getSelectedCounty();
            }
            onLinkageListener.onAddressPicked(province, city, county);
        }
    }


    /**
     * 地址提供者
     */
    public static class AddressProvider implements DataProvider {
        private List<String> firstList = new ArrayList<>();
        private List<List<String>> secondList = new ArrayList<>();
        private List<List<List<String>>> thirdList = new ArrayList<>();

        public AddressProvider(List<Province> provinces) {
            parseData(provinces);
        }

        @Override
        public boolean isOnlyTwo() {
            return thirdList.size() == 0;
        }

        @Override
        public List<String> provideFirstData() {
            return firstList;
        }

        @Override
        public List<String> provideSecondData(int firstIndex) {
            return secondList.get(firstIndex);
        }

        @Override
        public List<String> provideThirdData(int firstIndex, int secondIndex) {
            return thirdList.get(firstIndex).get(secondIndex);
        }

        private void parseData(List<Province> data) {
            int provinceSize = data.size();
            //添加省
            for (int x = 0; x < provinceSize; x++) {
                Province pro = data.get(x);
                firstList.add(pro.getAreaName());
                List<City> cities = pro.getCities();
                List<String> xCities = new ArrayList<>();
                List<List<String>> xCounties = new ArrayList<>();
                int citySize = cities.size();
                //添加地市
                for (int y = 0; y < citySize; y++) {
                    City cit = cities.get(y);
                    cit.setProvinceId(pro.getAreaId());
                    xCities.add(cit.getAreaName());
                    List<County> counties = cit.getCounties();
                    ArrayList<String> yCounties = new ArrayList<>();
                    int countySize = counties.size();
                    //添加区县
                    if (countySize == 0) {
                        yCounties.add(cit.getAreaName());
                    } else {
                        for (int z = 0; z < countySize; z++) {
                            County cou = counties.get(z);
                            cou.setCityId(cit.getAreaId());
                            yCounties.add(cou.getAreaName());
                        }
                    }
                    xCounties.add(yCounties);
                }
                secondList.add(xCities);
                thirdList.add(xCounties);
            }
        }

    }

}
