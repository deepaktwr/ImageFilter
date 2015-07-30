package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES30.*;

/**
 * Created by deepak on 28/7/15.
 */
public class DrawImageTexture {

    public void drawTexture(){
        glUseProgram(GlDecorator.getProgramObject());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, GlDecorator.getTextureBufferObject().get(0));

        bindUniformTexture();

        IntBuffer vertexBufferObject = IntBuffer.allocate(1);
        glGenBuffers(1, vertexBufferObject);

        FloatBuffer vertexBuffer = getRectBufferCoord();

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject.get());

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 6 * 4 * 4);

        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glUseProgram(0);

    }
    private void bindUniformTexture(){
        int textureLocation = glGetUniformLocation(GlDecorator.getProgramObject(), "myTexture");
        glUniform1i(textureLocation, /*the level we have given at the time of binding texture in GlDecorator*/0);
    }
    private FloatBuffer getRectBufferCoord(){
        float[] rect={
                //rect coord
                /** have to maintain start and end sequence of counter clock wise order (it's decide the rotation of the texture)
                 * it's in CCW*/
                -1f, 1f, 1f,1f,
                -1f, -1f, 1f,1f,
                1f, 1f, 1f,1f,

                -1f, -1f, 1f,1f,
                1f, -1f, 1f,1f,
                1f, 1f, 1f,1f,

                //texture coords as the top left corner is origin (with twice of x and y -- mistaken)
                //it'll also decide the rotation of texture, it's in CCW
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,

        };
        FloatBuffer vertexBuffer = FloatBuffer.allocate(rect.length);
        vertexBuffer.put(rect);
        vertexBuffer.position(0);
        return vertexBuffer;
    }
}
