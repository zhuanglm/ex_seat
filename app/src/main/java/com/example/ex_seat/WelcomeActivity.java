package com.example.ex_seat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends Activity {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		Button m_Startbtn = (Button)findViewById(R.id.button1);
		m_Startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);  //从IntentActivity跳转到SubActivity
				intent.putExtra("name", "raymond");  //放入数据
				startActivity(intent);  //开始跳转
				finish();
            }
        });
	}

}
