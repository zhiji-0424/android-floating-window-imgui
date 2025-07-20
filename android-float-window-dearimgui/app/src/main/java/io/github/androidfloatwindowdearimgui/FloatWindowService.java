package io.github.androidfloatwindowdearimgui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import android.widget.*;

public class FloatWindowService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO: Implement this method
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
		// TODO: Implement this method
		super.onConfigurationChanged(newConfig);
	}

	// 创建
	void CreateFloatWindow() {
		Toast.makeText(this, "创建了: CreateFloatWindow", Toast.LENGTH_SHORT).show();
	}

	// 关闭 销毁
	void CloseFloatWindow() {

	}

	// 显示
	void ShowFloatWindow() {

	}

	// 暂时隐藏
	void HideFloatWindow() {

	}
}

