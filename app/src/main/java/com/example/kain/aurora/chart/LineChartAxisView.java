package com.example.kain.aurora.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangs on 2018/3/29.
 */

public class LineChartAxisView extends View {

    private int viewWidth;
    private int viewHight;
    private int ylabelCount = 7;

    private float top;
    private float bottom;
    private float left;
    private float right;

    private Paint linePaint;
    private TextPaint textPaint;
    private Path mpath;

    public LineChartAxisView(Context context) {
        super(context);
    }

    public LineChartAxisView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartAxisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHight = h;

        left = viewWidth * (1 / 16f);
        right = viewWidth * (15 / 16f);
        top = viewHight * (2 / 16f);
        bottom = viewHight * (14 / 16f);
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);//画笔的粗细
        linePaint.setStrokeWidth(1);//画笔的宽度

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(10);

        mpath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();

        canvas.drawColor(Color.TRANSPARENT);
        canvas.save();
        mpath.moveTo(left, top);
        mpath.lineTo(left, bottom);
        mpath.lineTo(right, bottom);
        canvas.drawPath(mpath, linePaint);

//        绘制Y轴坐标
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("(mV)", left - 20, top - 15, textPaint);
        for (int i = 0; i < ylabelCount; i++) {
            canvas.drawText(String.valueOf(10 * i), left - 20, bottom - i * viewHight * 12 / 16f / 6, textPaint);
        }
        canvas.restore();
    }
}
