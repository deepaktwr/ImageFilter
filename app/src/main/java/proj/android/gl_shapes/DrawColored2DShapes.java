package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES20.*;

/**
 * Created by deepak on 28/6/15.
 */
public class DrawColored2DShapes {

    private float c(float val){
        return val/100f;
    }
    public void drawColoredTriangle(){
        float[] vertexAndColor={
                c(50),c(-25),1f,1f,
                c(0),c(30),1f,1f,
                c(-50),c(-25),1f,1f,

                1f,0f,0f,1f,//r
                0f,1f,0f,1f,//g
                0f,0f,1f,1f//b
        };
        IntBuffer bindBuff = IntBuffer.allocate(1);
        FloatBuffer buffData = FloatBuffer.allocate(vertexAndColor.length);
        buffData.put(vertexAndColor);
        buffData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(1, bindBuff);
        glBindBuffer(GL_ARRAY_BUFFER, bindBuff.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        //means the first attribute
        glEnableVertexAttribArray(1);
        //second attribute
        glEnableVertexAttribArray(0);

        //send coordinate data at attribute 1 in vertex shader i.e position
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);
        //giving color data after 48 byte data of triangle coordinates
        //will be passed to attribute position 0 value i.e color in colored_vertex_frag shader which is varying so it'll
        //pass that color data to fragment shader to show colors.
        //colors are shaded because smooth keyword which is by default present in shader to make something special
        //with color data in order to color each fragment.
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 48);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}