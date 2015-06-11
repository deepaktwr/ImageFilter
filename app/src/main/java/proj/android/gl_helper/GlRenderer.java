package proj.android.gl_helper;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import proj.android.gl_shapes.ShapeSquare;
import proj.android.gl_shapes.ShapeTriangle;

/**
 * Created by deepak on 11/6/15.
 */
public class GlRenderer implements GLSurfaceView.Renderer{

    private ShapeTriangle shapeTriangle;
    //private ShapeSquare shapeSquare;
    private Context context;

    public GlRenderer(Context context){
        super();
        this.context=context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.09f, 0.08f, 0.01f, 1.0f);

        shapeTriangle=ShapeTriangle.getInstance(context);
        //shapeSquare=ShapeSquare.getInstance();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        shapeTriangle.drawTriangleShape();
    }

    public static int loadShader(int shaderType, String shaderCode){
        int shader = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
