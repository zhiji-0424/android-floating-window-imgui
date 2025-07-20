package io.github.androidfloatwindowdearimgui;

import android.app.*;
import android.os.*;
import android.provider.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.view.View.*;

public class MainActivity extends Activity {
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (have_permission()) {
			StartService();
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
				StartService();
			} else {
				Toast.makeText(this, "在刚刚的页面没有授权", Toast.LENGTH_LONG).show();
			}
		}
	}

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        // 似乎返回键按下后不会销毁？
    }

	boolean have_permission() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
	}
    
    void StartService() {
        Intent intent = new Intent(this, FloatWindowService.class);
        startService(intent);
        Toast.makeText(this, "悬浮窗服务已启动", Toast.LENGTH_SHORT).show();
    }
	
	void StopService() {
		
	}
}
