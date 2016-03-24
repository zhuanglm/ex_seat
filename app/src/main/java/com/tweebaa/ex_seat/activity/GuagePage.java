package com.tweebaa.ex_seat.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tweebaa.ex_seat.R;
import com.tweebaa.ex_seat.model.DashboardView;
import com.tweebaa.ex_seat.model.DataCollector;
import com.tweebaa.ex_seat.model.DataUtil;
import com.tweebaa.ex_seat.model.DataGraph;
import com.tweebaa.ex_seat.model.DatabaseHelper;
import com.tweebaa.ex_seat.model.HighlightCR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
/**
 * Created by Zhuang on 2016-03-03.
 */
public class GuagePage extends Fragment {

    private Activity m_Act;
    private Context m_Context;
    private DatabaseHelper database;
    private SQLiteDatabase db;

    private int mCurrentOrientation;
    private DashboardView dashboardView1;
    private DataGraph drawView;
    private Button BTSwitchbtn;
    private Button BKSwitchbtn;
    private TextView m_textHttp;

    private static boolean m_bStopFlag = false;
    private static boolean m_bBTStartFlag = true;
    private SimpleDateFormat sDateFormat;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_Act = getActivity();
        m_Context = context;

        database = new DatabaseHelper(context,DataUtil.DB_NAME,null,DataUtil.version);
        db = database.getReadableDatabase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bundle bundle = getArguments();
        //bundle.getString("EVENT");
        //setListAdapter(adapter);
        BKSwitchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();

                if(m_bStopFlag)
                {
                    BKSwitchbtn.setText(R.string.button_name1);
                    ((MainActivity)m_Act).handler.postDelayed(((MainActivity)m_Act).runnable_short, DataUtil.TimeInterver_BT);
                    ((MainActivity)m_Act).handler.postDelayed(((MainActivity)m_Act).runnable_long, 1000);
                    m_bStopFlag = false;
                }
                else
                {
                    BKSwitchbtn.setText(R.string.button_name2);
                    ((MainActivity)m_Act).handler.removeCallbacks(((MainActivity)m_Act).runnable_short);
                    ((MainActivity)m_Act).handler.removeCallbacks(((MainActivity)m_Act).runnable_long);
                    m_bStopFlag = true;

                    sDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    //Toast.makeText(getApplicationContext(),Integer.toString(DataCollector.m_nTimes*DataDef.TimeInterver_BT/1000),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),date,Toast.LENGTH_SHORT).show();
                    ContentValues cv = new ContentValues();
                    cv.put("date", date);
                    long  ms = DataCollector.m_nTimes*DataUtil.TimeInterver_BT ;//毫秒数
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
                    formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                    String hms = formatter.format(ms);
                    //cv.put("duration",Integer.toString(DataCollector.m_nTimes*DataDef.TimeInterver_BT / 1000));
                    cv.put("duration",hms);
                    cv.put("max", Integer.toString(DataCollector.getMaxRPM()));
                    cv.put("avg", Integer.toString(DataCollector.m_nAverageRPM));

                    db.insert("data", null, cv);
                    DataCollector.init();

                    	/*while(cursor.moveToNext()){
							String date = cursor.getString(cursor.getColumnIndex("date"));
							String dura = cursor.getString(cursor.getColumnIndex("duration"));
							String max = cursor.getString(cursor.getColumnIndex("avg"));
						}*/

                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mCurrentOrientation = getResources().getConfiguration().orientation;
        View view=null;

        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
            // If current screen is portrait
            Log.i("info", "portrait"); // 竖屏
            //setContentView(R.layout.mainP);
            view = inflater.inflate(R.layout.layout_guage, null);
        } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
            //If current screen is landscape
            Log.i("info", "landscape"); // 横屏
            //setContentView(R.layout.mainL);
            view = inflater.inflate(R.layout.layout_guage_landscape, null);
        }

        dashboardView1 = (DashboardView) view.findViewById(R.id.dashboardView1);
        List<HighlightCR> highlight1 = new ArrayList<HighlightCR>();
        highlight1.add(new HighlightCR(210, 60, Color.parseColor("#03A9F4")));
        highlight1.add(new HighlightCR(270, 60, Color.parseColor("#FFA000")));
        dashboardView1.setStripeHighlightColorAndRange(highlight1);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.chart_layout);
        drawView = new DataGraph(m_Context);//创建自定义的控件
        drawView.setMinimumHeight(300);
        drawView.setMinimumWidth(500);
        layout.addView(drawView);//讲自定义的控件进行添加

        BKSwitchbtn = (Button)view.findViewById(R.id.button1);
        BTSwitchbtn = (Button)view.findViewById(R.id.button2);

        m_textHttp = (TextView)view.findViewById(R.id.textView_http);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void RefreshDashboard(float fValue){
        dashboardView1.setRealTimeValue(fValue);
        dashboardView1.invalidate();
    }
}
