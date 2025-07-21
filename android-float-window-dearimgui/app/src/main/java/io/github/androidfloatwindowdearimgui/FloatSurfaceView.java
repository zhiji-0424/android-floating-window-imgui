package io.github.androidfloatwindowdearimgui;

import android.content.*;
import android.opengl.*;
import android.view.*;
import net.zhiji.androidfloatingwindowimgui.*;

public class FloatSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	SurfaceHolder holder = null;
	boolean running = false;

	public FloatSurfaceView(Context context) {
		super(context);
		holder = getHolder();
		// holder 用于控制surface，回调在此类中已实现
		holder.addCallback(this);
		// setBackgroundColor(Color.argb(1.0f, 1.0f, 0.5f, 0.5f));
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
//

//public class FloatSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
//	private static final String TAG = "CustomGLSurfaceView";
//	private RenderThread mRenderThread;
//
//	public FloatSurfaceView(Context context) {
//		super(context);getHolder().addCallback(this);
//	}
//
//	@Override
//	public void surfaceCreated(SurfaceHolder holder) {
//		mRenderThread=new RenderThread(holder.getSurface());mRenderThread.start();
//	}
//
//	@Override
//	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//		mRenderThread.onSurfaceChanged(width,height);
//	}
//
//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder) {
//		mRenderThread.requestExitAndWait();
//	}
//
//	private static class RenderThread extends Thread {
//		private volatile boolean mShouldExit = false;
//		private int mWidth;
//		private int mHeight;
//		private Surface mSurface;
//
//		// EGL 相关变量
//		private EGLDisplay mEglDisplay;
//		private EGLContext mEglContext;
//		private EGLSurface mEglSurface;
//
//		public RenderThread(Surface surface) {
//			mSurface=surface;
//		}
//
//		@Override
//		public void run() {
//			initEGL();initGL();
//
//			while(!mShouldExit){if(!renderFrame()){break;}}
//
//			releaseEGL();
//		}
//
//		private void initEGL() {
//			// 1. 获取 EGLDisplay
//			mEglDisplay=EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);if(mEglDisplay==EGL14.EGL_NO_DISPLAY){throw new RuntimeException("eglGetDisplay failed");}
//
//			// 2. 初始化 EGL
//			int[]version=new int[2];if(!EGL14.eglInitialize(mEglDisplay,version,0,version,1)){throw new RuntimeException("eglInitialize failed");}
//
//			// 3. 选择 EGLConfig
//			int[]configAttribs={EGL14.EGL_RENDERABLE_TYPE,EGL14.EGL_OPENGL_ES2_BIT,EGL14.EGL_SURFACE_TYPE,EGL14.EGL_WINDOW_BIT,EGL14.EGL_RED_SIZE,8,EGL14.EGL_GREEN_SIZE,8,EGL14.EGL_BLUE_SIZE,8,EGL14.EGL_ALPHA_SIZE,8,EGL14.EGL_DEPTH_SIZE,16,EGL14.EGL_NONE};
//
//			EGLConfig[]configs=new EGLConfig[1];int[]numConfigs=new int[1];if(!EGL14.eglChooseConfig(mEglDisplay,configAttribs,0,configs,0,configs.length,numConfigs,0)){throw new RuntimeException("eglChooseConfig failed");}
//
//			// 4. 创建 EGLContext
//			int[]contextAttribs={EGL14.EGL_CONTEXT_CLIENT_VERSION,2,EGL14.EGL_NONE};mEglContext=EGL14.eglCreateContext(mEglDisplay,configs[0],EGL14.EGL_NO_CONTEXT,contextAttribs,0);if(mEglContext==EGL14.EGL_NO_CONTEXT){throw new RuntimeException("eglCreateContext failed");}
//
//			// 5. 创建 EGLSurface
//			int[]surfaceAttribs={EGL14.EGL_NONE};mEglSurface=EGL14.eglCreateWindowSurface(mEglDisplay,configs[0],mSurface,surfaceAttribs,0);if(mEglSurface==EGL14.EGL_NO_SURFACE){throw new RuntimeException("eglCreateWindowSurface failed");}
//
//			// 6. 绑定上下文和表面
//			if(!EGL14.eglMakeCurrent(mEglDisplay,mEglSurface,mEglSurface,mEglContext)){throw new RuntimeException("eglMakeCurrent failed");}
//		}
//
//		private void initGL() {
//			GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
//		}
//
//		private boolean renderFrame() {
//			if(mEglDisplay==EGL14.EGL_NO_DISPLAY){return false;}
//
//			GLES20.glClearColor(1.0f, 1.0f, 0.5f, 0.5f);
//			GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
//
//			// 在这里添加你的 OpenGL ES 渲染代码
//			// 例如绘制三角形、矩形等
//
//			EGL14.eglSwapBuffers(mEglDisplay,mEglSurface);return true;
//		}
//
//		public void onSurfaceChanged(int width, int height) {
//			mWidth=width;mHeight=height;GLES20.glViewport(0,0,width,height);
//		}
//
//		public void requestExitAndWait() {
//			mShouldExit = true;
//            try {
//                join();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        private void releaseEGL() {
//            if (mEglDisplay != EGL14.EGL_NO_DISPLAY) {
//                EGL14.eglMakeCurrent(mEglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
//									 EGL14.EGL_NO_CONTEXT);
//                EGL14.eglDestroySurface(mEglDisplay, mEglSurface);
//                EGL14.eglDestroyContext(mEglDisplay, mEglContext);
//                EGL14.eglTerminate(mEglDisplay);
//            }
//            mEglDisplay = EGL14.EGL_NO_DISPLAY;
//            mEglContext = EGL14.EGL_NO_CONTEXT;
//            mEglSurface = EGL14.EGL_NO_SURFACE;
//        }
//    }
//}
