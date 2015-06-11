package proj.android.gl_shapes;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import proj.android.exceptions.FilterException;
import proj.android.gl_helper.GlRenderer;
import proj.android.gl_helper.RawFileReader;
import proj.android.helper.Utils;
import proj.android.imagefilter.R;

/**
 * Created by deepak on 12/6/15.
 */
public class ShapeTriangle {

    private ShapeTriangle(Context context){ this.context = context;}

    private FloatBuffer vertextBuffer;
    private Context context;

    private int mProgram;
    private static final int coOrdinatePerVertext = 3;
    private static float[] triangleCoOrdinates = {
            0.4f, 0.25f, 0.0f,
            -0.35f,-0.33f, 0.0f,
            0.80f, -0.40f, 0.0f
    };

    private float[] color = {
            0.2f, 0.2f, 0.25f, 0.25f
    };

    private static ShapeTriangle shapeTriangle;
    public static ShapeTriangle getInstance(Context context){
        if(shapeTriangle == null)
            shapeTriangle = new ShapeTriangle(context);
        shapeTriangle.init();
        return shapeTriangle;
    }

    private final void init(){
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoOrdinates.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertextBuffer = byteBuffer.asFloatBuffer();
        vertextBuffer.put(triangleCoOrdinates);
        vertextBuffer.position(0);

        loadShaders();
    }

    private final void loadShaders(){

        int vertexShader = GlRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                getShaderString(R.raw.simple_vertex_shader));
        int fragmentShader = GlRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                getShaderString(R.raw.simple_fragment_shader));

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);

    }
    private final String getShaderString(int shaderId){
        String shader = "";
        try{
            shader = RawFileReader.getShaderFromRaw(context, shaderId);
        }catch(FilterException filterException){}
        return shader;
    }

    private int mPositionHandle;
    private int mColorHandle;

    private static final int vertextCount = triangleCoOrdinates.length / coOrdinatePerVertext;
    private static final int vertextStrider = coOrdinatePerVertext * 4;
    public final void drawTriangleShape(){
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, coOrdinatePerVertext,
                GLES20.GL_FLOAT, false, vertextStrider, vertextBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertextCount);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

}
