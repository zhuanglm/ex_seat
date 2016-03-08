package com.tweebaa.ex_seat.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.tweebaa.ex_seat.R;

/**
 * Created by Zhuang on 2016-03-08.
 */
public class BatteryView extends View {

    private int mMinValue; // 最小值
    private int mMaxValue; // 最大值
    private int mModeType;

    private int measureWidth;
    private int measureHeigth;
    private RectF mBatteryRect;
    private RectF mCapRect;
    private Paint mBatteryPaint;
    private Paint mPowerPaint;
    private float mBatteryStroke = 2f;

    private float mBatteryWidth ; // 电池的宽度
    private float mBatteryLength;
    private float mCapHeight ;
    private float mCapWidth ;

    private float mPowerPadding = 1;
    private float mPowerLength ;
    private float mPowerWidth ;
    private float mPower = 0f;
    private int mPowerColor;
    private RectF mPowerRect;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BatteryView, defStyleAttr, 0);

        mPower = a.getFloat(R.styleable.BatteryView_currentValue, 0.0f);
        mBatteryWidth = a.getDimensionPixelSize(R.styleable.BatteryView_batteryWidth, 15);
        mBatteryLength = a.getDimensionPixelSize(R.styleable.BatteryView_batteryLength, 30);
        mCapWidth = mBatteryWidth/2;
        mCapHeight =  mCapWidth/3;
        mMinValue = a.getInteger(R.styleable.BatteryView_minBattery, 0);
        mMaxValue = a.getInteger(R.styleable.BatteryView_maxBattery, 100);
        mModeType = a.getInt(R.styleable.BatteryView_batteryMode, 2);
        mPowerColor = a.getColor(R.styleable.BatteryView_stripeColor, Color.GREEN);

        mPowerLength = mBatteryLength - mBatteryStroke - mPowerPadding * 2;
        mPowerWidth = mBatteryWidth - mBatteryStroke - mPowerPadding * 2;
        a.recycle();
        initView();
    }

    public void initView() {
        /**
         * 设置电池画笔
         */
        mBatteryPaint = new Paint();
        mBatteryPaint.setColor(Color.GRAY);
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setStyle(Paint.Style.STROKE);
        mBatteryPaint.setStrokeWidth(mBatteryStroke);

        /**
         * 设置电量画笔
         */
        mPowerPaint = new Paint();
        mPowerPaint.setColor(mPowerColor);
        mPowerPaint.setAntiAlias(true);
        mPowerPaint.setStyle(Paint.Style.FILL);
        mPowerPaint.setStrokeWidth(mBatteryStroke);

        /** * 设置电池矩形  */
        if(mModeType==2) {    //horizontal
            mBatteryRect = new RectF(0, 0, mBatteryLength, mBatteryWidth);
            /*** 设置电池盖矩形*/
            mCapRect = new RectF(mBatteryLength, (mBatteryWidth - mCapWidth) / 2,
                    mBatteryLength + mCapHeight, (mBatteryWidth - mCapWidth) / 2 + mCapWidth);

            mPowerRect = new RectF(mBatteryStroke / 2+mPowerPadding,mPowerPadding + mBatteryStroke / 2,
                    mBatteryStroke / 2 + mPowerPadding + mPowerLength * (mPower / (mMaxValue - mMinValue)),
                    mPowerWidth+ mBatteryStroke/2+mPowerPadding);
        }
        else{
            //mCapRect = new RectF(mBatteryLength, (mBatteryWidth - mCapWidth) / 2, mBatteryLength + mCapHeight, (mBatteryWidth - mCapWidth) / 2 + mCapWidth);
            mCapRect = new RectF((mBatteryWidth - mCapWidth) / 2, 0,(mBatteryWidth - mCapWidth) / 2 + mCapWidth, mCapHeight);
            mBatteryRect = new RectF(0, mCapHeight, mBatteryWidth,mBatteryLength+mCapHeight);
            mPowerRect = new RectF(mBatteryStroke / 2+mPowerPadding,
                    mBatteryStroke / 2 + mPowerPadding + mPowerLength * ((mMaxValue - mMinValue - mPower) / (mMaxValue - mMinValue)) ,
                    mPowerWidth+ mBatteryStroke/2+mPowerPadding,
                    mCapHeight+mBatteryStroke/2+mPowerPadding+mPowerLength);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (mModeType==2)
            canvas.translate(measureWidth / 2- mBatteryLength/2, measureHeigth / 2 - mBatteryWidth/2);
        else
            canvas.translate(measureWidth / 2- mBatteryWidth/2, measureHeigth / 2 - (mBatteryLength+mCapHeight)/2);
        canvas.drawRoundRect(mBatteryRect, 2f, 2f, mBatteryPaint); // 画电池轮廓需要考虑 画笔的宽度
        canvas.drawRoundRect(mCapRect, 2f, 2f, mBatteryPaint);// 画电池盖
        canvas.drawRect(mPowerRect, mPowerPaint);

        canvas.restore();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeigth);
    }


    public void setValue(float value) {
        mPower = value;
        if (mPower < 0) {
            mPower = 0;
        }

        if(mModeType==2) {    //horizontal

            mPowerRect = new RectF(mBatteryStroke / 2+mPowerPadding,mPowerPadding + mBatteryStroke / 2,
                    mBatteryStroke / 2 + mPowerPadding + mPowerLength * (mPower / (mMaxValue - mMinValue)),
                    mPowerWidth+ mBatteryStroke/2+mPowerPadding);
        }
        else{

            mPowerRect = new RectF(mBatteryStroke / 2+mPowerPadding,
                    mBatteryStroke / 2 + mPowerPadding + mPowerLength * ((mMaxValue - mMinValue - mPower) / (mMaxValue - mMinValue)) ,
                    mPowerWidth+ mBatteryStroke/2+mPowerPadding,
                    mCapHeight+mBatteryStroke/2+mPowerPadding+mPowerLength);
        }
        invalidate();
    }
}
