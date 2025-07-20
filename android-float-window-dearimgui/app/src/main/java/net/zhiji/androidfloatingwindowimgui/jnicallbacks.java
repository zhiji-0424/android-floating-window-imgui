package net.zhiji.androidfloatingwindowimgui;

public class jnicallbacks {
	public native static void AppInit();
	public native static void AppQuit();
	public native static void AppIterate();
	public native static void AppEvent();
	
	static {
		System.loadLibrary("main");
	}
}
