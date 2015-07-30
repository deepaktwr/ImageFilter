package proj.android.gl_helper;

import android.opengl.GLES20;

/**
 * Created by deepak on 17/6/15.
 * @link http://duriansoftware.com/joe/An-intro-to-modern-OpenGL.-Chapter-1:-The-Graphics-Pipeline.html
 */
public enum ShaderType {
    /**
     * it will take the input came from vertex shader to apply texture or color patterns on them;
     * the fragment processor which process this shader does not modify fragment position
     * */
    FRAGMENT_SHADER(GLES20.GL_FRAGMENT_SHADER),
    /**
     * the vertex shader deals with vertices in the GLU pipeline
     * it will apply to all the vertices we define;
     * vertex processor which process this shader does not modify the graphics operation
     * */
    VERTEX_SHADER(GLES20.GL_VERTEX_SHADER);
    //GEOMETRY_SHADER(GLES30.GL_GEOMETRY_SHADER);
    private int shaderType;
    ShaderType(int shaderType){
        this.shaderType=shaderType;
    }
    public int getType(){
        return shaderType;
    }
}
