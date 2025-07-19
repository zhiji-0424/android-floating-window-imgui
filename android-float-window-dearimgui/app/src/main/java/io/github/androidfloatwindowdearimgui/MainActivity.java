package io.github.androidfloatwindowdearimgui;

import android.app.*;
import android.os.*;
import android.provider.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;

public class MainActivity extends Activity {
	
    View float_view = null;
    WindowManager window_manager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (have_permission()) {
			StartFloatWindow();
		} else {
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, 1);
			// 请求码是1。
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			// 是请求悬浮窗返回的
			if (have_permission()) {
				StartFloatWindow();
			} else {
				Toast.makeText(this, "没有授权", Toast.LENGTH_LONG).show();
			}
		}
	}

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
    }
    
    

	boolean have_permission() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
	}

	void StartFloatWindow() {
		Toast.makeText(this, "成功，仍待完善。", Toast.LENGTH_LONG).show();
		
        float_view = new TextView(this);
		((TextView) float_view).setText("hello这是中文。😁(／_＼)大怨种");
		((TextView) float_view).setTextColor(0xffffffff);
		((TextView) float_view).setBackgroundColor(0x80ff0011);
		float_view.setLayoutParams(
				new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        
        // 这里就不适配旧系统了
        int layout_flag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        WindowManager.LayoutParams layout_params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layout_flag,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
        // layout_params.gravity = Gravity.TOP | Gravity.START;
        layout_params.x = (500);
        layout_params.y = (500);
        
        window_manager = (WindowManager)getSystemService(WINDOW_SERVICE);
        window_manager.addView(float_view, layout_params);
        SetupDragging(layout_params);
	}
    
    void HideFloatWindow() {
        window_manager.removeView(float_view);
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
                            return true;
                    }
                    return false;
                }
            });
    }
}

class NativeView extends View {
	public NativeView(Context context) {
		super(context);
	}
}

