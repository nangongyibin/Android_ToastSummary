package com.ngyb.toastsummary;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ngyb.toastsummary.constant.Constant;
import com.ngyb.utils.SharedPreferencesUtils;

/**
 * 作者：南宫燚滨
 * 描述：
 * 邮箱：nangongyibin@gmail.com
 * 日期：2019/11/10 16:41
 */
public class CustomerToast {
    private Context ctx;
    private final SharedPreferencesUtils sp;
    private final WindowManager wm;
    private View viewToast;
    private final WindowManager.LayoutParams params;

    public CustomerToast(Context ctx) {
        this.ctx = ctx;
        sp = new SharedPreferencesUtils(ctx);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
    }

    public void showToast(String address) {
        int drawableId = sp.getInt(Constant.TOAST_STYLE_COLOR, R.drawable.shape_alpha_style);
        viewToast = View.inflate(ctx, R.layout.layout_toast, null);
        viewToast.setBackgroundResource(drawableId);
        TextView tvPhoneAddress = viewToast.findViewById(R.id.tv_phone_address);
        tvPhoneAddress.setText(address);
        wm.addView(viewToast, params);
        viewToast.setOnTouchListener(new View.OnTouchListener() {
            private int startX;
            private int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int disX = moveX - startX;
                        int disY = moveY - startY;
                        params.x += disX;
                        params.y += disY;
                        wm.updateViewLayout(viewToast, params);
                        startY = moveY;
                        startX = moveX;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    public void hiddenToast() {
        if (wm != null && viewToast != null) {
            if (viewToast.getParent() != null) {
                wm.removeView(viewToast);
            }
        }
    }
}
