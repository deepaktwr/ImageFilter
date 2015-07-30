package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES30.*;

/**
 * Created by root on 26/7/15.
 */
public class DrawOptimizedElement {

    private float c(float val){
        return val/100f;
    }

    private FloatBuffer vertexBuffer, matrixBuffer;
    private ShortBuffer indexBuffer;

    private final float closeZ = -1.2f, farZ = -2.6f, middleZ = -2.0f;

    private void setVertexBuffer(){
        float[] vetices = {
                //vertical object
                c(-40), c(80), middleZ,
                c(-40), c(-80), middleZ,

                c(0), c(80), closeZ,
                c(0), c(-80), closeZ,

                c(40), c(80), middleZ,
                c(40), c(-80), middleZ,

                c(0), c(80), farZ,
                c(0), c(-80), farZ,
                //vertical color
                1f,0f,0f,0.3f,
                1f,0f,0f,0.3f,

                0f,1f,0f,0.3f,
                0f,1f,0f,0.3f,

                0f,0f,1f,0.3f,
                0f,0f,1f,0.3f,

                1f,0f,1f,0.3f,
                1f,0f,1f,0.3f,

                //horizontal object

                c(-80), c(40), middleZ,
                c(80), c(40), middleZ,

                c(-80), c(0), closeZ,
                c(80), c(0), closeZ,

                c(-80), c(-40), middleZ,
                c(80), c(-40), middleZ,

                c(-80), c(0), farZ,
                c(80), c(0), farZ,

                //horizontal color
                1f,0f,0f,0.3f,
                1f,0f,0f,0.3f,

                1f,0f,1f,0.3f,
                1f,0f,1f,0.3f,

                0f,0f,1f,0.3f,
                0f,0f,1f,0.3f,

                0f,1f,0f,0.3f,
                0f,1f,0f,0.3f,
        };

        vertexBuffer = FloatBuffer.allocate(vetices.length);
        vertexBuffer.put(vetices);
        vertexBuffer.position(0);
    }

    private void setIndexBuffer(){
        //can take same vertices for both
        short[] indices={
                //verticle indices
                3,1,0,
                3,0,2,

                5,3,2,
                5,2,4,

                5,6,7,
                5,4,6,

                7,0,1,
                7,6,0,
                //horizontal indices
                0,1,3,
                0,3,2,

                2,3,5,
                2,5,4,

                1,0,6,
                1,6,7,

                7,6,4,
                7,4,5
        };

        indexBuffer = ShortBuffer.allocate(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    private void setMatrixBuffer(){
        float N = 1.0f;
        float F = 3.0f;
        float firstZCo = (N+F) / (N-F);
        float secondZCo = (2f * N*F) / (N-F);
        float scaleVal = 1.0f;

        float[] matrix={
                scaleVal,0f,0f,0f,
                0f,scaleVal,0f,0f,
                0f,0f,firstZCo,-1f,
                0f,0f,secondZCo,0f
        };
        matrixBuffer = FloatBuffer.allocate(matrix.length);
        matrixBuffer.put(matrix);
        matrixBuffer.position(0);
    }

    private FloatBuffer getOffsetBuffer(float zCoord){
        float[] offset ={
                0f,0f,zCoord
        };
        FloatBuffer offsetBuffer = FloatBuffer.allocate(offset.length);
        offsetBuffer.put(offset);
        offsetBuffer.position(0);
        return offsetBuffer;
    }

    private void bindUniformOffset(FloatBuffer offsetBuffer){
        int offsetLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "offsetValue");

        glUniform3fv(offsetLocation, 1, offsetBuffer);
    }
    private void bindUniformMatrix(){
        int matrixLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "cameraToClipMatrix");
        glUniformMatrix4fv(matrixLocation, 1, false, matrixBuffer);
    }

    private void enableFaceCulling(){
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glFrontFace(GL_CW);
    }

    private void enableDepthTest(){
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        glDepthFunc(GL_LEQUAL);
        glDepthRangef(0f, 1f);
    }

    private void enableDepthClamp(){
    }

    public void drawOptimizedElements(){
        setVertexBuffer();
        setIndexBuffer();
        setMatrixBuffer();

        glUseProgram(GlDecorator.getProgramObject());

        IntBuffer vao = IntBuffer.allocate(1);
        IntBuffer vbo = IntBuffer.allocate(1);
        IntBuffer ibo = IntBuffer.allocate(1);

        glGenVertexArrays(1, vao);
        glGenBuffers(1, vbo);
        glGenBuffers(1, ibo);

        glBindVertexArray(vao.get());
        glBindBuffer(GL_ARRAY_BUFFER, vbo.get());
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo.get());

        enableFaceCulling();
        enableDepthTest();
        bindUniformMatrix();

        setGlobalCalls();

        //vertical object
        drawVerticalObject();

        //horizontal object
        drawHorizontalObject();
        //enableDepthTest();
        cleanUp();

    }

    private void setGlobalCalls(){
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 2, indexBuffer, GL_STATIC_DRAW);
    }

    private void drawVerticalObject(){
        //bindUniformOffset(getOffsetBuffer(0f));
        //bindUniformOffset(getOffsetBuffer(-.75f));
        bindUniformOffset(getOffsetBuffer(-.2f));
        //bindUniformOffset(getOffsetBuffer(0.5f));

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 8 * 3 * 4);

        glDrawElements(GL_TRIANGLES, (8 * 3), GL_UNSIGNED_SHORT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private void drawHorizontalObject(){
        //bindUniformOffset(getOffsetBuffer(-.75f));
        bindUniformOffset(getOffsetBuffer(-.2f));
        //bindUniformOffset(getOffsetBuffer(-1f));

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, (8 * 3 * 4) + (8 * 4 * 4));
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, (8 * 3 * 4) + (8 * 4 * 4) + (8 * 3 * 4));

        glDrawElements(GL_TRIANGLES, (8 * 3), GL_UNSIGNED_SHORT, 8 * 3 * 2);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

    }

    private void cleanUp(){
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glUseProgram(0);
    }
}
