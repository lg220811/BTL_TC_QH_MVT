package CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Thiem on 13-Apr-16.
 */
public class Line extends View {
    //thuoc tinh
    private boolean isBackBoneToRoot;
    private Paint paint = new Paint();
    private int startX, startY;
    private int endX, endY;

    public Line(Context context, int sX, int eX, int sY, int eY,boolean isBackBoneToRoot){
        super(context);
        this.startX = sX;
        this.endX = eX;
        this.startY = sY;
        this.endY = eY;
        this.isBackBoneToRoot = isBackBoneToRoot;
        init(null, 0);
    }

    public Line(Context context, Node start, Node end, boolean isBack){
        super(context);
        this.startX = start.getXm();
        this.startY = start.getYm();
        this.endX = end.getXm();
        this.endY = end.getYm();
        this.isBackBoneToRoot = isBack;
        init(null,0);
    }
    public Line(Context context) {
        super(context);
    }

    public Line(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Line(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle){
        paint.setColor(isBackBoneToRoot ? Color.RED : Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawLine(startX, startY, endX, endY, paint);
//        canvas.rotate(45, startX, startY);
//        canvas.drawText("heelo", startX,startY,paint);
    }
}
