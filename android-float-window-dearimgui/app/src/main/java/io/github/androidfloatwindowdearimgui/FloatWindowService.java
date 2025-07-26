package io.github.androidfloatwindowdearimgui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.util.*;

// 默认了权限具备才会启动服务

public class FloatWindowService extends Service {
	
	WindowManager window_manager = null;
	FloatSurfaceView float_view = null;
	WindowManager.LayoutParams layout_params = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CreateFloatWindow();
		ShowFloatWindow();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		CloseFloatWindow();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	// 创建
	void CreateFloatWindow() {
		float_view = new FloatSurfaceView(this);
		float_view.setZOrderOnTop(true);
		
		// 这里就不适配旧系统了
        layout_params = new WindowManager.LayoutParams();
		layout_params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		layout_params.format = PixelFormat.RGBA_8888;
		layout_params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layout_params.flags = WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
							| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_FULLSCREEN
							| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		layout_params.alpha = 0.99f;
		/*WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
			| WindowManager.LayoutParams.FLAG_FULLSCREEN*/
		layout_params.width = 400;
		layout_params.height= 400;
		//layout_params.alpha = 0.0f;
        // layout_params.gravity = Gravity.TOP | Gravity.START;
		layout_params.x = 0;
        layout_params.y = 0;
	}

	// 关闭 销毁
	void CloseFloatWindow() {
		HideFloatWindow();
	}

	// 显示
	void ShowFloatWindow() {
		StartFloatWindow();
	}

	// 暂时隐藏
	void HideFloatWindow() {

	}
	
	void StartFloatWindow() {
        window_manager = (WindowManager)getSystemService(WINDOW_SERVICE);
        window_manager.addView(float_view, layout_params);
        SetupDragging(layout_params);
	}
	
	// 代码来自ai
    void SetupDragging(final WindowManager.LayoutParams params) {
        float_view.setOnTouchListener(new View.OnTouchListener() {
                private int initialX; // 初始 X 坐标（像素）
                private int initialY; // 初始 Y 坐标（像素）
                private float initialTouchX; // 触摸初始 X 坐标（屏幕像素）
                private float initialTouchY; // 触摸初始 Y 坐标（屏幕像素）
				private boolean touch_in_title = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
					//Log.i("touch", "params.y:"+params.y);
					//Log.i("touch", "event.y:"+event.getY());
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 记录触摸前的位置和触摸点坐标
							if (event.getY() > 100) {
								// 起始点在标题栏位置才移动
								return false;
							}
							touch_in_title = true;
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_MOVE:
							if (!touch_in_title) {
								return false;
							}
                            // 计算移动后的新位置，并更新 LayoutParams
							params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            window_manager.updateViewLayout(float_view, params);
                            return true;
						case MotionEvent.ACTION_UP:
							touch_in_title = false;
                    }
                    return false;
                }
            });
    }
}

