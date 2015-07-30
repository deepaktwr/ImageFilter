package proj.android.gl_shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;
import proj.android.helper.Utils;

import static android.opengl.GLES30.*;

/**
 * Created by deepak on 23/7/15.
 */
public class DrawNewPerspective {
    private final float c(float value){
        return (value/100f);
    }



    private void drawTrianlges(FloatBuffer vertexData, IntBuffer bindData){
        glBindBuffer(GL_ARRAY_BUFFER, bindData.get());
        glBufferData(GL_ARRAY_BUFFER, vertexData.capacity()*4, vertexData, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }
    private void setUniformObjects(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);

        int offsetLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");
        int zNearLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "zNear");
        int zFarLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "zFar");
        int scaleXYLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "scaleXY");

        FloatBuffer offsetValue = FloatBuffer.allocate(2);
        //we need to give offset value in -ve so that it can be seen bigger in that direction
        offsetValue.put(0f);
        offsetValue.put(0f);
        offsetValue.position(0);

        glUniform2fv(offsetLocation, 1, offsetValue);
        glUniform1f(zNearLocation, 1f);
        glUniform1f(zFarLocation, 3f);
        glUniform1f(scaleXYLocation, 1f);
    }
    private void setUniformMatrixObject(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);

        int offsetLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");
        int matrixLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "cameraToClipMatrix");


        FloatBuffer offsetValue = FloatBuffer.allocate(2);
        //we need to give offset value in -ve so that it can be seen bigger in that direction
        offsetValue.put(0f);
        offsetValue.put(0f);
        offsetValue.position(0);

        setUniformMatrixForCameraSpace();

        glUniform2fv(offsetLocation, 1, offsetValue);
        glUniformMatrix4fv(matrixLocation, 1, false, matrixBuffer);

    }
    private FloatBuffer matrixBuffer;
    private void setUniformMatrixForCameraSpace(){
        //the z value we are sending here will become in -ve, because our coordinates in ive dimentions we need to give near
        //and far values as -ve in terms
        float N = 1f;
        float F = 3f;
        float firstZCoefficient = (N+F)/(N-F);
        float secondZCoefficient = 2f*N*F/(N-F);
        float scaleValue =1f;
        float xScale = scaleValue/((float)GlDecorator.getScreenWidthMeasure()/(float)GlDecorator.getScreenHeightMeasure());
        float[] values = {
                scaleValue,0,0,0,
                0,scaleValue,0,0,
                0,0,firstZCoefficient,-1f,
                0,0,secondZCoefficient,0
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(values.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        matrixBuffer = byteBuffer.asFloatBuffer();
        matrixBuffer.put(values);
        matrixBuffer.position(0);
    }
    private FloatBuffer front1, front2, front3, front4, back1, back2, top1, top2, bottom1, bottom2, left1, left2, right1, right2;
    public void drawPerspectiveWithoutMatrices(){


        setFrontFaceAsCW();
        setTopFaceAsCW();
        setRightFaceAsCW();

        setFrontFaceAsCW1();
        setTopFaceAsCW1();
        setRightFaceAsCW1();

        glUseProgram(GlDecorator.getProgramObject());
        IntBuffer bindData = IntBuffer.allocate(12);
        bindData.position(0);
        glGenBuffers(12, bindData);

        //setUniformObjects();
        setUniformMatrixObject();

        //bottom left
        drawTrianlges(front1, bindData);
        drawTrianlges(front2, bindData);
        //drawTrianlges(back1, bindData);
        //drawTrianlges(back2, bindData);
        drawTrianlges(top1, bindData);
        drawTrianlges(top2, bindData);
        drawTrianlges(right1, bindData);
        drawTrianlges(right2, bindData);


        //top right
        drawTrianlges(front3, bindData);
        drawTrianlges(front4, bindData);
        drawTrianlges(bottom1, bindData);
        drawTrianlges(bottom2, bindData);
        drawTrianlges(left1, bindData);
        drawTrianlges(left2, bindData);




    }


    private final float w = 1f;
    //the same factor gose to close and far z that if the vertices are in -ve coordinate then need to shift the things in z too
    /*private final float closeZ = -1.5f, farZ = -0.5f;*/

    private void setFrontFaceAsCW1(){
        //this is front face so no shifting towards eye
        float[] front1Vert ={
                c(-75), c(-75), closeZ, w,
                c(-40), c(-20), closeZ, w,
                c(-40), c(-75), closeZ, w

        };
        float[] front2Vert ={
                c(-75), c(-75), closeZ, w,
                c(-75), c(-20), closeZ, w,
                c(-40), c(-20), closeZ, w

        };

        float color[]={
                1f, 0f, 0f, 0.2f,
                1f, 0f, 0f, 0.2f,
                1f, 0f, 0f, 0.2f,
        };

        front1 = FloatBuffer.allocate(front1Vert.length + color.length);
        front1.put(front1Vert);
        front1.put(color);
        front1.position(0);

        front2 = FloatBuffer.allocate(front2Vert.length + color.length);
        front2.put(front2Vert);
        front2.put(color);
        front2.position(0);
    }

    private void setTopFaceAsCW1(){
        float[] top1Vert ={
                c(-40), c(-20), closeZ, w,
                c(-75), c(-20), farZ, w,
                c(-40), c(-20), farZ, w

        };
        float[] top2Vert ={
                c(-40), c(-20), closeZ, w,
                c(-75), c(-20), closeZ, w,
                c(-75), c(-20), farZ, w

        };

        float color[]={
                0f, 1f, 0f, 0.2f,
                0f, 1f, 0f, 0.2f,
                0f, 1f, 0f, 0.2f,
        };

        top1 = FloatBuffer.allocate(top1Vert.length + color.length);
        top1.put(top1Vert);
        top1.put(color);
        top1.position(0);

        top2 = FloatBuffer.allocate(top2Vert.length + color.length);
        top2.put(top2Vert);
        top2.put(color);
        top2.position(0);
    }

    private void setRightFaceAsCW1(){
        float[] right1Vert ={
                c(-40), c(-75), closeZ, w,
                c(-40), c(-20), farZ, w,
                c(-40), c(-75), farZ, w

        };
        float[] right2Vert ={
                c(-40), c(-75), closeZ, w,
                c(-40), c(-20), closeZ, w,
                c(-40), c(-20), farZ, w

        };

        float color[]={
                0f, 0f, 1f, 0.2f,
                0f, 0f, 1f, 0.2f,
                0f, 0f, 1f, 0.2f,
        };

        right1 = FloatBuffer.allocate(right1Vert.length + color.length);
        right1.put(right1Vert);
        right1.put(color);
        right1.position(0);

        right2 = FloatBuffer.allocate(right2Vert.length + color.length);
        right2.put(right2Vert);
        right2.put(color);
        right2.position(0);
    }



    private final float closeZ = -1.1f, farZ = -2.8f;

    private void setFrontFaceAsCW(){
        //this is front face so no shifting towards eye
        float[] front1Vert ={
                c(40), c(40), closeZ, w,
                c(75), c(75), closeZ, w,
                c(75), c(40), closeZ, w

        };
        float[] front2Vert ={
                c(40), c(40), closeZ, w,
                c(40), c(75), closeZ, w,
                c(75), c(75), closeZ, w

        };

        float color[]={
                1f, 0f, 0f, 0.2f,
                1f, 0f, 0f, 0.2f,
                1f, 0f, 0f, 0.2f,
        };

        front3 = FloatBuffer.allocate(front1Vert.length + color.length);
        front3.put(front1Vert);
        front3.put(color);
        front3.position(0);

        front4 = FloatBuffer.allocate(front2Vert.length + color.length);
        front4.put(front2Vert);
        front4.put(color);
        front4.position(0);
    }


    private void setTopFaceAsCW(){
        //it'll be bootom face because we're in +ve coordinates, and because it's in +ve coordinate so it should be CCW to
        //be represented as CW
        float[] top1Vert ={
                c(75), c(40), farZ, w,
                c(40), c(40), closeZ, w,
                c(75), c(40), closeZ, w,


        };
        float[] top2Vert ={
                c(75), c(40), farZ, w,
                c(40), c(40), farZ, w,
                c(40), c(40), closeZ, w,


        };

        float color[]={
                0f, 1f, 0f, 0.2f,
                0f, 1f, 0f, 0.2f,
                0f, 1f, 0f, 0.2f,
        };

        bottom1 = FloatBuffer.allocate(top1Vert.length + color.length);
        bottom1.put(top1Vert);
        bottom1.put(color);
        bottom1.position(0);

        bottom2 = FloatBuffer.allocate(top2Vert.length + color.length);
        bottom2.put(top2Vert);
        bottom2.put(color);
        bottom2.position(0);
    }

    private void setRightFaceAsCW(){
        // it'll be left face as we're in +ve coordinates
        float[] right1Vert ={
                c(40), c(40), closeZ, w,
                c(40), c(75), farZ, w,
                c(40), c(75), closeZ, w

        };
        float[] right2Vert ={
                c(40), c(40), closeZ, w,
                c(40), c(40), farZ, w,
                c(40), c(75), farZ, w

        };

        float color[]={
                0f, 0f, 1f, 0.2f,
                0f, 0f, 1f, 0.2f,
                0f, 0f, 1f, 0.2f,
        };

        left1 = FloatBuffer.allocate(right1Vert.length + color.length);
        left1.put(right1Vert);
        left1.put(color);
        left1.position(0);

        left2 = FloatBuffer.allocate(right2Vert.length + color.length);
        left2.put(right2Vert);
        left2.put(color);
        left2.position(0);
    }

}
