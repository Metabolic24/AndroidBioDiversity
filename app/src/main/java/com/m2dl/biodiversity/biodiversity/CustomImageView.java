package com.m2dl.biodiversity.biodiversity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;


public class CustomImageView extends ImageView implements View.OnTouchListener {

    private ArrayList<RectF> rectList;
    private RectF current = null;
    private Paint finalPaint, drawingPaint;
    private float srcX, srcY, destX, destY = -1f;
    private KeyLauncher launcher;

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    //Constructeurs

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

        setOnTouchListener(this);

        finalPaint = new Paint();
        finalPaint.setColor(Color.RED);
        finalPaint.setStyle(Paint.Style.STROKE);
        finalPaint.setStrokeWidth(3);

        drawingPaint = new Paint();
        //drawingPaint.setColor(Color.GRAY); //Commenté si un seul élément
        drawingPaint.setColor(Color.RED);    //Commenté si plusieurs éléments
        drawingPaint.setStyle(Paint.Style.STROKE);
        drawingPaint.setStrokeWidth(3);
    }

    /*Méthode d'initialisation*/

    /**
     * *********
     */

    public void setLauncher(KeyLauncher launcher) {
        this.launcher = launcher;
    }


    /*************/
    /***LAUNCHER**/

    /**
     * *********
     */

    public void addRectangle(RectF rectangle) {
        rectList.add(rectangle);
        current = rectangle;
    }

    /*************/
    /**
     * RECTANGLE*
     */

    public void removeRectangle() {
        if (current != null) {
            rectList.remove(current);
            current = null;
        }
    }

    public void finalize(boolean isOK) {
        if (isOK && current != null) {
            rectList.add(current);
            invalidate();
        }
        current = null;
    }

    /*************/

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

    /*************/
    /****DESSIN***/

    /**
     * *********
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_CANCEL:
                srcX = -1f;
                srcY = -1f;
                destX = -1f;
                destY = -1f;
                current = null;
                break;
            case MotionEvent.ACTION_DOWN:
                srcX = event.getX();
                srcY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (srcX != -1f && srcY != -1f) {
                    removeRectangle();

                    destX = event.getX();
                    destY = event.getY();
                    current = new RectF(srcX, srcY, destX, destY);

                    addRectangle(current);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                removeRectangle();

                if (destX != -1f && destY != -1f) {
                    if (srcX == destX) {
                        srcX = srcX - 5;
                        destX = destX + 5;
                    }

                    if (srcY == destY) {
                        srcY = srcY - 5;
                        destY = destY + 5;
                    }
                    current = new RectF(srcX, srcY, destX, destY);
                    destX = -1f;
                    destY = -1f;
                } else {
                    current = new RectF(srcX - 5, srcY - 5, srcX + 5, srcY + 5);
                }

                srcX = -1f;
                srcY = -1f;

                launcher.launch();

                break;
        }

        return true;
    }


    /*************/
    /**
     * TACTILE**
     */
    public interface KeyLauncher {
        public void launch();
    }
}
