package net.zhiji.androidfloatingwindowimgui;

import android.view.*;
import android.content.res.*;

public class jnicallbacks {
	public native static void AppInit(Surface surface);
	public native static void AppQuit();
	public native static void AppAssetInit(AssetManager asset_manager);
	// public native static void AppIterate();
	
	// event_type: key:0, motion:1, mouse_button:2
	// device_type: mouse:0, pen:1, finger:2
	// action:     motion:0, down:1, up:2
	public native static void AppEvent(int event_type,
										int key_code, int scan_code, int key_action, int mate_state,
										int device_type, int action, int x, int y);
	
	static {
		System.loadLibrary("main");
	}
}
