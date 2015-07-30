package proj.android.gl_shapes;

import android.content.Context;
import android.opengl.GLES20;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.exceptions.BlockExecution;
import proj.android.exceptions.FilterException;
import proj.android.gl_helper.GlDecorator;
import proj.android.gl_helper.GlRenderer;
import proj.android.gl_helper.RawFileReader;
import proj.android.gl_helper.ShaderType;
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
    private static int[] triangleCoOrdinates = {
            1, 1, 0,
            -1,-1, 0,
            1, -1, 0
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
       // vertextBuffer.put(triangleCoOrdinates);
        vertextBuffer.position(0);

        loadShaders();
    }

    private final void loadShaders(){

        int vertexShader = GlDecorator.loadShader(ShaderType.VERTEX_SHADER,
                getShaderString(R.raw.simple_vertex_shader));
        int fragmentShader = GlDecorator.loadShader(ShaderType.FRAGMENT_SHADER,
                getShaderString(R.raw.simple_fragment_shader));
       /* String vertextSh =getShaderString(R.raw.simple_vertex_shader);
        String fragSh =getShaderString(R.raw.simple_fragment_shader);
        Utils.logError(vertextSh);
        Utils.logError(fragSh);

        int verObj =GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(verObj, vertextSh);
        GLES20.glCompileShader(verObj);

        int frgObj =GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(frgObj, fragSh);
        GLES20.glCompileShader(frgObj);*/

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);

    }
    private final String getShaderString(int shaderId){
        String shader = "";
        /*try{
            shader = RawFileReader.getShaderFromRaw(context, shaderId);
        }catch(IOException e){
            throw new BlockExecution("reading raw file",e);
        }*/
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

    private final float[] vertexTriangle = {
            0.4f, 0.25f, 0.0f, 1.0f,
            -0.4f, -0.33f, 0.0f, 1.0f,
            0.8f, -0.4f, 0.0f, 1.0f,
            0.4f, 0.25f, 0.0f, 1.0f,
            -0.4f, -0.33f, 0.0f, 1.0f,
            0.8f, -0.4f, 0.0f, 1.0f
    };


    public final void initVertexBuffer(){
        GLES20.glUseProgram(mProgram);
        final int[] buffers=new int[1];
        //generate buffer objects
        GLES20.glGenBuffers(1, buffers, 0);
        //bind buffer object
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);

        //get float buffer from vertices
        FloatBuffer floatBuffer = genFloatBufferFromVertex(vertexTriangle, 4);

        //put parameteres to generated buffer via FloatBuffer
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                floatBuffer.capacity() * 4, floatBuffer, GLES20.GL_STATIC_DRAW);

        //enable
        GLES20.glEnableVertexAttribArray(0);

        //tell opengl what to do with buffer data
        GLES20.glVertexAttribPointer(0, 4, GLES20.GL_FLOAT, false, 0, 0);


        //from start index to number of indexes to read
        //GLES20.GL_LINES shows the type
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 6);

        /*GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        IntBuffer floatBuffer1=genFloatBufferFromVertexInt(triangleCoOrdinates, 4);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer1.capacity() * 4, floatBuffer1,
                GLES20.GL_STATIC_DRAW);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_INT, false, 0, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        //cleaning the bounded buffer
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);*/
    }

    private final FloatBuffer genFloatBufferFromVertex(float[] vertex, int sizeOfBytePerVertex){
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(vertex.length * sizeOfBytePerVertex);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer=byteBuffer.asFloatBuffer();
        floatBuffer.put(vertex);
        floatBuffer.position(0);
        return floatBuffer;
    }
    private final IntBuffer genFloatBufferFromVertexInt(int[] vertex, int sizeOfBytePerVertex){
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(vertex.length * sizeOfBytePerVertex);
        byteBuffer.order(ByteOrder.nativeOrder());
        IntBuffer floatBuffer=byteBuffer.asIntBuffer();
        floatBuffer.put(vertex);
        floatBuffer.position(0);
        return floatBuffer;
    }

}
