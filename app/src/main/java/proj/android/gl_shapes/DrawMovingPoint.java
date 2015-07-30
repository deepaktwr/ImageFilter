package proj.android.gl_shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;
import proj.android.helper.Utils;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_STREAM_DRAW;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glDisableVertexAttribArray;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by root on 30/6/15.
 */
public class DrawMovingPoint {
    private float c(float val){
        return val/100f;
    }
    public void drawMovingPoint(){
        FloatBuffer buffData = getDirectFloatBufferForPoint();
        FloatBuffer buffDataColor = getDirectFloatBufferForColor();
        IntBuffer binObj = IntBuffer.allocate(2);
        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(2, binObj);
        glBindBuffer(GL_ARRAY_BUFFER, binObj.get(0));
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STREAM_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, binObj.get(1));
        glBufferData(GL_ARRAY_BUFFER, buffDataColor.capacity() * 4, buffDataColor, GL_STREAM_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_POINTS, 0, 10);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        /*glUseProgram(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);*/
        //movePoint(buffData, buffDataColor, binObj);

    }
    private float angle = 0.0f;
    private void movePoint(FloatBuffer buffData, FloatBuffer buffDataColor, IntBuffer binObj){
        updatePointPosition(buffData);
        //glUseProgram(GlDecorator.getProgramObject());
        glBindBuffer(GL_ARRAY_BUFFER, binObj.get(0));
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffData.capacity() * 4, buffData);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, binObj.get(1));
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffDataColor.capacity() * 4, buffDataColor);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_POINTS, 0, 10);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        /*glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);*/
        /*cc++;
        if(cc<50)
            movePoint(buffData, buffDataColor, binObj);*/
    }
    private void updatePointPosition(FloatBuffer buffData){
        angle+=0.05;
        float moduleAngle = angle % 2f;
        float radius = c(40f);
        float x = radius * (float)Math.sin((Math.PI * moduleAngle));
        float y = radius * (float)Math.cos((Math.PI * moduleAngle));
        Utils.logError("x=" + x + " y=" + y);
        //36,37
        buffData.put(36, x);
        buffData.put(37, y);
        buffData.position(0);
    }
    private FloatBuffer getDirectFloatBufferForPoint(){
        float[] point={


                c(0),c(0),1f,1f,
                c(0),c(40),1f,1f,
                c(-40),c(0),1f,1f,
                c(0),c(-40),1f,1f,
                c(40),c(0),1f,1f,

                c(-28.2842f),c(28.2842f),1f,1f,
                c(-28.2842f),c(-28.2842f),1f,1f,
                c(28.2842f),c(-28.2842f),1f,1f,
                c(28.2842f),c(28.2842f),1f,1f,

                c(0),c(40),1f,1f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(point.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffData = byteBuffer.asFloatBuffer();
        buffData.put(point);
        buffData.position(0);
        return buffData;
    }
    private FloatBuffer getDirectFloatBufferForColor(){
        float[] color={

                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1.f,0.2f,0.2f,1f,
                1f,1f,1f,1f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffData = byteBuffer.asFloatBuffer();
        buffData.put(color);
        buffData.position(0);
        return buffData;
    }
}
