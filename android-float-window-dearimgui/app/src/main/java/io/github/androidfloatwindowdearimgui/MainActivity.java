package io.github.androidfloatwindowdearimgui;

import android.app.*;
import android.os.*;
import android.provider.*;
import android.content.*;
import android.net.*;
import android.widget.*;


public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if (have_permission()) {
            StartFloatWindow();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                       Uri.parse("package:"+getPackageName()));
            startActivityForResult(intent, 1);
            // 请求码是1。
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            || Settings.canDrawOverlays(this);
    }
    
    void StartFloatWindow() {
        Toast.makeText(this, "成功，仍待完善。", Toast.LENGTH_LONG).show();
    }
}
