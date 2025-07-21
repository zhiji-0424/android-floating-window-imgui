package io.github.androidfloatwindowdearimgui;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import android.app.*;


public class ErrorActivity extends Activity {
    
    public static final String TAG = "ErrorActivity";
	public TextView tv1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		//tv1 = new TextView(this);
        setContentView(tv1);

		//Toolbar toolbar=(Toolbar)findViewById(R.id.errortoolbar);
		
		//setSupportActionBar(toolbar);
		tv1=findViewById(R.id.errorTextView1);
		Intent Intent=this.getIntent();
		
		tv1.setText(Intent.getStringExtra("Errmsg"));
    }
    
}
