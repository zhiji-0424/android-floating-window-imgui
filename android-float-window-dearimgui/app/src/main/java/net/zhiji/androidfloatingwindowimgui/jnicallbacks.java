package net.zhiji.androidfloatingwindowimgui;
import android.view.*;

public class jnicallbacks {
	public native static void AppInit(Surface surface);
	public native static void AppQuit();
	public native static void AppIterate();
	public native static void AppEvent();
	
	static {
		System.loadLibrary("main");
	}
}
