package com.example.analogclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;



import java.util.Calendar;

import static android.graphics.Color.WHITE;

public class CustomAnalogClock extends View {
    private int mHeight, mWidth = 0;
    private int mPadding = 0;
    private int mNumeralSpacing = 0;
    private int mHandTruncation, mHourHandTruncation = 0, mMilliHandTruncation = 0;
    private int mRadius = 0, miliRadius = 0;
    private Paint mPaint;
    private Rect mRect = new Rect();
    private boolean isInit;
    private int[] mClockHours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initializeClock();
        }
        canvas.drawColor(Color.BLACK);
        drawCircleBorder(canvas);
        drawMiliCircleBorder(canvas);
        drawClockCenter(canvas);
        drawMiliClockCenter(canvas);
        drawNumericHourBorder(canvas);
        drawClockHands(canvas);
        drawOuterCircleBorder(canvas);
        postInvalidateDelayed(500);
        invalidate();
    }

    private void initializeClock() {
        mPaint = new Paint();
        mHeight = getHeight();
        mWidth = getWidth();
        mPadding = mNumeralSpacing + 50; // spacing from circle border
        int minAttr = Math.min(mHeight, mWidth);
        mRadius = minAttr / 2 - mPadding;
        miliRadius = minAttr / 6 - mPadding;
        // for maintaining different heights among the clock hands
        mHandTruncation = minAttr / 20;
        mHourHandTruncation = minAttr / 8;
        mMilliHandTruncation = minAttr / 40;
        isInit = true;
    }

    private void SharedClockColor() {
        SharedPreferences colors = this.getContext().getSharedPreferences("clock", Context.MODE_PRIVATE);
        int clockColor = colors.getInt("basicColor", WHITE);
        mPaint.setColor(clockColor);
    }

    private void SharedHandColor() {
        SharedPreferences colors = this.getContext().getSharedPreferences("clock", Context.MODE_PRIVATE);
        int handColor = colors.getInt("handColor", WHITE);
        mPaint.setColor(handColor);
    }

    private void drawOuterCircleBorder(Canvas canvas) {
        mPaint.reset();
        SharedClockColor();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mPadding, mPaint);
    }

    private void drawCircleBorder(Canvas canvas) {
        mPaint.reset();
        SharedClockColor();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[] {12, 39}, 0));
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mPadding - 10, mPaint);
    }

    private void drawMiliCircleBorder(Canvas canvas) {
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        SharedClockColor();
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        canvas.drawCircle((2 * mWidth) / 3, mHeight / 2  , miliRadius + mPadding - 10, mPaint);
    }

    private void drawClockCenter(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        SharedClockColor();
        canvas.drawCircle(mWidth / 2, mHeight / 2, 12, mPaint);
    }

    private void drawMiliClockCenter(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        SharedClockColor();
        canvas.drawCircle((2 * mWidth) / 3, mHeight / 2, 12, mPaint);
    }

    private void drawNumericHourBorder(Canvas canvas) {
        int fontSize = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);
        SharedHandColor();
        for (int hour : mClockHours) {
            String tmp = String.valueOf(hour);
            mPaint.getTextBounds(tmp, 0, tmp.length(), mRect); // for circle-wise bounding
            double angle = Math.PI / 6 * (hour - 3); // as mathematical rule
            int x = (int) (mWidth / 2 + Math.cos(angle) * mRadius - mRect.width() / 2);
            int y = (int) (mHeight / 2 + Math.sin(angle) * mRadius + mRect.height() / 2);
            canvas.drawText(String.valueOf(hour), x, y, mPaint);
        }
    }

    private void drawClockHands(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHandLine(canvas, (hour + calendar.get(Calendar.MINUTE) / 60) * 5f, true, false);
        drawHandLine(canvas, calendar.get(Calendar.MINUTE), false, false);
        drawHandLine(canvas, calendar.get(Calendar.SECOND), false, true);
        drawMilliHandLine(canvas, calendar.get(Calendar.MILLISECOND) / 16.89, true);
    }

    private void drawHandLine(Canvas canvas, double moment, boolean isHour, boolean isSecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int handRadius =
                isHour ? mRadius - mHandTruncation - mHourHandTruncation : mRadius - mHandTruncation;
        if (isSecond)
        SharedHandColor();
        canvas
                .drawLine(mWidth / 2, mHeight / 2, (float) (mWidth / 2 + Math.cos(angle) * handRadius),
                        (float) (mHeight / 2 + Math.sin(angle) * handRadius), mPaint);
    }

    private void drawMilliHandLine(Canvas canvas, double moment, boolean isMillisecond) {
        double angle = Math.PI * moment / 30 - Math.PI / 2;
        int millihandRadius = miliRadius - mMilliHandTruncation;
        if (isMillisecond)
        SharedHandColor();
        canvas
                .drawLine((2 * mWidth) / 3, mHeight / 2, (float) ((2 * mWidth) / 3 + Math.cos(angle) * millihandRadius),
                        (float) ((mHeight) / 2 + Math.sin(angle) * millihandRadius), mPaint);
    }
}

