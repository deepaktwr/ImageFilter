package proj.android.gl_shapes;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES30.*;

/**
 * Created by root on 9/7/15.
 */
public class DrawIndexedDrawing {

    private float c(float val){
        return val/100f;
    }


    public void drawWithElementArray(){
        glUseProgram(GlDecorator.getProgramObject());

        vertexArrayObject = IntBuffer.allocate(1);
        glGenVertexArrays(1, vertexArrayObject);
        glBindVertexArray(vertexArrayObject.get());

        FloatBuffer vertexBufferV = getVertexBufferObjectV();
        FloatBuffer vertexBufferH = getVertexBufferObjectH();
        indexBufferV = getIndexBufferObjectV();
        indexBufferH = getIndexBufferObjectH();

        vertexBufferObject = IntBuffer.allocate(2);
        indexBufferObject = IntBuffer.allocate(2);

        glGenBuffers(2, vertexBufferObject);
        glGenBuffers(2, indexBufferObject);

        bindUniformAndMatrixBuffer();


        //vertical object
        putElementsInBuffers(vertexBufferV, indexBufferV, 0);

        //glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get(0));

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 8 * 3 * 4);

        glDrawElements(GL_TRIANGLES, indexBufferV.capacity(), GL_UNSIGNED_SHORT, 0);


        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);


        //horizontal object
        putElementsInBuffers(vertexBufferH, indexBufferH, 1);

        //glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get(0));

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 8 * 3 * 4);

        glDrawElements(GL_TRIANGLES, indexBufferH.capacity(), GL_UNSIGNED_SHORT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        glUseProgram(0);
    }



    private IntBuffer vertexArrayObject, vertexBufferObject,indexBufferObject;
    private ShortBuffer indexBufferV, indexBufferH;
    private void putElementsInBuffers(FloatBuffer vertexBuffer, ShortBuffer indexBuffer, int position){

        //object Vertical
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get(position));
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GL_STATIC_DRAW);
        //(GL_ARRAY_BUFFER, 0);

        // objects index Vertical
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject.get(position));
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 2, indexBuffer, GL_STATIC_DRAW);
        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    }
    private void bindUniformAndMatrixBuffer(){

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);

        float[] offset ={
                0f,0f,0f
        };
        FloatBuffer offsetBuffer = FloatBuffer.allocate(offset.length);
        offsetBuffer.put(offset);
        offsetBuffer.position(0);

        int offsetLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");
        int matrixLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "cameraToClipMatrix");

        glUniform3fv(offsetLocation, 1, offsetBuffer);
        glUniformMatrix4fv(matrixLocation, 1, false, getMatrixBuffer());
    }
    private FloatBuffer getMatrixBuffer(){
        float N = 1f;
        float F = 3f;
        float scaleValue = 1f;
        float scaleInScreen = scaleValue /
                ((float)GlDecorator.getScreenWidthMeasure()/(float)GlDecorator.getScreenHeightMeasure());
        float firstZCoefficient = (N+F) / (N-F);
        float secondZCoefficient = (2f * N*F) / (N-F);

        float[] vertexMatrix = {
                scaleValue, 0f, 0f, 0f,
                0f, scaleValue, 0f, 0f,
                0f, 0f, firstZCoefficient, -1f,
                0f, 0f, secondZCoefficient, 0f
        };
        FloatBuffer matrixBuffer = FloatBuffer.allocate(vertexMatrix.length);
        matrixBuffer.put(vertexMatrix);
        matrixBuffer.position(0);
        return matrixBuffer;
    }
    private final float closeZ = -1.2f, farZ = -2.6f, middleZ = -2.0f;
    private FloatBuffer getVertexBufferObjectV(){
        float[] vertices = {
                c(-40), c(80), middleZ,
                c(-40), c(-80), middleZ,

                c(0), c(80), closeZ,
                c(0), c(-80), closeZ,

                c(40), c(80), middleZ,
                c(40), c(-80), middleZ,

                c(0), c(80), farZ,
                c(0), c(-80), farZ,


                1f,0f,0f,0.3f,
                1f,0f,0f,0.3f,

                0f,1f,0f,0.3f,
                0f,1f,0f,0.3f,

                0f,0f,1f,0.3f,
                0f,0f,1f,0.3f,

                1f,0f,1f,0.3f,
                1f,0f,1f,0.3f,
        };
        FloatBuffer vertexBuffer = FloatBuffer.allocate(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        return vertexBuffer;
    }
    private ShortBuffer getIndexBufferObjectV(){
        short[] indexes = {
                3,1,0,
                3,0,2,

                5,3,2,
                5,2,4,

                5,6,7,
                5,4,6,

                7,0,1,
                7,6,0
        };
        ShortBuffer indexBuffer = ShortBuffer.allocate(indexes.length);
        indexBuffer.put(indexes);
        indexBuffer.position(0);
        return indexBuffer;
    }

    private FloatBuffer getVertexBufferObjectH(){
        float[] vertices = {
                c(-80), c(40), middleZ,
                c(80), c(40), middleZ,

                c(-80), c(0), closeZ,
                c(80), c(0), closeZ,

                c(-80), c(-40), middleZ,
                c(80), c(-40), middleZ,

                c(-80), c(0), farZ,
                c(80), c(0), farZ,


                1f,0f,0f,0.3f,
                1f,0f,0f,0.3f,

                1f,0f,1f,0.3f,
                1f,0f,1f,0.3f,

                0f,0f,1f,0.3f,
                0f,0f,1f,0.3f,

                0f,1f,0f,0.3f,
                0f,1f,0f,0.3f,
        };
        FloatBuffer vertexBuffer = FloatBuffer.allocate(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        return vertexBuffer;
    }
    private ShortBuffer getIndexBufferObjectH(){
        short[] indexes = {
                0,1,3,
                0,3,2,

                2,3,5,
                2,5,4,

                1,0,6,
                1,6,7,

                7,6,4,
                7,4,5
        };
        ShortBuffer indexBuffer = ShortBuffer.allocate(indexes.length);
        indexBuffer.put(indexes);
        indexBuffer.position(0);
        return indexBuffer;
    }
}
