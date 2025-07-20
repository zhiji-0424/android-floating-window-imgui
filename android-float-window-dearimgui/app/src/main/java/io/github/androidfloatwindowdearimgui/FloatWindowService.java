package io.github.androidfloatwindowdearimgui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;

// 默认了权限具备才会启动服务

public class FloatWindowService extends Service {
	
	WindowManager window_manager = null;
	FloatSurfaceView float_view = null;

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
		Toast.makeText(this, "创建了: CreateFloatWindow", Toast.LENGTH_SHORT).show();
		float_view = new FloatSurfaceView(this);
		
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
		Toast.makeText(this, "成功，仍待完善。", Toast.LENGTH_LONG).show();

        float_view.setLayoutParams(
		    new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // 这里就不适配旧系统了
        int layout_flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        WindowManager.LayoutParams layout_params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layout_flag,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT);
        // layout_params.gravity = Gravity.TOP | Gravity.START;
		layout_params.format = PixelFormat.RGBA_8888;
        layout_params.x = 0;
        layout_params.y = 0;

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

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 记录触摸前的位置和触摸点坐标
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            // 计算移动后的新位置，并更新 LayoutParams
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            window_manager.updateViewLayout(float_view, params);
                            // ((TextView)float_view).setText(String.valueOf(params.x) + "," + String.valueOf(params.y));
                            return true;
                    }
                    return false;
                }
            });
    }
}

