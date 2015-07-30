package proj.android.gl_shapes;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;

import proj.android.fragments.FilterFragment;
import proj.android.gl_helper.GlDecorator;
import proj.android.helper.Utils;
import proj.android.imagefilter.R;

import static android.opengl.GLES20.*;
import static android.opengl.GLES20.glUseProgram;

/**
 * Created by deepak on 28/6/15.
 */
public class DrawMoving2DShapes {
    private float c(float val){
        return val/100f;
    }

    private float speedLimit;
    private FloatBuffer speed;
    public void movePointUsingUniform(){
        if(speedLimit==0f) {
            FloatBuffer buffData = getPointCoord();
            buffData.position(0);

            speed = FloatBuffer.allocate(1);
            speed.put(speedLimit);
            speed.position(0);

            bindBuffer = IntBuffer.allocate(1);
            glGenBuffers(1, bindBuffer);
            glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(0));
            glUseProgram(GlDecorator.getProgramObject());
            int loopDurationUniformLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "loopDuration");
            int pointsCoordUniformLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "pointsCoord");

            glUniform1f(loopDurationUniformLocation, 2f);
            // 1 = count i.e the number of elements going to modified
            glUniform4fv(pointsCoordUniformLocation, 1, buffData);

            glBufferData(GL_ARRAY_BUFFER, speed.capacity() * 4, speed, GL_STREAM_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
            glDrawArrays(GL_POINTS, 0, 1);
            cleanUp(1);

        }
        speedLimit+=0.0005;
        speed.put(0,speedLimit);
        speed.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(0));
        glUseProgram(GlDecorator.getProgramObject());
        glBufferSubData(GL_ARRAY_BUFFER, 0, speed.capacity() * 4, speed);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_POINTS, 0, 1);
        cleanUp(1);
    }
    private void cleanUp(Integer ... count){
        glBindBuffer(GL_ARRAY_BUFFER,0);
        for(int i:count)
            glDisableVertexAttribArray(i);
        glUseProgram(0);

    }
    private FloatBuffer getPointCoord(){
        float[] point={
            c(25f),c(30f),//center point
            c(40f),c(60)//initial point
        };
        FloatBuffer initialAndCenterPoint = FloatBuffer.allocate(point.length);
        initialAndCenterPoint.put(point);
        return initialAndCenterPoint;
    }

    private boolean isFirstTime =true;
    private FloatBuffer bufferData;
    private  IntBuffer bindBuffer;
    private FloatBuffer bufferDataRect;
    public void drawMultipleObjets(){

        bufferData = getDirectFloatBuffer();
        bufferDataRect=getDirectFloatBufferForRectengle();
        bufferData.position(0);
        bufferDataRect.position(0);
        bindBuffer = IntBuffer.allocate(2);
        glGenBuffers(2, bindBuffer);
        glUseProgram(GlDecorator.getProgramObject());


        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(0));
        //stream draw for multi time drawing, static for single time
        glBufferData(GL_ARRAY_BUFFER, bufferData.capacity() * 4, bufferData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(1));
        //stream draw for multi time drawing, static for single time
        glBufferData(GL_ARRAY_BUFFER, bufferDataRect.capacity() * 4, bufferDataRect, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 64);


        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glUseProgram(0);
    }
    private void movingNewObject(){
        //GlDecorator.clearWindowWithBuffers();
        computeXZForCircularMotion(bufferData);
        glUseProgram(GlDecorator.getProgramObject());
        //glGenBuffers(1, bindBuff);
        bufferData.position(0);
        bufferDataRect.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(0));
        glBufferSubData(GL_ARRAY_BUFFER, 0, bufferData.capacity() * 4, bufferData);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(1));
        glBufferSubData(GL_ARRAY_BUFFER, 0, bufferDataRect.capacity() * 4, bufferDataRect);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 4, GL_FLOAT, false, 0, 64);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glUseProgram(0);
    }
    public void drawMovingTriangle(){
        if(isFirstTime) {
            isFirstTime=false;
            bufferData = getDirectFloatBuffer();
            bufferData.position(0);
            bindBuffer = IntBuffer.allocate(1);
            glUseProgram(GlDecorator.getProgramObject());
            glGenBuffers(1, bindBuffer);
            glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(0));
            //stream draw for multi time drawing, static for single time
            glBufferData(GL_ARRAY_BUFFER, bufferData.capacity() * 4, bufferData, GL_STREAM_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
            glDrawArrays(GL_TRIANGLES, 0, 3);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glDisableVertexAttribArray(0);
            glUseProgram(0);
        }
        movingObject(bufferData, bindBuffer);

    }
    private void movingObject(FloatBuffer buffData, IntBuffer bindBuff){
        //GlDecorator.clearWindowWithBuffers();
        computeXYForCircularMotion(buffData);
        glUseProgram(GlDecorator.getProgramObject());
        //glGenBuffers(1, bindBuff);
        buffData.position(0);
        glBindBuffer(GL_ARRAY_BUFFER, bindBuff.get(0));
        //sub data because we have already bound it first time with stream draw, and RENDERING_CONTINUOSLY causes to to call
        //onDrawFrame of glSurfaceView continously so the first fuction will be called only once because we have given boolean
        //condition on it, but this function we be called again and again.
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffData.capacity() * 4, buffData);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glUseProgram(0);
    }

    private float periodRight = 0.75f; //bacause this coord has an leading +ve angle of 135 degrees == 3*PI/4, we have alreday
    //given PI so 0.75 which is 3/4 here
    private float periodTop = 0.0f; //it has angle of 0 degree
    private float periodLeft = -0.75f; //bacause this coord has an leading -ve angle of 135 degres;
    private float radius =0f;


    private float periodRightZ=0.0f;
    private float periodLeftZ=0.0f;

    private float periodRightX = 0.75f;
    private float periodLeftX = -0.75f;

    private void computeXZForCircularMotion(FloatBuffer buffData){

        periodLeftX+=0.02f;//the speed from which we want to move objectS
        periodRightX+=0.02f;
        periodRightZ+=0.02f;
        periodLeftZ+=0.02f;
        radius=c(30f);//max value from the center point 0,0 we want to move, because we are assuming the the center  is 0,0 so
        //the radius will be same for all coordinates

        float remainderX = periodRightX % 2f/* gets the remainder of 2 so that the value could not be greater than 2 otherwise it will
        cause greater value than PI * 2 in sine and cosine values*/;
        float remainderZ = periodRightZ % 2f;
        //right coord
        float x = radius*(float)Math.sin((Math.PI)*remainderX/*period*/)/**0.5f*//*magnitude*/;
        float z = radius*(float)Math.sin((Math.PI) * remainderZ/*period*/)/**0.5f*//*magnitude*/;
        buffData.put(0, x);
        buffData.put(2, z);

        remainderX = periodLeftX % 2f/* gets the remainder of 2 so that the value could not be greater than 2 otherwise it will
        cause greater value than PI * 2 in sine and cosine values*/;
        remainderZ = periodLeftZ % 2f;
        //left coord
        x = radius * (float) Math.sin((Math.PI)*remainderX/*period*/)/**0.5f*//*magnitude*/;
        z = radius*(float)Math.cos((Math.PI) * remainderZ/*period*/)/**0.5f*//*magnitude*/;
        buffData.put(8, x);
        buffData.put(10, z);

    }

    private void computeXYForCircularMotion(FloatBuffer buffData){

        periodTop+=0.02f;//the speed from which we want to move objectS
        periodLeft+=0.02f;
        periodRight+=0.02f;
        radius=c(30f);//max value from the center point 0,0 we want to move, because we are assuming the the center  is 0,0 so
        //the radius will be same for all coordinates

        float remainder = periodRight % 2f/* gets the remainder of 2 so that the value could not be greater than 2 otherwise it will
        cause greater value than PI * 2 in sine and cosine values*/;

        //right coord
        float x = radius*(float)Math.sin((Math.PI)*remainder/*period*/)/**0.5f*//*magnitude*/;
        float y = radius*(float)Math.cos((Math.PI) * remainder/*period*/)/**0.5f*//*magnitude*/;
        buffData.put(0, x);
        buffData.put(1, y);

        //top coord
        remainder = periodTop % 2f;
        x = radius*(float)Math.sin((Math.PI) * remainder/*period*/)/**0.5f*//*magnitude*/;
        y = radius * (float) Math.cos((Math.PI) * remainder/*period*/)/**0.5f*//*magnitude*/;
        buffData.put(4, x);
        buffData.put(5, y);

        //left coord
        remainder = periodLeft % 2f;
        x = radius*(float)Math.sin((Math.PI) * remainder/*period*/)/**0.5f*//*magnitude*/;
        y = radius * (float) Math.cos((Math.PI) * remainder/*period*/)/**0.5f*//*magnitude*/;
        buffData.put(8, x);
        buffData.put(9, y);

    }

    /**
     * direct float buffer uses java native apis and do not tak part in GC
     * so fast.
     * */
    private FloatBuffer getDirectFloatBuffer(){
        float[] trinangleCoord={
                c(30f/1.412f),c(-30f/1.412f),-1f,1f,
                c(0),c(30),-1f,1f,
                c(-30f/1.412f),c(-30f/1.412f),-1f,1f,

                1f,1f,1f,1f,
                1f,1f,1f,1f,
                1f,1f,1f,1f,

        };
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(trinangleCoord.length * 4);
        //for current platform byte order, default is LITTLE_ENDIAN
        directBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffData = directBuffer.asFloatBuffer();
        buffData.put(trinangleCoord);
        return buffData;
    }
    private FloatBuffer getDirectFloatBufferForRectengle(){
        float[] rectCoordWithColor={
                c(10f/1.412f),c(10f/1.412f),0.f,1f,
                c(10f/1.412f),c(-10f/1.412f),0.f/*z will be above the triangle*/,1f,
                c(-10f/1.412f),c(10f/1.412f),.0f,1f,
                c(-10f/1.412f),c(-10f/1.412f),.0f,1f,

                0f,0f,0f,0f,
                0f,0f,0f,0f,
                0f,0f,0f,0f,
                0f,0f,0f,0f,
        };
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(rectCoordWithColor.length * 4);
        //for current platform byte order, default is LITTLE_ENDIAN
        directBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer buffData = directBuffer.asFloatBuffer();
        buffData.put(rectCoordWithColor);
        return buffData;
    }
    int count=0;
    public void drawMovingPoint(){
        if(count==0) {
            count++;
            Utils.logError("calling");
            buffData = getDirectFloatBufferForPoint();
            buffDataColor = getDirectFloatBufferForColor();
            binObj = IntBuffer.allocate(2);

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
            glUseProgram(0);
        }

        movePoint(buffData, buffDataColor, binObj);
        //startTimer(buffData, buffDataColor, binObj);

    }
    private float angle = 0.0f;
    /*private long myTime = System.currentTimeMillis();*/
    private void movePoint(FloatBuffer buffData, FloatBuffer buffDataColor, IntBuffer binObj){
        count++;
        updatePointPosition(buffData);
        glUseProgram(GlDecorator.getProgramObject());
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
        glUseProgram(0);
        //for chaging the navigation mode to stop rendering
        /*if(cc>30){
            Utils.getSurfaceView().setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }*/
    }
    private void updatePointPosition(FloatBuffer buffData){
        //acceelaration motion
        //angle+=((float)((float)(System.currentTimeMillis() - myTime)/1000f)/10f);
        //lenear motion
        angle+=0.02;

        float moduleAngle = angle % 2f;
        float radius = c(40f);
        float x = radius * (float)Math.sin((Math.PI * moduleAngle));
        float y = radius * (float)Math.cos((Math.PI * moduleAngle));
        //float z = radius * (float)Math.sin((Math.PI * moduleAngle));
        Utils.logError("x=" + x + " y=" + y);
        //36,37
        buffData.put(36, x);
        buffData.put(37, y);
        //buffData.put(38, z);
        //buffData.position(0);
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
    private FloatBuffer buffData;
    private FloatBuffer buffDataColor;
    private IntBuffer binObj;
}
