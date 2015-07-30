package proj.android.gl_shapes;

import android.content.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import proj.android.gl_helper.GlDecorator;
import proj.android.gl_helper.RawFileReader;
import proj.android.gl_helper.RawReadStatusListener;
import proj.android.gl_helper.ShaderType;
import proj.android.helper.Utils;
import proj.android.imagefilter.R;

/**
 * Created by deepak on 16/6/15.
 */
public class DemoShape implements RawReadStatusListener{
    private Context context;
    private RawFileReader rawFileReader;
    private Draw2DShapes draw2DShapes;
    private DrawColored2DShapes drawColored2DShapes;
    private DrawMoving2DShapes drawMoving2DShapes;
    private DrawUsingUniform drawUsingUniform;
    private DrawPerspective drawPerspective;
    private DrawIndexedDrawing drawIndexedDrawing;
    private DrawNewPerspective drawNewPerspective;
    private DrawSimpleIndexElement drawSimpleIndexElement;
    private DrawOptimizedElement drawOptimizedElement;
    private DrawImageTexture drawImageTexture;
    private DemoShape(Context context){
        this.context=context;
    }

    private static DemoShape demoShape= null;
    public static DemoShape getIntance(Context context){
        if(demoShape==null)
            demoShape = new DemoShape(context);
        demoShape.init(context);
        return demoShape;
    }

    private final void init(Context context){
        this.context = context;
        rawFileReader = new RawFileReader(context, this);
        draw2DShapes = new Draw2DShapes();
        drawColored2DShapes=new DrawColored2DShapes();
        drawMoving2DShapes = new DrawMoving2DShapes();
        drawUsingUniform=new DrawUsingUniform();
        drawPerspective=new DrawPerspective();
        drawNewPerspective = new DrawNewPerspective();
        drawSimpleIndexElement= new DrawSimpleIndexElement();
        drawIndexedDrawing= new DrawIndexedDrawing();
        drawOptimizedElement = new DrawOptimizedElement();
        drawImageTexture = new DrawImageTexture();

    }

    private List<Integer> shaders;
    /**
     * load the shaders and program in order to process it on the opengl window
     * */
    private final void loadShaders(int vertextShaderId, int fragmentShaderId, Boolean ... modification){
        //should only be called again when required, will be called all the time when rendering mode is Continuous
        //so that need to use it carefully, i.e only need to call it when reloading of shader and program is neccessury
        if(!Utils.isProgramReady() ||(modification.length>1 && modification[0]==true)) {
            Utils.setIsProgramReady(false);
            Utils.logMessage("loading shader");
            if (shaders != null) {
                shaders.clear();
                shaders = null;
                System.gc();
            }
            shaders = new ArrayList<>();
            try {
                rawFileReader.getShaderFromRaw(vertextShaderId, ShaderType.VERTEX_SHADER, modification);
                rawFileReader.getShaderFromRaw(fragmentShaderId, ShaderType.FRAGMENT_SHADER, modification);
            } catch (IOException e) {
                Utils.logError("in reading from raw file:" + e);
            }
        }
    }

