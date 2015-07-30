package proj.android.gl_filter;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import proj.android.gl_helper.GlDecorator;
import proj.android.gl_helper.RawFileReader;
import proj.android.gl_helper.RawReadStatusListener;
import proj.android.gl_helper.ShaderType;
import proj.android.gl_shapes.DrawImageTexture;
import proj.android.helper.Utils;

/**
 * Created by deepak on 28/7/15.
 */
public class DemoImageFilter implements RawReadStatusListener {
    private static DemoImageFilter demoImageFilter;
    private DrawImageTexture drawImageTexture;
    private RawFileReader rawFileReader;
    private Context context;

    public static DemoImageFilter getInstance(Context context){
        if(demoImageFilter == null)
            demoImageFilter=new DemoImageFilter();
        demoImageFilter.init(context);
        return demoImageFilter;
    }

    private void init(Context context){
        this.context=context;
        rawFileReader = new RawFileReader(context, this);
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

    public void drawTexture(int vertexShaderId, int fragmentShaderId){
        loadShaders(vertexShaderId, fragmentShaderId, true, true);
        drawImageTexture.drawTexture();
    }

    @Override
    public void onReadingComplete(String shaderSource, ShaderType shaderType, Boolean... modification) {
        Utils.logMessage(Utils.formatMessage("shader %s loaded from raw file", shaderType.toString()));
        //Utils.logMessage(shaderSource);
        int shader = GlDecorator.loadShader(shaderType, shaderSource);
        shaders.add(shader);
        Integer[] intParam = new Integer[shaders.size()];
        GlDecorator.loadProgram(modification.length > 0 ? modification[0] : false, modification.length > 1 ? modification[1] : false,
                shaders.toArray(intParam));
    }

    @Override
    public void onReadingFailure(ShaderType shaderType) {
        Utils.logError(Utils.formatMessage("shader %s failed to load from raw file", shaderType.toString()));
    }
}
