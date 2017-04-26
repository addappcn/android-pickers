package cn.addapp.pickers.common;

import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

/**
 * @author matt
 * blog: addapp.cn
 */
public  class LineConfig {
    public enum DividerType { // 分隔线类型
        FILL, WRAP
    }
    private static final int LINE_ALPHA = 220;
    private static final int LINE_COLOR = 0XFF83CDE6;
    private static final float LINE_THICK = 1f;//px
    private boolean visible = true;
    private boolean shadowVisible = false;
    private int color = LINE_COLOR;
    private int alpha = LINE_ALPHA;
    private float ratio = (float) (1.0 / 6.0);
    private float thick = LINE_THICK;// px
    private int width = 0;
    private int height = 0;
    private int itemHeight = 0;
    private int wheelSize = 0;

    public LineConfig() {
        super();
    }

    public LineConfig(@FloatRange(from = 0, to = 1) float ratio) {
        this.ratio = ratio;
    }

    /**
     * 线是否可见
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * 阴影是否可见
     */
    public void setShadowVisible(boolean shadowVisible) {
        this.shadowVisible = shadowVisible;
    }

    public boolean isShadowVisible() {
        return shadowVisible;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    /**
     * 线颜色
     */
    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    @IntRange(from = 1, to = 255)
    public int getAlpha() {
        return alpha;
    }

    /**
     * 线透明度
     */
    public void setAlpha(@IntRange(from = 1, to = 255) int alpha) {
        this.alpha = alpha;
    }

    @FloatRange(from = 0, to = 1)
    public float getRatio() {
        return ratio;
    }

    /**
     * 线比例，范围为0-1,0表示最长，1表示最短
     */
    public void setRatio(@FloatRange(from = 0, to = 1) float ratio) {
        this.ratio = ratio;
    }

    public float getThick() {
        return thick;
    }

    /**
     * 线粗
     */
    public void setThick(float thick) {
        this.thick = thick;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    @Override
    public String toString() {
        return "visible=" + visible + "color=" + color + ", alpha=" + alpha
                + ", thick=" + thick + ", width=" + width;
    }

}