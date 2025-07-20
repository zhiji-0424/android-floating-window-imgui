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
	RendererThread thread = null;

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
		thread = new RendererThread();
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		jnicallbacks.AppQuit();
		running = false;
		if (thread != null) {
			try {
				thread.join();
			}
			catch (InterruptedException e) {
				Log.e("FloatSurfaceView", "RendererThread join 发生错误。");
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	class RendererThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!running) {
				// 空循环等待
			}
			while (running) {
				// Canvas c = holder.lockCanvas();
				// c.drawColor(Color.BLACK);
				// Paint p = new Paint();
				// p.setColor(Color.GREEN);
				// c.drawText("hello,surface创建完成。", 0, 0, p);
				// holder.unlockCanvasAndPost(c);
				jnicallbacks.AppIterate();
			}
		}

	}
}

