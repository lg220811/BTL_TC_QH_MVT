package CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Thiem on 06-Apr-16.
 */
public class Node extends View {
    //thuoc tinh de ve
    private boolean root;
    private boolean backBone;
    private Paint paint = new Paint();
    //thuoc tinh de tinh toan
    private int name;
    private int x, y, w;
    private Context context;
    boolean show;

    public Node(Context context, int x, int y, boolean root, int name) {
        super(context);
        this.context = context;
        this.x = x;
        this.y = y;
        this.name = name;
        this.root = root;
        this.show = true;
    }

    public boolean isBackBone() {
        return backBone;
    }

    public void setBackBone(boolean backBone) {
        this.backBone = backBone;
        if (backBone)
            this.root = false;
        invalidate();
    }

    public Node(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Node(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle){
        paint.setColor(root ? Color.RED : backBone ? Color.YELLOW : Color.BLACK);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(null, 0);
        int r = root?15:10;
        canvas.drawCircle(x,y,r,paint);
        if (show) {
            canvas.drawText("Node " + name, x - r, y - r, paint);
            canvas.drawText("W = " + w, x - r, y + 2 * r, paint);
        }
    }

    public void setRoot(boolean root) {
        this.root = root;
        if (root)
            this.backBone = false;
        invalidate();
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
        invalidate();
    }

    public int getXm() {
        return x;
    }

    public int getYm() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getName() {
        return name;
    }

    public boolean isRoot() {
        return root;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
        invalidate();
    }

}
