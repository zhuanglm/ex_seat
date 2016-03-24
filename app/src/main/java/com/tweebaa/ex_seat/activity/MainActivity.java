package com.tweebaa.ex_seat.activity;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.tweebaa.ex_seat.R;
import com.tweebaa.ex_seat.adapter.ViewPagerAdapter;
import com.tweebaa.ex_seat.model.BatteryView;
import com.tweebaa.ex_seat.model.DashboardView;
import com.tweebaa.ex_seat.model.DataCollector;
import com.tweebaa.ex_seat.model.DataUtil;
import com.tweebaa.ex_seat.model.DataGraph;
import com.tweebaa.ex_seat.model.DatabaseHelper;
import com.tweebaa.ex_seat.model.HighlightCR;
import com.tweebaa.ex_seat.model.HttpConnect;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

	public MyHandler handler = new MyHandler(this);
	StringBuffer result = new StringBuffer();
	static Context contextAct;
	TextView hellotv;
	private ActionBar actionBar;
	private DataGraph drawView;
	private DashboardView dashboardView1;
	private static boolean m_bStopFlag = false;
	private static boolean m_bBTStartFlag = true;
	private ViewPager viewPager;
	Button BTSwitchbtn;
	Button BKSwitchbtn;
	private int currentItem,offSet,bmWidth;
	private Animation animation;
	private ImageView imageView;
	private Matrix matrix = new Matrix();
	public DatabaseHelper database;
	private SQLiteDatabase db;
	private Cursor db_cursor;
	private SimpleDateFormat sDateFormat;
	public SimpleCursorAdapter simple_adapter;
	private BatteryView m_SensorPower;

	public static class MyHandler extends Handler {
	    private final WeakReference<MainActivity> mActivity;
	 
	    private MyHandler(MainActivity activity) {
	      mActivity = new WeakReference<>(activity);
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
						//String value = (String) activity.hellotv.getText();
						/*int nSum = Integer.valueOf(data);
						if(isNumeric(value))
						{
							nSum += Integer.valueOf(value);
						}
						activity.hellotv.setText(Integer.toString(nSum));*/
						activity.hellotv.setText(data);
						activity.m_SensorPower.setValue(DataCollector.m_nAverageRPM);
						//Toast.makeText(contextAct,data,Toast.LENGTH_SHORT).show();
						break;
					case 0x1234: 
						int nRandom = msg.getData().getInt("get_data");
						DataCollector.getAverageRPM(nRandom);
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
					msg.what = DataUtil.msgTYPE_YES;
					msg.obj = ht.getXMLData(result);
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

	    	Thread tBT = new Thread(){
				public void run() {  
					
					Message msg = new Message();
					msg.what = 0x1234;
					Bundle bundle = new Bundle();
					
					if(DataUtil.dataFromDev.size() >= DataUtil.MaxDataSize && DataUtil.MaxDataSize>0){
						DataUtil.dataFromDev.remove(0);
                    }
					int nData = new Random().nextInt(800) + 1;
					DataUtil.dataFromDev.add(nData);
					bundle.putInt("get_data", nData);
					msg.setData(bundle);
					handler.sendMessage(msg);
                    //handler.sendEmptyMessage(0x1234);
					
				}
			};
			tBT.start();
	    	//Toast.makeText(getApplicationContext(),sR,Toast.LENGTH_SHORT).show();
	        handler.postDelayed(this, DataUtil.TimeInterver_BT);
						
			}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int mCurrentOrientation = getResources().getConfiguration().orientation;

		setContentView(R.layout.activity_main);
		//Intent intent = this.getIntent();    //获得当前的Intent
 		//Bundle bundle = intent.getExtras();  //获得全部数据
		//String value = bundle.getString("name");  //获得名为name的值

		//actionBar=getActionBar();
		final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayShowTitleEnabled(false);
		if(actionBar!=null){
			actionBar.setTitle(R.string.app_page1);
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setLogo(R.drawable.logo);
			actionBar.setDisplayUseLogoEnabled(true);
		}

		database = new DatabaseHelper(this, DataUtil.DB_NAME,null, DataUtil.version);
		db = database.getReadableDatabase();

		List<View> viewList = new ArrayList<>();

		LayoutInflater lf = LayoutInflater.from(this);
		View view1=null;
		if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
			// If current screen is portrait
			Log.i("info", "portrait"); // 竖屏
			//setContentView(R.layout.mainP);
			view1 = lf.inflate(R.layout.layout_guage, (ViewGroup)null);
		} else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
			//If current screen is landscape
			Log.i("info", "landscape"); // 横屏
			//setContentView(R.layout.mainL);
			view1 = lf.inflate(R.layout.layout_guage_landscape, (ViewGroup)null);
		}

		View view2 = lf.inflate(R.layout.layout_record, (ViewGroup)null);
		View view3 = lf.inflate(R.layout.layout_fragment_profile, (ViewGroup)null);
		View view4 = lf.inflate(R.layout.layout_fragment_board, (ViewGroup)null);

		//2nd page
		ListView m_listviewData = (ListView) view2.findViewById(R.id.listView_data);

		db_cursor = db.query("data", new String[]{"_id","max", "date", "duration", "avg"}, null, null, null, null, null);
		simple_adapter = new SimpleCursorAdapter(view2.getContext(), R.layout.data_item, db_cursor,
				new String[]{"max","date", "duration", "avg"}, new int[]{R.id.max,R.id.date, R.id.duration, R.id.avg},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		m_listviewData.setAdapter(simple_adapter);

		imageView = (ImageView) findViewById(R.id.cursor);
		TextView textView1 = (TextView) findViewById(R.id.textView1);
		TextView textView2 = (TextView) findViewById(R.id.textView2);
		TextView textView3 = (TextView) findViewById(R.id.textView3);
		TextView textView4 = (TextView) findViewById(R.id.textView4);

		viewList.add(view1);
		viewList.add(view2);
		viewList.add(view3);
		viewList.add(view4);

		initeCursor();

		ViewPagerAdapter viewPager_adapter = new ViewPagerAdapter(viewList);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(viewPager_adapter);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			//Cursor cursor;

			// 顶部的imageView是通过animation缓慢的滑动
			@Override
			public void onPageSelected(int arg0) {

				switch (arg0) {
					case 0:
						if (currentItem == 1) {
							animation = new TranslateAnimation(
									offSet * 2 + bmWidth, 0, 0, 0);
						} else if (currentItem == 2) {
							animation = new TranslateAnimation(offSet * 4 + 2
									* bmWidth, 0, 0, 0);
						} else if (currentItem == 3) {
							animation = new TranslateAnimation(offSet * 6 + 3
									* bmWidth, 0, 0, 0);
						}
						actionBar.setTitle(R.string.app_page1);

						break;
					case 1:
						if (currentItem == 0) {
							animation = new TranslateAnimation(0, offSet * 2
									+ bmWidth, 0, 0);
						} else if (currentItem == 2) {
							animation = new TranslateAnimation(4 * offSet + 2
									* bmWidth, offSet * 2 + bmWidth, 0, 0);
						} else if (currentItem == 3) {
							animation = new TranslateAnimation(6 * offSet + 3 * bmWidth, offSet * 2 + bmWidth, 0, 0);
						}

						actionBar.setTitle(R.string.app_page2);

						break;
					case 2:
						if (currentItem == 0) {
							animation = new TranslateAnimation(0, 4 * offSet + 2
									* bmWidth, 0, 0);
						} else if (currentItem == 1) {
							animation = new TranslateAnimation(
									offSet * 2 + bmWidth, 4 * offSet + 2 * bmWidth,
									0, 0);
						} else if (currentItem == 3) {
							animation = new TranslateAnimation(
									6 * offSet + 3 * bmWidth, 4 * offSet + 2 * bmWidth,
									0, 0);
						}
						actionBar.setTitle(R.string.app_page3);
						break;
					case 3:
						if (currentItem == 0) {
							animation = new TranslateAnimation(0, 6 * offSet + 3 * bmWidth, 0, 0);
						} else if (currentItem == 1) {
							animation = new TranslateAnimation(offSet * 2
									+ bmWidth, 6 * offSet + 3 * bmWidth, 0, 0);
						} else if (currentItem == 2) {
							animation = new TranslateAnimation(
									4 * offSet + 2 * bmWidth, 6 * offSet + 3 * bmWidth,
									0, 0);
						}

						actionBar.setTitle(R.string.app_page4);
						break;
				}

				currentItem = arg0;
				animation.setDuration(150); // 光标滑动速度
				animation.setFillAfter(true);
				imageView.startAnimation(animation);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		textView1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(0);
			}
		});

		textView2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(1);
			}
		});

		textView3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(2);
			}
		});

		textView4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(3);
			}
		});

		dashboardView1 = (DashboardView) view1.findViewById(R.id.dashboardView_rpm);
		if(dashboardView1!=null){
			List<HighlightCR> highlight1 = new ArrayList<HighlightCR>();
			highlight1.add(new HighlightCR(210, 60, Color.parseColor("#03A9F4")));
			highlight1.add(new HighlightCR(270, 60, Color.parseColor("#FFA000")));
			dashboardView1.setStripeHighlightColorAndRange(highlight1);
		}
		m_SensorPower = (BatteryView) view1.findViewById(R.id.battery);
		
		LinearLayout layout = (LinearLayout)view1.findViewById(R.id.chart_layout);
		drawView = new DataGraph(this);//创建自定义的控件
		drawView.setMinimumHeight(300);
		drawView.setMinimumWidth(500);
		layout.addView(drawView);//讲自定义的控件进行添加
		
		BKSwitchbtn = (Button)view1.findViewById(R.id.button1);
		BTSwitchbtn = (Button)view1.findViewById(R.id.button2);
		contextAct = getApplicationContext();
		hellotv = (TextView)view1.findViewById(R.id.textView_http);
		
		
		BKSwitchbtn.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                
	                //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
	                
	                if(m_bStopFlag)
	                {
	                	BKSwitchbtn.setText(R.string.button_name1);
	                	handler.postDelayed(runnable_short, DataUtil.TimeInterver_BT);
	            		handler.postDelayed(runnable_long, 1000);
	            		m_bStopFlag = false;
	                }
	                else
	                {
	                	BKSwitchbtn.setText(R.string.button_name2);
	 	                handler.removeCallbacks(runnable_short);
	 	            	handler.removeCallbacks(runnable_long);
	 	            	m_bStopFlag = true;

						sDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.getDefault());
						String date = sDateFormat.format(new java.util.Date());
						//Toast.makeText(getApplicationContext(),Integer.toString(DataCollector.m_nTimes*DataUtil.TimeInterver_BT/1000),Toast.LENGTH_SHORT).show();
						//Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
						ContentValues cv = new ContentValues();
						cv.put("date", date);
						long  ms = DataCollector.m_nTimes* DataUtil.TimeInterver_BT ;//毫秒数
						SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());//初始化Formatter的转换格式。
						formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
						String hms = formatter.format(ms);
						//cv.put("duration",Integer.toString(DataCollector.m_nTimes*DataUtil.TimeInterver_BT / 1000));
						cv.put("duration",hms);
						cv.put("max", Integer.toString(DataCollector.getMaxRPM()));
						cv.put("avg", Integer.toString(DataCollector.m_nAverageRPM));

						db.insert("data", null, cv);
						DataCollector.init();

						db_cursor = db.query("data", new String[]{"_id","max", "date", "duration", "avg"}, null, null, null, null, null);
						simple_adapter.swapCursor(null);
						simple_adapter.swapCursor(db_cursor);

						/*while(cursor.moveToNext()){
							String date = cursor.getString(cursor.getColumnIndex("date"));
							String dura = cursor.getString(cursor.getColumnIndex("duration"));
							String max = cursor.getString(cursor.getColumnIndex("avg"));
						}*/

	                }
	               
	            }
	        });
		
		handler.postDelayed(runnable_short, DataUtil.TimeInterver_BT);
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
		else if(id == R.id.action_clear)
		{
			db.delete("data",null,null);
			db_cursor = db.query("data", new String[]{"_id","max", "date", "duration", "avg"}, null, null, null, null, null);
			simple_adapter.swapCursor(null);
			simple_adapter.swapCursor(db_cursor);
		}
		else if(id == R.id.action_exit)
		{
			showExitDia();
		}

		return super.onOptionsItemSelected(item);
	}
	


	private void initeCursor() {
		Bitmap view_cursor = BitmapFactory.decodeResource(getResources(), R.drawable.cursor);
		bmWidth = view_cursor.getWidth();

		DisplayMetrics dm;
		dm = getResources().getDisplayMetrics();

		//offSet = (dm.widthPixels - 3 * bmWidth) / 6;
		float scalx = (dm.widthPixels/ DataUtil.WinTabNum);
		scalx /= bmWidth;
		matrix.setScale(scalx,1f);
		bmWidth*=scalx;
		offSet = (dm.widthPixels/ DataUtil.WinTabNum - bmWidth) / 2;
		//matrix.setTranslate(offSet, 0);
		matrix.postTranslate(offSet, 0);
		imageView.setImageMatrix(matrix); // 需要imageView的scaleType为matrix
		currentItem = 0;
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

				db.close();
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

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}
}
