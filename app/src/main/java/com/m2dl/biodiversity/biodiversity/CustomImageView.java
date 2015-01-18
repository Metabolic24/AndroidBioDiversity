package com.m2dl.biodiversity.biodiversity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.ArrayList;


public class CustomImageView extends ImageView {

    private ArrayList<RectF> rectList;
    private RectF current;
    private Paint finalPaint, drawingPaint;

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectList = new ArrayList<>();
        finalPaint = new Paint();
        finalPaint.setColor(Color.RED);
        finalPaint.setStyle(Paint.Style.STROKE);
        finalPaint.setStrokeWidth(3);

        drawingPaint = new Paint();
        //drawingPaint.setColor(Color.GRAY); //Commenté si un seul élément
        drawingPaint.setColor(Color.RED);
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeWidth(3);
    }

    public void addRectangle(RectF rectangle, boolean isFinal) {
        rectList.add(rectangle);
        if (isFinal) {
            current = null;
        } else {
            current = rectangle;
        }
    }

    public void removeRectangle() {
        if (current != null) {
            rectList.remove(current);
            current = null;
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        for (RectF currentRect : rectList) {
            if (currentRect.equals(current)) {
                canvas.drawRect(currentRect, drawingPaint);
            } else {
                canvas.drawRect(currentRect, finalPaint);
            }
        }
    }
}
