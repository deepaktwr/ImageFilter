package proj.android.gl_helper;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by deepak on 11/6/15.
 */
public class GlSurfaceView extends GLSurfaceView{

    private static GlSurfaceView glSurfaceView;
    private GlRenderer glRenderer;
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
        setEGLContextClientVersion(2);
        glRenderer = new GlRenderer(context);
        setRenderer(glRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
