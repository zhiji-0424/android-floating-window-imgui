package io.github.androidfloatwindowdearimgui;

import android.app.*;
import android.os.*;
import java.lang.Process;
import android.provider.*;
import android.content.*;
import android.net.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.view.View.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity {
	
	TextView text_view = null;
	FloatSurfaceView fv = null;
	NativeActivity djdjfkdj;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CrashHandler.getInstance().init(this);
		setContentView(R.layout.main);
		text_view = findViewById(R.id.mainTextView1);
		text_view.setTextSize(9f);
		
		//fv = new FloatSurfaceView(this);
		//setContentView(fv);

		if (have_permission()) {
			StartService();
		} else {
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, 1);
			// 请求码是1。
		}
		
		
		new Thread(new Runnable() {
				@Override
				public void run()
				{
					startLogcatThread();
				}
			}).start();
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
        // Toast.makeText(this, "悬浮窗服务已启动", Toast.LENGTH_SHORT).show();
    }
	
	void StopService() {
		
	}
	
	// 日志记录
	public void startLogcatThread() {
		Process process = null;
		BufferedReader reader = null;
		
		// Toast.makeText(getApplicationContext(), "run", Toast.LENGTH_SHORT).show();
		try {
			// 执行 logcat 命令
			process = Runtime.getRuntime().exec("logcat -c");
			process = Runtime.getRuntime().exec("logcat");
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//FileWriter fw = new FileWriter("/storage/emulated/0/Android/data/io.github.androidfloatwindowdearimgui/files/a.log");

			List<String> lines = new ArrayList<>();
			String line;
			int lineCount = 0;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
				lineCount++;

				// 每收集 1 行就发送一次
				if (lineCount >= 50) {
					final String logStr = String.join("\n", lines);
					// Toast.makeText(getApplicationContext(), logStr, Toast.LENGTH_SHORT);
					// handler.post();

					// Toast.makeText(getApplicationContext(), logStr, Toast.LENGTH_SHORT).show();
					
					/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("日志").setMessage(logStr);
					builder.show();*/
					
					
					this.runOnUiThread(new Runnable() {
							@Override
							public void run()
							{
								text_view.setText(logStr);
								ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
								ClipData data = ClipData.newPlainText("label", logStr);
								cm.setPrimaryClip(data);
							}
						});
					/*fw.write(logStr+"\n");
					fw.flush();*/

					lines.clear();
					lineCount = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (process != null) {
				process.destroy();
			}
		}
	}
}
