package proj.android.gl_shapes;

import android.content.Context;
import android.opengl.GLES20;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import proj.android.exceptions.BlockExecution;
import proj.android.exceptions.FilterException;
import proj.android.gl_helper.GlRenderer;
import proj.android.gl_helper.RawFileReader;
import proj.android.imagefilter.R;

/**
 * Created by root on 12/6/15.
 */
public class ShapeSquare {

    private ShapeSquare(Context context){this.context = context;}

    private FloatBuffer vertextBuffer;
    private ShortBuffer drawBuffer;

    private Context context;
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
    public static ShapeSquare getInstance(Context context){
        if(shapeSquare == null)
            shapeSquare=new ShapeSquare(context);
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

        loadShaders();
    }

    private int mProgram;
    private final void loadShaders(){
        /*int vertextShader = GlRenderer.loadShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.simple_vertex_shader));
        int fragmentShader = GlRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.simple_fragment_shader));

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertextShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);*/
    }
    private final String getShader(int shaderId){
        String shader =null;
        /*try {
            shader = RawFileReader.getShaderFromRaw(context, shaderId);
        } catch (IOException e) {
            throw new BlockExecution("reading raw file",e);
        }*/
        return shader;
    }

    private int mPositionHandle;
    private int mColorHandle;

    private int vertextCount = squareCoOrdinates.length / coOrdinatePerVertext;
    private int vertextStrider = coOrdinatePerVertext * 4;

    public final void drawSquareShape(){
        GLES20.glUseProgram(mProgram);

/*        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, coOrdinatePerVertext, GLES20.GL_FLOAT,
                false, vertextStrider, vertextBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_POLYGON_OFFSET_UNITS, 0, vertextCount);


        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertextCount, GLES20.GL_UNSIGNED_SHORT, drawBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);*/
    }

}
