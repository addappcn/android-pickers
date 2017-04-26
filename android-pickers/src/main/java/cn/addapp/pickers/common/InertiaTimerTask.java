package cn.addapp.pickers.common;


import java.util.TimerTask;

import cn.addapp.pickers.widget.WheelView;

/**
 * @author matt
 * blog: addapp.cn
 * @since 2015/9/29
 */
final public class InertiaTimerTask extends TimerTask {

    float a;
    final float velocityY;
    final WheelView wheelView;

    public InertiaTimerTask(WheelView wheelView, float velocityY) {
        super();
        this.wheelView = wheelView;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            wheelView.cancelFuture();
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        wheelView.totalScrollY = wheelView.totalScrollY - i;
        if (!wheelView.isLoop) {
            float itemHeight = wheelView.itemHeight;
            float top = (-wheelView.initPosition) * itemHeight;
            float bottom = (wheelView.getItemsCount() - 1 - wheelView.initPosition) * itemHeight;
            if(wheelView.totalScrollY - itemHeight*0.25 < top){
                top = wheelView.totalScrollY + i;
            }
            else if(wheelView.totalScrollY + itemHeight*0.25 > bottom){
                bottom = wheelView.totalScrollY + i;
            }

            if (wheelView.totalScrollY <= top){
                a = 40F;
                wheelView.totalScrollY = (int)top;
            } else if (wheelView.totalScrollY >= bottom) {
                wheelView.totalScrollY = (int)bottom;
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }

}
