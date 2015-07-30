package proj.android.gl_shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES20.*;

/**
 * Created by deepak on 5/7/15.
 */
public class DrawPerspective {
    private float c(float val){
        return val/100f;
    }

    private float w =1f;

    public void drawPerspectiveFrustum(){
        setFrontFacingT();
        setBackFacingT();
        setLeftFacingT();
        setRightFacingT();
        setTopFacingT();
        setBottomFacingT();

        IntBuffer bindBuff = IntBuffer.allocate(12);
        glGenBuffers(12, bindBuff);
        glUseProgram(GlDecorator.getProgramObject());
        setUniformObjects();

        drawTriangle(frontFacing1stT, frontFacingColor, bindBuff, 0);
        frontFacingColor.position(0);
        drawTriangle(frontFacing2ndT, frontFacingColor, bindBuff, 1);

        drawTriangle(backFacing1stT, backFacingColor, bindBuff, 2);
        backFacingColor.position(0);
        drawTriangle(backFacing2ndT, backFacingColor, bindBuff, 3);

        drawTriangle(leftFacing1stT, leftFacingColor, bindBuff, 4);
        leftFacingColor.position(0);
        drawTriangle(leftFacing2ndT, leftFacingColor, bindBuff, 5);

        drawTriangle(rightFacing1stT, rightFacingColor, bindBuff, 6);
        rightFacingColor.position(0);
        drawTriangle(rightFacing2ndT, rightFacingColor, bindBuff, 7);

        drawTriangle(topFacing1stT, topFacingColor, bindBuff, 8);
        topFacingColor.position(0);
        drawTriangle(topFacing2ndT, topFacingColor, bindBuff, 9);

        drawTriangle(bottomFacing1stT, bottomFacingColor, bindBuff, 10);
        bottomFacingColor.position(0);
        drawTriangle(bottomFacing2ndT, bottomFacingColor, bindBuff, 11);

    }
    public void drawPerspectiveFrustumUsingMatrices(){
        setFrontFacingT();
        setBackFacingT();
        setLeftFacingT();
        setRightFacingT();
        setTopFacingT();
        setBottomFacingT();

        IntBuffer bindBuff = IntBuffer.allocate(12);
        glGenBuffers(12, bindBuff);
        glUseProgram(GlDecorator.getProgramObject());
        setUniformMatrixObjects();

        drawTriangle(frontFacing1stT, frontFacingColor, bindBuff, 0);
        frontFacingColor.position(0);
        drawTriangle(frontFacing2ndT, frontFacingColor, bindBuff, 1);

        drawTriangle(backFacing1stT, backFacingColor, bindBuff, 2);
        backFacingColor.position(0);
        drawTriangle(backFacing2ndT, backFacingColor, bindBuff, 3);

        drawTriangle(leftFacing1stT, leftFacingColor, bindBuff, 4);
        leftFacingColor.position(0);
        drawTriangle(leftFacing2ndT, leftFacingColor, bindBuff, 5);

        drawTriangle(rightFacing1stT, rightFacingColor, bindBuff, 6);
        rightFacingColor.position(0);
        drawTriangle(rightFacing2ndT, rightFacingColor, bindBuff, 7);

        drawTriangle(topFacing1stT, topFacingColor, bindBuff, 8);
        topFacingColor.position(0);
        drawTriangle(topFacing2ndT, topFacingColor, bindBuff, 9);

        drawTriangle(bottomFacing1stT, bottomFacingColor, bindBuff, 10);
        bottomFacingColor.position(0);
        drawTriangle(bottomFacing2ndT, bottomFacingColor, bindBuff, 11);
    }
    private void setUniformMatrixObjects(){
        /*glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);*/
        int offset = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");
        int matrixLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "cameraToClipMatrix");
        setUniformMatrixForCameraSpace();
        glUniformMatrix4fv(matrixLocation, 1, /**row major or coloumn major order true for row major*/false, matrixBuffer);
        //glUniform2f(offset,0.5f,0.5f);
        FloatBuffer floatBuffer = FloatBuffer.allocate(2);
        floatBuffer.put(new float[]{0.0f, 0.0f});
        floatBuffer.position(0);
        glUniform2fv(offset, 1, floatBuffer);
    }
    private FloatBuffer matrixBuffer;
    private void setUniformMatrixForCameraSpace(){
        float N=1f;
        float F = 3f;
        float firstZCoefficient = (N+F)/(N-F);
        float secondZCoefficient = 2*N*F/(N-F);
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
    private void setUniformObjects(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
        int offset = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");
        int zNear = glGetUniformLocation(GlDecorator.getProgramObject(), "zNear");
        int zfar = glGetUniformLocation(GlDecorator.getProgramObject(), "zFar");
        int xyScaleValueOrFrustumScaleValue = glGetUniformLocation(GlDecorator.getProgramObject(),
                "xyScaleValueOrFrustumScaleValue");

        glUniform1f(zNear, 1f);//should not be zero, can be near to zero
        glUniform1f(zfar, 3f);
        glUniform1f(xyScaleValueOrFrustumScaleValue, 1f);
    }
    private void drawTriangle(FloatBuffer vertices, FloatBuffer color, IntBuffer bindBuffer, int bindingPoint){
        glBindBuffer(GL_ARRAY_BUFFER, bindBuffer.get(bindingPoint));

        /*glBufferData(GL_ARRAY_BUFFER, color.capacity() * 4, color, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);*/
        vertices.put(color);
        vertices.position(0);

        glBufferData(GL_ARRAY_BUFFER, vertices.capacity() * 4, vertices, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 48);

        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        //glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }


    /**need to make 12 rectangles
     * 2 in pair with same color
     * 12 times draw triangle with color then with strip try
     * */
    private FloatBuffer frontFacing1stT;
    private FloatBuffer frontFacing2ndT;
    private FloatBuffer frontFacingColor;
    private FloatBuffer backFacing1stT;
    private FloatBuffer backFacing2ndT;
    private FloatBuffer backFacingColor;

    private FloatBuffer leftFacing1stT;
    private FloatBuffer leftFacing2ndT;
    private FloatBuffer leftFacingColor;
    private FloatBuffer rightFacing1stT;
    private FloatBuffer rightFacing2ndT;
    private FloatBuffer rightFacingColor;

    private FloatBuffer topFacing1stT;
    private FloatBuffer topFacing2ndT;
    private FloatBuffer topFacingColor;
    private FloatBuffer bottomFacing1stT;
    private FloatBuffer bottomFacing2ndT;
    private FloatBuffer bottomFacingColor;
/**the z will be vary as [-F -N]
 * we are drawing opposite i.e the front face wil go to back face in clip space,
 * so that we'll draw this as front face of clip space so back face of camera space in which we are passing cords.
 * so that this is a back face in camera space.(CCW)*/
    private void setFrontFacingT(){
        //it is an back face in clip because x value will be divided by larger z value(2.9) so it'll be shifted
        //towards center or eye much more than other so it'll look small.
        //the concept is that, if the object looks small then it's far.
        float[] pointsCoord1st = {
            c(75),c(70),-2.90f,w,
            c(40),c(70),-2.90f,w,
            c(75),c(30),-2.90f,w,
        };
        float[] pointsCoord2nd = {
                c(75),c(30),-2.90f,w,
                c(40),c(70),-2.90f,w,
                c(40),c(30),-2.90f,w
        };

        float[] color={
                1f,0f,0f,0f,
                1f,0f,0f,0f,
                1f,0f,0f,0f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4+ color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        frontFacing1stT = byteBuffer.asFloatBuffer();
        frontFacing1stT.put(pointsCoord1st);
        //frontFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4+ color.length*4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        frontFacing2ndT = byteBuffer1.asFloatBuffer();
        frontFacing2ndT.put(pointsCoord2nd);
        //frontFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        frontFacingColor = byteBuffer2.asFloatBuffer();
        frontFacingColor.put(color);
        frontFacingColor.position(0);
    }
/**this will be front face in camera space(CW)*/
    private void setBackFacingT(){
        float[] pointsCoord1st = {
                //z divide will be small so look large so near, so that it's a front face.
                c(75),c(70),-1.1f,w,

                c(75),c(30),-1.1f,w,
                c(40),c(70),-1.1f,w,
        };
        float[] pointsCoord2nd = {
                c(75),c(30),-1.1f,w,

                c(40),c(30),-1.1f,w,
                c(40),c(70),-1.1f,w,
        };

        float[] color={
                0.5f,0f,0f,1f,
                0.5f,0f,0f,1f,
                0.5f,0f,0f,1f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4 + color.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        backFacing1stT = byteBuffer.asFloatBuffer();
        backFacing1stT.put(pointsCoord1st);
        //backFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4 + color.length * 4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        backFacing2ndT = byteBuffer1.asFloatBuffer();
        backFacing2ndT.put(pointsCoord2nd);
        //backFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        backFacingColor = byteBuffer2.asFloatBuffer();
        backFacingColor.put(color);
        backFacingColor.position(0);
    }
/**this will be right face in camera space (CCW)*/
    private void setLeftFacingT(){

        //the x value is 75 so it'll be a far value and will shift to user where z is 2.9, so it'll be a right face because
        //the left face has x 40 below
        float[] pointsCoord1st = {
                c(75),c(70),-1.1f,w,
                c(75),c(70),-2.9f,w,
                c(75),c(30),-1.1f,w,
        };
        float[] pointsCoord2nd = {
                c(75),c(30),-1.1f,w,
                c(75),c(70),-2.9f,w,
                c(75),c(30),-2.9f,w
        };

        float[] color={
                0f,1f,0f,0f,
                0f,1f,0f,0f,
                0f,1f,0f,0f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4+ color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        leftFacing1stT = byteBuffer.asFloatBuffer();
        leftFacing1stT.put(pointsCoord1st);
        //leftFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4+ color.length*4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        leftFacing2ndT = byteBuffer1.asFloatBuffer();
        leftFacing2ndT.put(pointsCoord2nd);
        //leftFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        leftFacingColor = byteBuffer2.asFloatBuffer();
        leftFacingColor.put(color);
        leftFacingColor.position(0);
    }
/**this will be left face in camera space (CW)*/
    private void setRightFacingT(){
        //CW
        float[] pointsCoord1st = {
                c(40),c(70),-1.1f,w,
                c(40),c(30),-1.1f,w,
                c(40),c(70),-2.9f,w,
        };
        float[] pointsCoord2nd = {
                c(40),c(30),-1.1f,w,
                c(40),c(30),-2.9f,w,
                c(40),c(70),-2.9f,w
        };

        float[] color={
                0f,0.5f,0f,0f,
                0f,0.5f,0f,0f,
                0f,0.5f,0f,0f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4+ color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        rightFacing1stT = byteBuffer.asFloatBuffer();
        rightFacing1stT.put(pointsCoord1st);
       // rightFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4+ color.length*4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        rightFacing2ndT = byteBuffer1.asFloatBuffer();
        rightFacing2ndT.put(pointsCoord2nd);
        //rightFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        rightFacingColor = byteBuffer2.asFloatBuffer();
        rightFacingColor.put(color);
        rightFacingColor.position(0);
    }
/**this will be our top face with revert clock direction only in camera space (CCW).
 * it's rotated because we are looking at it from bottom which convert it from CCW-CW.
 * so we need to make it CW and bottom to CCW as per our requirment*/
    private void setTopFacingT(){
        //it depends upon from where we are looking at it.
        //because y is +ve so we must are looking at it from below to top as we are at 0,0,0.
        //and because we'll only able to see the bottom face so this will be CCW(if we do look at it from bottom)
        float[] pointsCoord1st = {
                c(75),c(70),-2.9f,w,

                c(75),c(70),-1.1f,w,
                c(40),c(70),-2.9f,w,

        };
        float[] pointsCoord2nd = {
                c(75),c(70),-1.1f,w,

                c(40),c(70),-1.1f,w,
                c(40),c(70),-2.9f,w,

        };

        float[] color={
                0f,0f,1f,0f,
                0f,0f,1f,0f,
                0f,0f,1f,0f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4+ color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        topFacing1stT = byteBuffer.asFloatBuffer();
        topFacing1stT.put(pointsCoord1st);
        //topFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4+ color.length*4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        topFacing2ndT = byteBuffer1.asFloatBuffer();
        topFacing2ndT.put(pointsCoord2nd);
        //topFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        topFacingColor = byteBuffer2.asFloatBuffer();
        topFacingColor.put(color);
        topFacingColor.position(0);
    }
/**this will be our bottom face with revert clock direction only in camera space (CW)
 * but we are lokking at it from bottom so it will be converted to CW-CCW.
 * so we need to change it to CCW so it can be converted to final CW as per our requirment*/
    private void setBottomFacingT(){
        //CW
        float[] pointsCoord1st = {
                c(75),c(30),-2.9f,w,

                c(40),c(30),-2.9f,w,
                c(75),c(30),-1.1f,w,

        };
        float[] pointsCoord2nd = {
                c(75),c(30),-1.1f,w,

                c(40),c(30),-2.9f,w,
                c(40),c(30),-1.1f,w,
        };

        float[] color={
                0f,0f,0.5f,0f,
                0f,0f,0.5f,0f,
                0f,0f,0.5f,0f,
        };
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsCoord1st.length * 4+ color.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());

        bottomFacing1stT = byteBuffer.asFloatBuffer();
        bottomFacing1stT.put(pointsCoord1st);
        //bottomFacing1stT.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(pointsCoord2nd.length * 4+ color.length*4);
        byteBuffer1.order(ByteOrder.nativeOrder());

        bottomFacing2ndT = byteBuffer1.asFloatBuffer();
        bottomFacing2ndT.put(pointsCoord2nd);
        //bottomFacing2ndT.position(0);

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(color.length * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());

        bottomFacingColor = byteBuffer2.asFloatBuffer();
        bottomFacingColor.put(color);
        bottomFacingColor.position(0);
    }


}