    public void draw2DShapes(){
        loadShaders(R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        if(Utils.isProgramReady()) {
            //draw2DShapes.drawSimpleLine();
            //draw2DShapes.drawTwoLines();
            //draw2DShapes.drawStripLines();
            //draw2DShapes.drawSimpleTriangle();
            //draw2DShapes.drawTwoTriangles();
            //draw2DShapes.drawStripTriangles();
            //draw2DShapes.drawSimpleRectengle();
        }
        else
            Utils.logError("the program is not ready yet, please wait");
    }
    public void drawColored2DShapes(){
        //loadShaders(R.raw.simple_vertex_shader, R.raw.dynamic_fragment_shader, false, false);
        loadShaders(R.raw.colored_vertex_frag, R.raw.colored_fragment_frag, false, false);
        if(Utils.isProgramReady()) {
            //draw2DShapes.drawSimpleTriangle();
            //draw2DShapes.drawSimpleRectengle();
            drawColored2DShapes.drawColoredTriangle();
        }
        else
            Utils.logError("the program is not ready yet, please wait");
    }
    public void drawMovingShapes(){
        //loadShaders(R.raw.vertext_shader_color, R.raw.fragment_shader_color, false, false);
        //loadShaders(R.raw.vertex_shader_moving_object, R.raw.fragment_shader_moving_object, false, false);
        //loadShaders(R.raw.vertex_uniform_move, R.raw.fragment_uniform_move, false, false);
        if(Utils.isProgramReady()) {
            //drawMoving2DShapes.drawMovingTriangle();
            //drawMoving2DShapes.drawMovingPoint();
            //drawMoving2DShapes.drawMultipleObjets()is it good to create opengl using ndk in andoir;
            //drawMoving2DShapes.movePointUsingUniform();
        }
        else
            Utils.logError("the program is not rea1.0dy yet, please wait");
    }
    public void drawUsingUniform(){
        loadShaders(R.raw.vertex_uniform_shader, R.raw.fragement_uniform_shader, false, false);
        if(Utils.isProgramReady()) {
            drawUsingUniform.DrawShape();
        }
        else
            Utils.logError("the program is not ready yet, please wait");
    }
    public void drawPerspective(){
        loadShaders(R.raw.perspective_vertex_shader, R.raw.perspective_fragment_shader);
        drawPerspective.drawPerspectiveFrustum();
        //loadShaders(R.raw.vertex_perspective_matrix, R.raw.perspective_fragment_shader);
        //drawPerspective.drawPerspectiveFrustumUsingMatrices();
    }
    public void drawPerspectiveNew(){

        //loadShaders(R.raw.perspective_vertex_shader_new, R.raw.perspective_fragment_shader);
        //drawNewPerspective.drawPerspectiveWithoutMatrices();
        loadShaders(R.raw.vertex_perspective_matrix_new, R.raw.perspective_fragment_shader);
        drawNewPerspective.drawPerspectiveWithoutMatrices();
    }
    public void drawSimpleIndexDraw(){
        //loadShaders(R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        //drawSimpleIndexElement.drawElements();
        loadShaders(R.raw.colored_vertex_frag, R.raw.colored_fragment_frag);
        drawSimpleIndexElement.drawElementsWithColor();
    }
    public void drawIndexedDrawing(){
        loadShaders(R.raw.vertex_perspective_matrix_indexed, R.raw.perspective_fragment_shader);
        drawIndexedDrawing.drawWithElementArray();
    }
    public void drawOptimizedElementDraw(){
        loadShaders(R.raw.vertex_perspective_matrix_indexed, R.raw.perspective_fragment_shader);
        drawOptimizedElement.drawOptimizedElements();
    }
    public void drawTexture(){
        loadShaders(R.raw.simple_texture_vertex_shader, R.raw.simple_texture_fragment_shader);
        drawImageTexture.drawTexture();
    }
    @Override
    public void onReadingComplete(String shaderSource, ShaderType shaderType, Boolean ... modification) {
        Utils.logMessage(Utils.formatMessage("shader %s loaded from raw file", shaderType.toString()));
        //Utils.logMessage(shaderSource);
        int shader = GlDecorator.loadShader(shaderType, shaderSource);
        shaders.add(shader);
        Integer[] intParam = new Integer[shaders.size()];
        GlDecorator.loadProgram(modification.length > 0 ? modification[0] : false, modification.length > 1 ? modification[1] : false,
                shaders.toArray(intParam));
    }
    public List<Integer> getCompiledShader(){
        return shaders;
    }


    @Override
    public void onReadingFailure(ShaderType shaderType) {
        Utils.logError(Utils.formatMessage("shader %s failed to load from raw file", shaderType.toString()));
    }
}
