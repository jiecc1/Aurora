package com.example.kain.aurora.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangs on 2018/3/29.
 */

public class LineChartView extends View {

    private int viewWidth;
    private int viewHight;
    private int ylabelCount = 7;
    private int time;

    private float top;
    private float bottom;
    private float left;
    private float right;

    private Paint linePaint;
    private TextPaint textPaint;
    private Path mpath;

    private List<Float> yAxis = new ArrayList<>();

    public LineChartView(Context context) {
        super(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    /**
     * 只需要初始化一次
     */
    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1f);//画笔的宽度

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
        textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("(ms)", right + 20, bottom + 20, textPaint);
        for (int i = 0; i < 6; i++) {
            canvas.drawText(String.valueOf(time - i * 4000), left + i * viewWidth * 14 / 16f / 5, bottom + 20, textPaint);
        }
        canvas.restore();

        if (yAxis != null && yAxis.size() > 0) {
            canvas.save();
            mpath.moveTo(getX(yAxis.size()), getY(yAxis.get(0)));
            for (int i = 1; i < yAxis.size(); i++) {
                if (yAxis.get(i - 1) != yAxis.get(i)) {
                    mpath.lineTo(getX(yAxis.size() - i + 1), getY(yAxis.get(i)));
                    mpath.lineTo(getX(yAxis.size() - i), getY(yAxis.get(i)));
                }

            }
            canvas.drawPath(mpath, linePaint);
            canvas.restore();
        }
    }

    //    获得X轴坐标
    private float getX(int i) {
        float x = left + viewWidth * 14 / 16f / 500 * i;
        return x;
    }

    private float getY(float i) {
        float y = bottom - viewHight * 12 / 16f / 60 * i;
        return y;
    }

    public void setAxis(int time, List<Float> y) {
        this.time = time;
        this.yAxis = y;
        invalidate();
    }

}
