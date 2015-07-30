package proj.android.gl_helper;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by deepak on 11/6/15.
 */
public class GlSurfaceView extends GLSurfaceView{

    private static GlSurfaceView glSurfaceView;
    /*private GlRenderer glRendererForDemos;*/
    private GlRendererForFilter glRendererForFilter;
    private Context context;

    public static final GlSurfaceView getInstance(Context context){
        if(glSurfaceView == null) {
            glSurfaceView = new GlSurfaceView(context);
            glSurfaceView.init();
        }
        return glSurfaceView;
    }

    private GlSurfaceView(Context context) {
        super(context);
        this.context=context;
    }
    private final void init(){
        // setting the GLSL version to 3 along with in the manifest
        setEGLContextClientVersion(3);
        //setGlRendererForDemos();
        setGlRendererForFilter();

        // RENDERMODE_WHEN_DIRTY clarifies that the rendering only perform when their is already some data on the openGL window
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //continuous for calling onDrawFrame again and again for redrawing
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    private void setGlRendererForFilter(){
        glRendererForFilter = new GlRendererForFilter(context);
        setRenderer(glRendererForFilter);
    }
    private void setGlRendererForDemos(){
        /*glRendererForDemos = new GlRenderer(context);
        setRenderer(glRendererForDemos);*/
    }

    public void drawNewTexture(int vertexShaderId, int fragmentShaderId){
        glRendererForFilter.drawTexture(vertexShaderId, fragmentShaderId);
    }
}
