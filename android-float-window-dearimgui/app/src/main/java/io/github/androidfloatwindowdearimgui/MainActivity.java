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
	View FloatView = null;

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

	boolean have_permission() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
	}

	void StartFloatWindow() {
		Toast.makeText(this, "成功，仍待完善。", Toast.LENGTH_LONG).show();
		
        FloatView = new TextView(this);
		((TextView) FloatView).setText("hello这是中文。😁(／_＼)大怨种");
		((TextView) FloatView).setTextColor(0xffffffff);
		((TextView) FloatView).setBackgroundColor(0x80ff0011);
		FloatView.setLayoutParams(
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
        
        WindowManager window_manager = (WindowManager)getSystemService(WINDOW_SERVICE);
        window_manager.addView(FloatView, layout_params);
        
	}
}

class NativeView extends View {
	public NativeView(Context context) {
		super(context);
	}
}

