package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;

import static android.opengl.GLES20.*;

/**
 * Created by deepak on 1/7/15.
 */
public class DrawUsingUniform {
    public void DrawShape(){
        FloatBuffer buffData = getTriangleCoord();
        IntBuffer binObj = IntBuffer.allocate(1);
        int uniformObjectLoc = glGetUniformLocation(GlDecorator.getProgramObject(), "data");

        glUseProgram(GlDecorator.getProgramObject());
        glUniform2f(uniformObjectLoc, c(50), c(50));
        glGenBuffers(1, binObj);
        glBindBuffer(GL_ARRAY_BUFFER, binObj.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    private float c(float val){
        return val/100f;
    }
    private FloatBuffer getTriangleCoord(){
        float[] trinCoord={
                c(20),c(30),1f,1f,
                c(-20),c(-50),1f,1f,
                c(50),c(-60),1f,1f,
        };
        FloatBuffer buffData=FloatBuffer.allocate(trinCoord.length);
        buffData.put(trinCoord);
        buffData.position(0);
        return buffData;
    }
}
