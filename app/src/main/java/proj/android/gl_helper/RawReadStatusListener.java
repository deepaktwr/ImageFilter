package proj.android.gl_helper;

/**
 * Created by deepak on 25/6/15.
 */
public interface RawReadStatusListener {
    void onReadingComplete(String shaderSource, ShaderType shaderType, Boolean ... modification);
    void onReadingFailure(ShaderType shaderType);
}
