package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES30.*;

/**
 * Created by deepak on 26/7/15.
 */
public class DrawSimpleIndexElement {

    private final float c(float value){
        return (value/100f);
    }
    public void drawElements(){
        setVerticesAndIndex();
        glUseProgram(GlDecorator.getProgramObject());
        IntBuffer vertexArrayObject = IntBuffer.allocate(1);
        IntBuffer vertexBufferObject = IntBuffer.allocate(1);
        IntBuffer indexBuffer = IntBuffer.allocate(1);

        glGenVertexArrays(1, vertexArrayObject);
        glGenBuffers(1, vertexBufferObject);
        glGenBuffers(1, indexBuffer);

        glBindVertexArray(vertexArrayObject.get());
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get());

        glBufferData(GL_ARRAY_BUFFER, vertices.capacity() * 4, vertices, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.get());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.capacity() * 2, indices, GL_STATIC_DRAW);

        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_SHORT, 0);
        //glDrawArrays(GL_TRIANGLES, 0, 3);

    }

    public void drawElementsWithColor(){
        setVerticesWithColorAndIndex();
        glUseProgram(GlDecorator.getProgramObject());
        IntBuffer vertexArrayObject = IntBuffer.allocate(1);
        IntBuffer vertexBufferObject = IntBuffer.allocate(1);
        IntBuffer indexBuffer = IntBuffer.allocate(1);

        glGenVertexArrays(1, vertexArrayObject);
        glGenBuffers(1, vertexBufferObject);
        glGenBuffers(1, indexBuffer);

        glBindVertexArray(vertexArrayObject.get());
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get());

        glBufferData(GL_ARRAY_BUFFER, vertices.capacity() * 4, vertices, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 64);


        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer.get());
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.capacity() * 2, indices, GL_STATIC_DRAW);

        glDrawElements(GL_TRIANGLES, indices.capacity(), GL_UNSIGNED_SHORT, 0);
        //glDrawArrays(GL_TRIANGLES, 0, 3);

    }

    private FloatBuffer vertices;
    private ShortBuffer indices;
    private void setVerticesAndIndex(){
        float[] vetex = {
                c(40),c(75),1f,1f,
                c(75),c(75),1f,1f,
                c(40),c(40),1f,1f,
                c(75),c(40),1f,1f,
        };
        short[] index ={
                2,0,3,
                2,1,3
        };

        vertices = FloatBuffer.allocate(vetex.length);
        indices = ShortBuffer.allocate(index.length);

        vertices.put(vetex);
        vertices.position(0);

        indices.put(index);
        indices.position(0);
    }

    private void setVerticesWithColorAndIndex(){
        float[] vetex = {
                c(40),c(75),1f,1f,
                c(75),c(75),1f,1f,
                c(40),c(40),1f,1f,
                c(75),c(40),1f,1f,

                1f,0f,0f,.3f,
                0f,1f,0f,.3f,
                0f,0f,1f,.3f,

                1f,1f,0f,.3f,
/*                0f,0f,1f,.3f,
                1f,0f,0f,.3f,*/
        };
        short[] index ={
                2,0,3,
                2,1,3
        };

        vertices = FloatBuffer.allocate(vetex.length);
        indices = ShortBuffer.allocate(index.length);

        vertices.put(vetex);
        vertices.position(0);

        indices.put(index);
        indices.position(0);
    }
}
