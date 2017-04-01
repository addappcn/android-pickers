# 概要
[![API 14+](https://img.shields.io/badge/API-14%2B-green.svg)](https://github.com/addappcn/AndroidPickers)


安卓选择器类库，包括日期及时间选择器（可设置范围）、单项选择器（可用于性别、职业、学历、星座等）、城市地址选择器（分省级、地级及县级）、数字选择器（可用于年龄、身高、体重、温度等）等……
欢迎大伙儿在[Issues](https://github.com/addappcn/AndroidPickers/issues)提交你的意见或建议。
欢迎Fork & Pull requests贡献您的代码，大家共同学习【[AndroidPickers交流群 : 456738690]】。
[查看更新日志](https://github.com/addappcn/AndroidPickers/blob/master/ChangeLog.md)，新版本可能未对旧版API作兼容处理，升级后若编译报错请根据错误提示更改。

# 安装
“app”是测试用例；“library”包括WheelPicker，
WheelPicker包括DatePicker、TimePicker、LinkagePicker、AddressPicker、NumberPicker、CarNumberPicker等。
#### ~~建议直接远程加载jcenter里的~~
latest.release表示使用最新版，也可以[参照此处指定具体的版本号](https://github.com/addappcn/AndroidPickers/releases)：
```groovy
dependencies {
    compile 'cn.addapp.framework:WheelPicker:版本号'
}
```
#### 若jcenter仓库里的无法下载的话，可换[JitPack](https://jitpack.io/#addappcn/AndroidPickers)的仓库试试：
第一步，在项目根目录下的build.gradle里加：
```
repositories {
    maven {
        url "https://www.jitpack.io"
    }
}
```
第二步，在项目的app模块下的build.gradle里加：
```
dependencies {
    compile 'com.github.addappcn.AndroidPickers:WheelPicker:版本号'
}
```

# ProGuard
由于地址选择器使用了[fastjson](https://github.com/alibaba/fastjson)来解析，混淆时候需要加入以下类似的规则，不混淆Province、City等实体类。
```
-keepattributes InnerClasses,Signature
-keepattributes *Annotation*

-keep class cn.addapp.framework.entity.** { *;}
```

# Sample （更多用法详见示例项目）
继承自定义扩展选择器：
```java
        CustomHeaderAndFooterPicker picker = new CustomHeaderAndFooterPicker(this);
        picker.setGravity(Gravity.CENTER);//居中
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                showToast(option);
            }
        });
        picker.show();
```
选择器内嵌到其他视图容器：
```java
        final CarNumberPicker picker = new CarNumberPicker(this);
        picker.setOnWheelListener(new CarNumberPicker.OnWheelListener() {
            @Override
            public void onFirstWheeled(int index, String item) {
                textView.setText(item + ":" + picker.getSelectedSecondItem());
            }

            @Override
            public void onSecondWheeled(int index, String item) {
                textView.setText(picker.getSelectedFirstItem() + ":" + item);
            }
        });
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.wheelview_container);
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


