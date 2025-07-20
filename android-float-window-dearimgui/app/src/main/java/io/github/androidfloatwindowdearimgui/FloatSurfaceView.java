package io.github.androidfloatwindowdearimgui;

import android.view.*;
import android.content.*;
import android.widget.Toast.*;
import android.util.*;
import android.graphics.*;
import net.zhiji.androidfloatingwindowimgui.*;

public class FloatSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder holder = null;
	boolean running = false;

	public FloatSurfaceView(Context context) {
		super(context);
		holder = getHolder();
		// holder 用于控制surface，回调在此类中已实现
		holder.addCallback(this);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// 画布创建就开始绘图
		running = true;
		jnicallbacks.AppInit(holder.getSurface());
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		jnicallbacks.AppQuit();
		running = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
}

