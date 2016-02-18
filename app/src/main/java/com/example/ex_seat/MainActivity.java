package com.example.ex_seat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;



public class MainActivity extends Activity {

	public static class MyHandler extends Handler {
	    private final WeakReference<MainActivity> mActivity;
	 
	    private MyHandler(MainActivity activity) {
	      mActivity = new WeakReference<MainActivity>(activity);
	    }
	 
	    @Override
	    public void handleMessage(Message msg) {
	    	MainActivity activity = mActivity.get();
	    	if (activity != null) {
	    	  super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						
						break;
					case 1:
						//完成主界面更新,拿到数据
						String data = (String)msg.obj;
						String value = (String) activity.hellotv.getText();
						/*int nSum = Integer.valueOf(data);
						if(isNumeric(value))
						{
							nSum += Integer.valueOf(value);
						}
						activity.hellotv.setText(Integer.toString(nSum));*/
						activity.hellotv.setText(data);
						//Toast.makeText(contextAct,data,Toast.LENGTH_SHORT).show();
						break;
					case 0x1234: 
						int nRandom = msg.getData().getInt("get_data");
						//activity.ceshitv.setText(Integer.toString(nRandom));
						activity.drawView.invalidate();
						activity.dashboardView1.setRealTimeValue(nRandom);
						activity.dashboardView1.invalidate();
						break;
					default:
						break;
				}
	    	}
	    }
	}
	 
	public MyHandler handler = new MyHandler(this);
	StringBuffer result = new StringBuffer();
	String sR = "abc";
	static Context contextAct;
	TextView hellotv;
	private DataGraph drawView;
	private DashboardView dashboardView1;
	private static boolean m_bStopFlag = false;
	private static boolean m_bBTStartFlag = true;
	Button BTSwitchbtn;
	Button BKSwitchbtn;
		
	Runnable runnable_long=new Runnable() {
	    @Override
	    public void run() {

	    	result.delete(0, result.length());
	    	result.append(getResources().getString(R.string.Req_URL));
	    	result.append(getString(R.string.Design_Total));
	    	
	    	Thread tURL = new Thread(){
				public void run() {  
					HttpConnect ht = new HttpConnect();
					//sR = ht.getData(result);
					Message msg = new Message();
					msg.what = DataDef.msgTYPE_YES;
					msg.obj = ht.getData(result);
					handler.sendMessage(msg);
					//sR = ht.ceshi(result);
					
				}
			};
			tURL.start();
	    	//Toast.makeText(getApplicationContext(),sR,Toast.LENGTH_SHORT).show();
	        					
	        handler.postDelayed(this, 10000);
						
			}
	};
	
	Runnable runnable_short=new Runnable() {
	    @Override
	    public void run() {
	        
	    	result.delete(0, result.length());
	    	result.append(getResources().getString(R.string.Req_URL));
	    	result.append(getString(R.string.Design_Total));
	    	
	    	Thread tBT = new Thread(){
				public void run() {  
					
					Message msg = new Message();
					msg.what = 0x1234;
					Bundle bundle = new Bundle();
					
					if(DataDef.dataFromDev.size() >= DataDef.MaxDataSize && DataDef.MaxDataSize>0){ 
						DataDef.dataFromDev.remove(0); 
                    }
					int nData = new Random().nextInt(800) + 1;
					DataDef.dataFromDev.add(nData);
					bundle.putInt("get_data", nData);
					msg.setData(bundle);
					handler.sendMessage(msg);
                    //handler.sendEmptyMessage(0x1234);
					
				}
			};
			tBT.start();
	    	//Toast.makeText(getApplicationContext(),sR,Toast.LENGTH_SHORT).show();
	        handler.postDelayed(this, 300);
						
			}
	};
	
	//private static final String DB_NAME = "ex_seat.db"; //数据库名称
    //private static final int version = 1; //数据库版本
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = this.getIntent();    //获得当前的Intent
 		Bundle bundle = intent.getExtras();  //获得全部数据
		String value = bundle.getString("name");  //获得名为name的值

		ActionBar actionBar=getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		
		/*DatabaseHelper database = new DatabaseHelper(this,DB_NAME,null,version);
		SQLiteDatabase db = database.getReadableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("username","Raymond Zhuang");
		cv.put("password","wuyi"); 
		db.insert("user",null,cv);*/
		
		dashboardView1 = (DashboardView) findViewById(R.id.dashboardView1);
		List<HighlightCR> highlight1 = new ArrayList<HighlightCR>();
        highlight1.add(new HighlightCR(210, 60, Color.parseColor("#03A9F4")));
        highlight1.add(new HighlightCR(270, 60, Color.parseColor("#FFA000")));
        dashboardView1.setStripeHighlightColorAndRange(highlight1);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.chart_layout);
		drawView = new DataGraph(this);//创建自定义的控件
		drawView.setMinimumHeight(300);
		drawView.setMinimumWidth(500);
		layout.addView(drawView);//讲自定义的控件进行添加
		
		BKSwitchbtn = (Button)findViewById(R.id.button1);
		BTSwitchbtn = (Button)findViewById(R.id.button2);
		contextAct = getApplicationContext();
		hellotv = (TextView)findViewById(R.id.textView1);
		
		
		BKSwitchbtn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                
	                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
	                
	                if(m_bStopFlag)
	                {
	                	BKSwitchbtn.setText(R.string.button_name1);
	                	handler.postDelayed(runnable_short, 300);
	            		handler.postDelayed(runnable_long, 1000);
	            		m_bStopFlag = false;
	                }
	                else
	                {
	                	BKSwitchbtn.setText(R.string.button_name2);
	 	                handler.removeCallbacks(runnable_short);
	 	            	handler.removeCallbacks(runnable_long);
	 	            	m_bStopFlag = true;
	                }
	               
	            }
	        });
		
		handler.postDelayed(runnable_short, 300);
		handler.postDelayed(runnable_long, 1000);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void BTSwitch(View view)
	{
		
		if(m_bBTStartFlag)
        {
			BTSwitchbtn.setText(R.string.button_name4);
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			if(adapter != null){
				//Toast.makeText(getApplicationContext(),"BlueTooth was detected",Toast.LENGTH_SHORT).show();
				if(!adapter.isEnabled()){
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					//请求开启蓝牙设备
					startActivity(intent);
				}
				Set<BluetoothDevice> devices = adapter.getBondedDevices();
				if(devices.size()>0){
					for(Iterator<BluetoothDevice> it = devices.iterator();it.hasNext();){
						BluetoothDevice device = (BluetoothDevice)it.next();
						//打印出远程蓝牙设备的物理地址
						Toast.makeText(getApplicationContext(),device.getName(),Toast.LENGTH_SHORT).show();
					}
				}else{
					
					Toast.makeText(getApplicationContext()," 还没有已配对的远程蓝牙设备！",Toast.LENGTH_SHORT).show();
				}
			}
			else{
				Toast.makeText(getApplicationContext()," No BlueTooth Device",Toast.LENGTH_SHORT).show();
			}
		
        	m_bBTStartFlag = false;
         }
         else
         {
        	BTSwitchbtn.setText(R.string.button_name3);
            
          	m_bBTStartFlag = true;
         }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			showMultiChosDia();
			
			return true;
		}
		else if(id == R.id.action_exit)
		{
			showExitDia();
			
		}

		return super.onOptionsItemSelected(item);
	}
	
	public static String Inputstr2Str_Reader(InputStream in, String encode)
    {
        
        String str = "";
        try
        {
            if (encode == null || encode.equals(""))
            {
                // 默认以utf-8形式
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
            StringBuffer sb = new StringBuffer();
            
            while ((str = reader.readLine()) != null)
            {
                sb.append(str).append("\n");
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return str;
    }
	
	public static boolean isNumeric(String str){
		for (int i = 0; i < str.length(); i++){
		   System.out.println(str.charAt(i));
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		}
		return true;
	}
	
	ArrayList<Integer> myChose= new ArrayList<Integer>();  
    private void showMultiChosDia()  
    {  
        final String[] mList={"选项1","选项2","选项3","选项4","选项5","选项6","选项7"};  
        final boolean mChoseSts[]={false,false,false,false,false,false,false};  
        myChose.clear();  
        AlertDialog.Builder multiChosDia=new AlertDialog.Builder(this);  
        multiChosDia.setTitle("多项选择对话框");  
        multiChosDia.setMultiChoiceItems(mList, mChoseSts, new DialogInterface.OnMultiChoiceClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {  
                // TODO Auto-generated method stub  
                if(isChecked)  
                {  
                    myChose.add(which);  
                }  
                else  
                {  
                    myChose.remove(which);  
                }  
            }  
        });  
        multiChosDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                // TODO Auto-generated method stub  
                int size=myChose.size();  
                String str="";  
                for(int i=0;i<size;i++)  
                {  
                    str+=mList[myChose.get(i)];  
                }  
                //showClickMessage(str);
                Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
            }  
        });  
        multiChosDia.create().show();  
    }
    
    
    
    private void showExitDia()  
    {  
         
    	//AlertDialog.Builder normalDialog=new AlertDialog.Builder(getApplicationContext());  
        AlertDialog.Builder normalDia=new AlertDialog.Builder(this);  
        normalDia.setIcon(R.drawable.ic_launcher);  
        normalDia.setTitle("普通的对话框");  
        normalDia.setMessage("退出系统？");  
          
        normalDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                // TODO Auto-generated method stub  
            	handler.removeCallbacks(runnable_short);
            	handler.removeCallbacks(runnable_long);
            	
				System.exit(0);
                //Toast.makeText(getApplicationContext(),"确定",Toast.LENGTH_SHORT).show();
            }  
        });  
        normalDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                // TODO Auto-generated method stub  
            	dialog.dismiss();
                //Toast.makeText(getApplicationContext(),"取消",Toast.LENGTH_SHORT).show();
            }  
        });  
        normalDia.create().show();
		
    } 
}
