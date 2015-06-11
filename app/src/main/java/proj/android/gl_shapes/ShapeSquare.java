package proj.android.gl_shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by root on 12/6/15.
 */
public class ShapeSquare {

    private ShapeSquare(){}

    private FloatBuffer vertextBuffer;
    private ShortBuffer drawBuffer;
    private static final int coOrdinatePerVertext = 3;
    private static float[] squareCoOrdinates = {
            0.35f, 0.25f, 0.0f,
            -0.40f,0.20f, 0.0f,
            -0.80f, -0.60f, 0.0f,
            0.25f, -0.85f, 0.0f
    };

    private static final short[] drawOrder = {
        0,1,2,0,2,3
    };


    private float[] color = {
            0.3f, 0.25f, 0.25f, 0.25f
    };

    private static ShapeSquare shapeSquare;
    public static ShapeSquare getInstance(){
        if(shapeSquare == null)
            shapeSquare=new ShapeSquare();
        shapeSquare.init();
        return shapeSquare;
    }

    private final void init(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(squareCoOrdinates.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertextBuffer=byteBuffer.asFloatBuffer();
        vertextBuffer.put(squareCoOrdinates);
        vertextBuffer.position(0);

        ByteBuffer order=ByteBuffer.allocate(drawOrder.length * 2);
        order.order(ByteOrder.nativeOrder());

        drawBuffer=order.asShortBuffer();
        drawBuffer.put(drawOrder);
        drawBuffer.position(0);

    }

}
