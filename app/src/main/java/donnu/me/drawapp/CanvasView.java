package donnu.nikasov.drawapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Миша on 08.03.2017.
 */

public class CanvasView extends View{


    private Bitmap bitmap;
    private Canvas canvas;
    private Context context;

    boolean firstStart = true;

    private int color;
    private int strokeWidth;

    Paint paint2;

    private List<Brush> allBrush;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        strokeWidth = 5;
        color = Color.BLACK;

        allBrush = new ArrayList<>();
        allBrush.add(new Brush(color, 4f));

        paint2 = new Paint();
        paint2.setColor(Color.WHITE);

        setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (firstStart) {
            firstStart=false;
        }

        canvas.drawRect(0, 0 , 720, 1280,paint2);

        for (Brush brush: allBrush) {
            canvas.drawPath(brush.getPath(), brush.getPaint());
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                allBrush.add(new Brush(color, strokeWidth));
                allBrush.get(allBrush.size() - 1).getPath().moveTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                allBrush.get(allBrush.size() - 1).getPath().lineTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    public void clearCanvas(){

        firstStart = true;
        paint2.setColor(Color.WHITE);

        for (Brush brush: allBrush) {
            brush.getPath().reset();
        }
        invalidate();
    }

    public void setSelectedColor(int color){
        this.color = color;
    }

    public void setStrokeWidth(int strokeWidth){
        this.strokeWidth = strokeWidth;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void saveFile(String fileName){

        try {

            FileOutputStream fileOutputStream = new FileOutputStream
                    (new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                     , (fileName +".png")));

            getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeLastPath(){

        if (allBrush.size()>1) {
            allBrush.get(allBrush.size() - 1).getPath().reset();
            allBrush.remove(allBrush.get(allBrush.size() - 1));
        }
        invalidate();
    }

    public void setSelectedBackgroundColor(int color){
        firstStart = true;
        paint2.setColor(color);
        invalidate();
    }

    public void setImageOnView(String path){
        bitmap = BitmapFactory.decodeFile(path);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);

        canvas = new Canvas(bitmap);

        System.out.println( "Screen = " + (Resources.getSystem().getDisplayMetrics().widthPixels) +
                ", " + (Resources.getSystem().getDisplayMetrics().heightPixels));
    }

}
