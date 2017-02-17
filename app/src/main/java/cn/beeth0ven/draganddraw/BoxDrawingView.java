package cn.beeth0ven.draganddraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Air on 2017/2/16.
 */

public class BoxDrawingView extends View {

    private Box currentBox;
    private List<Box> boxes = new ArrayList<>();
    private Paint boxPaint;
    private Paint backgroundPaint;
    private Integer pointId1, pointId2;
    private float angle = 0;

    public BoxDrawingView(Context context) {
        this(context, null);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        boxPaint = new Paint();
        boxPaint.setColor(0x22ff0000);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                pointId1 = event.getPointerId(event.getActionIndex());
                currentBox = new Box(current);
                boxes.add(currentBox);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "ACTION_POINTER_DOWN";
                pointId2 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (pointId1 != null && pointId2 != null) {
                    angle += getRotateAngleFromTouchEvent(event);
                }

                if (currentBox != null) {
                    currentBox.current = current;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                pointId1 = null;
                currentBox = null;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action = "ACTION_POINTER_UP";
                pointId2 = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                currentBox = null;
                pointId1 = null;
                pointId2 = null;
                break;
        }
        Log.i("BoxDrawingView", action + " at x=" + current.x + ", y=" + current.y);
        return true;
    }

    private float getRotateAngleFromTouchEvent(MotionEvent event) {

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.rotate((float) 45, getWidth() / 2, getHeight() / 2);

        canvas.drawPaint(backgroundPaint);

        for (Box box: boxes) {
            float left = Math.min(box.origin.x, box.current.x);
            float right = Math.max(box.origin.x, box.current.x);
            float top = Math.min(box.origin.y, box.current.y);
            float bottom = Math.max(box.origin.y, box.current.y);

            canvas.drawRect(left, top, right, bottom, boxPaint);
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("savedInstanceState", super.onSaveInstanceState());
        bundle.putParcelableArrayList("boxes", (ArrayList<Box>) boxes);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        boxes = bundle.getParcelableArrayList("boxes");
        Parcelable savedInstanceState = bundle.getParcelable("savedInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
