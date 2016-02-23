package com.example.ex_seat;

import android.content.Context; 
import android.graphics.Canvas; 
import android.graphics.Color; 
import android.graphics.Paint; 
import android.view.View; 
                                                                 
public class DataGraph extends View{ 
    private int XPoint = 60; 
    private int YPoint ;
    private int XScale = 20;  //刻度长度 
    private int YScaleRange = 8;
    private int YScale ; 
    private int XLength ; 
    private int YLength ;
                                                                     
     

    private Paint paint = new Paint();
    private Paint chart_paint = new Paint();
    private Paint green_paint = new Paint();
                                                                 
                                                                     
    private String[] YLabel = new String[YScaleRange]; 
                                                                     
    /*private Handler handler = new Handler(){ 
        public void handleMessage(Message msg) { 
            if(msg.what == 0x1234){ 
                DataGraph.this.invalidate(); 
            } 
        }; 
    }; */
    public DataGraph(Context context) { 
        super(context); 
        for(int i=0; i<YLabel.length; i++){ 
            YLabel[i] = Integer.toString(i) ; 
        } 
                
        /*new Thread(new Runnable() { 
                                                                             
            @Override 
            public void run() { 
                while(true){ 
                    try { 
                        Thread.sleep(1000); 
                    } catch (InterruptedException e) { 
                        e.printStackTrace(); 
                    } 
                    if(data.size() >= MaxDataSize){ 
                        data.remove(0); 
                    } 
                    data.add(new Random().nextInt(4) + 1); 
                    activity.handler.sendEmptyMessage(0x1234); 
                } 
            } 
        }).start(); */
    } 
                                                                     
    @Override 
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
         
        YLength = this.getHeight()-40;
        YPoint = YLength+40;
        YScale = YLength/YScaleRange;
        XLength = this.getWidth();
        DataDef.MaxDataSize = XLength / XScale;
        
        
        paint.setStyle(Paint.Style.STROKE); 
        paint.setStrokeWidth(2);
        paint.setTextSize(18);
        paint.setAntiAlias(true); //去锯齿 
        paint.setColor(Color.BLUE); 
        chart_paint.setStrokeWidth(3);
        chart_paint.setColor(Color.RED);
        chart_paint.setAntiAlias(true);
        green_paint.setStrokeWidth(1);
        green_paint.setColor(Color.GREEN);
        green_paint.setTextSize(20);
        green_paint.setAntiAlias(true);
                                                                         
        //画Y轴 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);
                                                                         
        //Y轴箭头 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 5, YPoint-YLength + 15, paint);  //箭头 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 5, YPoint-YLength + 15 ,paint); 
                                                                         
        //添加刻度和文字 
        for(int i=0; i  < YScaleRange; i++) {
            float y = YPoint - i * YScale;
        	if(i>0) {
                canvas.drawLine(XPoint, y, XPoint + XLength, y, green_paint);  //刻度
            }
                                                                             
            canvas.drawText(YLabel[i], XPoint - 50, YPoint - i * YScale, paint);//文字 
        }
        canvas.drawText("×100 r/Min", 10, 20, green_paint);
        //canvas.drawLine(XPoint, 311, XPoint + 1000, 311, green_paint);                                                                  
        //画X轴 
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); 
        //System.out.println("Data.size = " + DataDef.dataFromDev.size());
        if(DataDef.dataFromDev.size() > 1){ 
            for(int i=1; i<DataDef.dataFromDev.size(); i++){ 
                canvas.drawLine(XPoint + (i-1) * XScale, YPoint - (float)((DataDef.dataFromDev.get(i-1)*0.01-0.01) * YLength/YScaleRange),  
                        XPoint + i * XScale, (float)(YPoint - (DataDef.dataFromDev.get(i)*0.01-0.01)* YLength/YScaleRange), chart_paint); 
            } 
        } 
    } 
}
