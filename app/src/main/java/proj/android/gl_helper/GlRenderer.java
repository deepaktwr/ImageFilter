package proj.android.gl_helper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import proj.android.gl_shapes.DemoShape;
import proj.android.imagefilter.R;

/**
 * the class basically help to render the shapes on the opengl window.
 * code is influenced from <a href="http://alfonse.bitbucket.org/oldtut/">this tutorial</a>
 * and <a href ="https://www.khronos.org/opengles/sdk/docs/man3/">openGL docs</a>
 * @author deepak
 */
public class GlRenderer implements GLSurfaceView.Renderer{

    private Context context;
    private DemoShape demoShape;

    public GlRenderer(Context context){
        super();
        this.context=context;
    }
    /**
     * color will be used when window will be cleared; only need to set once
     * */
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //for depth
        //GlDecorator.setClearingDepthOfWindow();

        GlDecorator.setClearingColorOfWindow();

        //for texture
        GlDecorator.loadTextureFromResource(context, R.mipmap.ic_launcher);

        /*String version =  gl10.glGetString(GL10.GL_VERSION);
        Log.w("vvr", "Version: " + version);

        String versions =  gl10.glGetString(GLES30.GL_SHADING_LANGUAGE_VERSION);
        Log.w("vvr", "SHADING LANGUAGE: " + versions);

        Utils.logMessage(gl10.glGetString(GLES30.GL_VENDOR));
        Utils.logMessage(gl10.glGetString(GLES30.GL_VERSION));
        Utils.logMessage(gl10.glGetString(GLES30.GL_SHADING_LANGUAGE_VERSION));*/


        initializeShapes();
    }
    /**
     * set the view port of the opengl screen;
     * opengl has view port like bottomLeft X and Y cord and width and height to the right*/
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GlDecorator.initializeViewPort(0, 0, width, height);

        GlDecorator.setViewPort();

    }
    /**
     * clearing the window and applying the color on that window which is specified by glClearColor;
     * the default buffer which will be cleared will be COLOR_BUFFER;
     * @see GlDecorator
     * */
    @Override
    public void onDrawFrame(GL10 gl10) {
        //for depth
        //GlDecorator.intializeClearBuffers(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GlDecorator.clearWindowWithBuffers();

        //demoShape.draw2DShapes();
        //demoShape.drawColored2DShapes();
        //demoShape.drawMovingShapes();
        //demoShape.drawUsingUniform();
        //demoShape.drawPerspective();
        //demoShape.drawPerspectiveNew();
        //demoShape.drawSimpleIndexDraw();
        //demoShape.drawIndexedDrawing();
        //demoShape.drawOptimizedElementDraw();textureCoords


        demoShape.drawTexture();
    }

    private final void initializeShapes(){
        demoShape = DemoShape.getIntance(context);
    }
}
