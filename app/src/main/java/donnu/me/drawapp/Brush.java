package donnu.nikasov.drawapp;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Миша on 08.03.2017.
 */

public class Brush {
    private Path path;
    private Paint paint;

    public Brush(int color, float strokeWith) {

        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(strokeWith);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        return paint;
    }
}
