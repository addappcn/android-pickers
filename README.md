# 概要
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/addappcn/android-pickers)


安卓选择器类库，包括日期及时间选择器（可设置范围）、单项选择器（可用于性别、职业、学历、星座等）、城市地址选择器（分省级、地级及县级）、数字选择器（可用于年龄、身高、体重、温度等）等……
欢迎大伙儿在[Issues](https://github.com/addappcn/android-pickers/issues)提交你的意见或建议。
欢迎Fork & Pull requests贡献您的代码，大家共同学习【android-pickers交流群 : 456738690】。
[查看更新日志](https://github.com/addappcn/android-pickers/blob/master/ChangeLog.md)


# 安装
“app”是Sample；“android-pickers”是library 包括WheelPicker、SinglePicker、DatePicker、TimePicker、LinkagePicker、AddressPicker、NumberPicker、CarNumberPicker等。
#### demo下载
[点我](/demo/release/demo-release.apk)
#### 远程加载JitPack包
加载[![](https://jitpack.io/v/addappcn/android-pickers.svg)](https://jitpack.io/#addappcn/android-pickers)的仓库：
第一步，在项目根目录下的build.gradle里添加  `maven { url "https://jitpack.io" } `引用：
```
allprojects {
    repositories {
        maven { url "https://jitpack.io" }//必须添加这行
    }
}
```
第二步，在项目的app模块下的build.gradle里加：
```
dependencies {
    implementation  'com.github.addappcn:android-pickers:2.0.0'
}
```

# ProGuard
由于地址选择器使用了[fastjson](https://github.com/alibaba/fastjson)来解析，混淆时候需要加入以下类似的规则，不混淆Province、City等实体类。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.addapp.pickers.entity.** { *;}
```
# 注意事项
1. 联动设置对时间，地址选择器无效，加这个只是针对不需要联动的数据,如不满足你的开发需求 请自定义picker。

# Sample （具体用法详见demo项目）
继承自定义扩展选择器：
```java
       CustomPicker picker = new CustomPicker(this);
               picker.setOffset(1);//显示的条目的偏移量，条数为（offset*2+1）
               picker.setGravity(Gravity.CENTER);//居中
               picker.setOnItemPickListener(new OnItemPickListener<String>() {
                   @Override
                   public void onItemPicked(int position, String option) {
                       showToast("index=" + position + ", item=" + option);
                   }
               });
               picker.show();
```
内嵌视图选择器：
```java
        final TextView textView = findView(R.id.wheelview_tips);
                WheelListView wheelListView = findView(R.id.wheelview_single);
                wheelListView.setItems(new String[]{"少数民族", "贵州穿青人", "不在56个少数民族之列", "第57个民族"}, 1);
                wheelListView.setSelectedTextColor(0xFFFF00FF);
                LineConfig config = new LineConfig();
                config.setColor(Color.parseColor("#26A1B0"));//线颜色
                config.setAlpha(100);//线透明度
                config.setThick(ConvertUtils.toPx(this, 3));//线粗
                wheelListView.setLineConfig(config);
                wheelListView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
                    @Override
                    public void onItemSelected(boolean isUserScroll, int index, String item) {
                        textView.setText("index=" + index + ",item=" + item);
                    }
                });
                picker = new CarNumberPicker(this);
                picker.setWeightEnable(true);
                picker.setColumnWeight(0.5f,0.5f,1);
                picker.setWheelModeEnable(true);
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
                ViewGroup viewGroup = findView(R.id.wheelview_container);
                viewGroup.addView(picker.getContentView());
```
选择器各个设置项：
```java
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        OptionPicker picker = new OptionPicker(this,
                isChinese ? new String[]{
                        "水瓶", "双鱼", "白羊", "金牛", "双子", "巨蟹",
                        "狮子", "处女", "天秤", "天蝎", "射手", "摩羯"
                } : new String[]{
                        "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                        "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                });
        picker.setLabel(isChinese ? "座" : "");
        picker.setCycleDisable(true);//禁用循环
        picker.setLineConfig(config);
        picker.setTopHeight(50);//顶部标题栏高度
        picker.setTopLineColor(0xFF33B5E5);//顶部标题栏下划线颜色
        picker.setTopLineHeight(1);//顶部标题栏下划线高度
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);//顶部标题颜色
        picker.setTitleTextSize(12);//顶部标题文字大小
        picker.setCancelTextColor(0xFF33B5E5);//顶部取消按钮文字颜色
        picker.setCancelTextSize(14);
        picker.setSubmitTextColor(0xFF33B5E5);//顶部确定按钮文字颜色
        picker.setSubmitTextSize(14);
        picker.setTextColor(0xFFEE0000, 0xFF999999);//中间滚动项文字颜色
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFEE0000);//线颜色
        config.setAlpha(140);//线透明度
        picker.setLineConfig(config);
        picker.setBackgroundColor(0xFFE1E1E1);
        //picker.setSelectedItem(isChinese ? "射手" : "Sagittarius");
        picker.setSelectedIndex(10);//默认选中项
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                showToast("index=" + index + ", item=" + item);
            }
        });
        picker.show();
```

![效果图](/screenshots/Screenshot_2017-04-21-15-45-59.png)
![效果图](/screenshots/Screenshot_2017-04-21-15-46-11.png)
![效果图](/screenshots/Screenshot_2017-04-21-15-56-00.png)
![效果图](/screenshots/Screenshot_2017-04-21-15-56-22.png)
![效果图](/screenshots/Screenshot_2017-04-21-15-56-38.png)
![效果图](/screenshots/Screenshot_2017-04-21-15-56-50.png)

## License

    Copyright 2017 matt (android-pickers)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
