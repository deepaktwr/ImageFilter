package proj.android.gl_helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.nio.IntBuffer;

import proj.android.exceptions.BlockExecution;
import proj.android.helper.Utils;

/**
 * Created by deepak on 26/6/15.
 */
public final class GlDecorator {
    /** red combination for clearing the window */
    private static float clearColorr = 0.9f, clearColorrReplica = clearColorr;//0.3,0.5,0.8,1.0
    /** green combination for clearing the window */
    private static float clearColorg = 0.9f, clearColorgReplica = clearColorg;
    /** blue combination for clearing the window */
    private static float clearColorb = 0.9f, clearColorbReplica = clearColorb;
    /** alpha combination for clearing the window */
    private static float clearColora = 1.0f, clearColoraReplica = clearColora;

    /** depth value for clearing the window */
    private static float clearDepth = 1.0f, depthReplica = clearDepth;

    /** opengl view port bottomleft x position from where the window starts */
    private static int bottomLeftX = 0, bottomLeftXReplica = bottomLeftX;
    /** opengl view port bottomleft y position from where the window starts */
    private static int bottomLeftY =0, bottomLeftYReplica = bottomLeftY;
    /** opengl view port width till the upper right corner where the window ends */
    private static int width = 0, widthReplica = width;
    /** opengl view port height till the upper right corner where the window ends */
    private static int height = 0, heightReplica = height;

    private static int bufferBits = GLES30.GL_COLOR_BUFFER_BIT, bufferBitsReplica = bufferBits;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        GlDecorator.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        GlDecorator.screenHeight = screenHeight;
    }

    private static int screenWidth, screenHeight;


    /**
     * will set the default color;
     * this color combination will be used when clearing a window; before clearing a window if this if not mentioned then
     * the default color will be black; it's in surfaceCreated because need not to specify every time because next time the
     * the window will be cleared using glClear it'll take this automatically; if we want to change this clearing color then
     * it needs to specify every time with new composition of RGBA in onDrawFrame with and before glClear
     * */
    public static  void setClearingColorOfWindow() {
        GLES30.glClearColor(clearColorr, clearColorg, clearColorb, clearColora);
    }

    /**
     * will set the default depth;
     * this depth will be used when clearing a window; before clearing a window if this if not mentioned then
     * the default depth will be 1.0f; it's in surfaceCreated because need not to specify every time because next time the
     * the window will be cleared using glClear it'll take this automatically; if we want to change this depth then
     * it needs to specify every time with new depth in onDrawFrame with and before glClear
     * */
    public static  void setClearingDepthOfWindow() {
        GLES30.glClearDepthf(clearDepth);
    }
    /**
     * this color combination will be used when clearing a window; before clearing a window if this if not mentioned then
     * the default color will be black;
     * need to be called this function if want to change the opengl window color
     * @param redComponent the red component used to clear window
     * @param greenComponent the green component used to clear window
     * @param blueComponent the blue component used to clear window
     * @param alphaComponent the alpha component used to clear window
     * */
    public static void initializeClearingColorOfWindow(float redComponent, float greenComponent, float blueComponent,
                                                float alphaComponent){
        clearColorr = redComponent;
        clearColorg = greenComponent;
        clearColorb = blueComponent;
        clearColora = alphaComponent;
    }

    /**
     * this depth will be used when clearing a window; before clearing a window if this if not mentioned then
     * the default depth will be 1f;
     * need to be called this function if want to change the opengl window depth
     * @param depthComponent the depth used to clear window
     * */
    public static void initializeClearingDepthOfWindow(float depthComponent){
        clearDepth = depthComponent;
    }
    /**
     * clear the color drawn to the window and use the color specified by glClearColor to polish it again;
     * GL_COLOR_BUFFER_BIT specify that only the color drawn to the opengl window will be cleared then polished
     * if we also want to clear the depth of the window which is declared at the time of creating shapes via W component with
     * X Y and Z , that W component of depth will be cleared using GLES30.GL_DEPTH_BUFFER_BIT with
     * GLES30.GL_COLOR_BUFFER_BIT in | (or) adjacent
     * */
    public static  void clearWindowWithBuffers() {
        GLES30.glClear(bufferBits);
    }
    /**
     * clear the color drawn to the window and use the color specified by glClearColor to polish it again;
     * GL_COLOR_BUFFER_BIT specify that only the color drawn to the opengl window will be cleared then polished
     * if we also want to clear the depth of the window which is declared at the time of creating shapes via W component with
     * X Y and Z , that W component of depth will be cleared using GLES30.GL_DEPTH_BUFFER_BIT with
     * GLES30.GL_COLOR_BUFFER_BIT in | (or) adjacent
     * @param buffersToClear the buffer on the window which needs to clear
     * */
    public static  void intializeClearBuffers(int buffersToClear) {
        bufferBits = buffersToClear;
    }
    /**
     * it should be called when window size of opengl need to be set or change;
     * the window size of opengl is defied via glViewPort
     * */
    public static  void setViewPort() {
        GLES30.glViewport(bottomLeftX, bottomLeftY, width, height);
    }

    public static int getScreenWidthMeasure(){
        return width-bottomLeftX;
    }
    public static int getScreenHeightMeasure(){
        return height-bottomLeftY;
    }
    /**
     * it should be called when window size of opengl need to be change;
     * should be called when need to apply new dimentions to the window
     * @param nBottomLeftX the new view port bottom left starting x point
     * @param nBottomLeftY the new view port bottom left starting y point
     * @param nWidth the new view port max x width
     * @param nHeight the new view port max y height
     * @see
     * */
    public static void initializeViewPort(int nBottomLeftX, int nBottomLeftY, int nWidth, int nHeight) {
        bottomLeftX = nBottomLeftX;
        bottomLeftY = nBottomLeftY;
        width = nWidth;
        height = nHeight;
    }

    /**
     * load and compile the shader of particular stage or type.
     * @param shaderType type of the shader (either fragment or vertex)
     * @param shaderSourceCode shader type source code to compile
     * */
    public static int loadShader(ShaderType shaderType, String shaderSourceCode){
        int shaderObject = createShader(shaderType, shaderSourceCode);
        compileShader(shaderType, shaderObject);
        return shaderObject;
    }
    /**
     * load and link program with opengl along with attaching the compiled shaders.
     * for the first time we have to give both vertex as well as fragment shader for successful linking of the program.
     * @param shaderObject the compiled shader object or objects
     * @param newProgram if new program needs to linked with the window;
     *                   the previous program object will be deleted.
     *                   <pl>
     *                   <li><code>true</code> the new program will be created in order to attach existing shaders</li>
     *                   <li><code>false</code> the previous attached program will be used to attach existing shaders if any;
     *                   otherwise new program object will be created</li>
     *                   </pl>
     *
     * @param newWindow the new window will be polished as user requirment i.e colors, bufferbits
     *                  <pl>
     *                  <li><code>true</code> if any new color change, bufferbit change, or view port coordinate change
     *                  detected then new window will be created again</li>
     *                  <li><code>false</code> the default window will be used</li>
     *                  </pl>
     * */
    public static int loadProgram(boolean newProgram, boolean newWindow, Integer ... shaderObject){
        if((programObject == 0 || newProgram) && shaderObject.length < 2)
            return 0;
        int program = createProgram(newProgram, newWindow, shaderObject);
        Utils.setIsProgramReady(true);
        return  program;
    }

    /**
     * it will create a shaderObject in integer type shader object;
     * the object define in GLSL may be type of GLuint(unsigned int type), GLufloat, GLint etc;
     * after creating object we will pass the source to this object i.e the code we want to compile for particular
     * shader type
     * @param shaderType the type of the shader
     * @param shaderSourceCode the source code in string to compile
     * */
    private static  int createShader(ShaderType shaderType, String shaderSourceCode){

        int shaderObject = GLES30.glCreateShader(shaderType.getType());
        GLES30.glShaderSource(shaderObject, shaderSourceCode);
        return shaderObject;
    }

    /**
     * compile the shaderObject which has been created before
     * @param shaderType the type of the shader
     * @param shaderObject the object of shader of particular shader type to compile
     * */
    private static  void compileShader(ShaderType shaderType, int shaderObject){
        GLES30.glCompileShader(shaderObject);

        int[] compileAndLogStatus = new int[2];

        getShaderiv(shaderObject, GLES30.GL_COMPILE_STATUS, compileAndLogStatus, 0);

        String shadeTypeString = null;
        switch(shaderType){
            case VERTEX_SHADER:
                shadeTypeString = "vertex shader";
                break;
            case FRAGMENT_SHADER:
                shadeTypeString = "fragment shader";
                break;

        }

        if(compileAndLogStatus[0] == GLES30.GL_FALSE){
            //getting the information of compilation failure
            String infoLog = GLES30.glGetShaderInfoLog(shaderObject);
            //getting info log length of failure
            getShaderiv(shaderObject, GLES30.GL_INFO_LOG_LENGTH, compileAndLogStatus, 1);

            throw  new BlockExecution(String.format("compile failure in %s with log %s and log length %s", shadeTypeString, infoLog, "" + compileAndLogStatus[1]));
        }
        else
            Utils.logMessage(String.format("compile success in %s", shadeTypeString));
    }
    /**
     * returns the parameter from the shader object onto the operation has been performed;
     * the operations can GL_SHADER_TYPE, GL_DELETE_STATUS, GL_COMPILE_STATUS, GL_INFO_LOG_LENGTH, or GL_SHADER_SOURCE_LENGTH
     * @param shaderObject the object which has been compiled
     * @param operationToPerform (like GLES30.GL_COMPILE_STATUS) operation which will be performed on shaderObject
     * @param result the array in which the result will be filled on certain position
     * @param position the position onto which the result will be filled
     * */
    private static  void getShaderiv(int shaderObject, int operationToPerform, int[] result, int position) {
        GLES30.glGetShaderiv(shaderObject, operationToPerform, result, position);
    }

    private static int programObject = 0;
    /**
     * the program object is combination of all shader stages. we can create a program here and can attach all the shader
     * with it.it's not the program responsibility to remember stage of particular shader object, shader object itself do
     * this on it's own.
     * after attaching the shaders with the created program we need to link the program by which it can processed further.
     * we should also check the linking status of the program after that.
     * first time program needs at least two shaders ie one vertex and one fragment shader to perform a successful link.
     * @param shaderObject shader object or objects of particular stage or stages which needs to attach with the program
     * @param newProgramObject if new program needs to linked with the window;
     *                   the previous program object will be deleted.
     *                   <pl>
     *                   <li><code>true</code> the new program will be created in order to attach existing shaders</li>
     *                   <li><code>false</code> the previous attached program will be used to attach existing shaders if any;
     *                   otherwise new program object will be created</li>
     *                   </pl>
     *
     * @param clearWindow the new window will be polished as user requirment i.e colors, bufferbits
     *                  <pl>
     *                  <li><code>true</code> if any new color change, bufferbit change, or view port coordinate change
     *                  detected then new window will be created again</li>
     *                  <li><code>false</code> the default window will be used</li>
     *                  </pl>
     * */
    private static  int createProgram(boolean newProgramObject, boolean clearWindow,Integer ... shaderObject){

        if(newProgramObject  && programObject != 0)
            detachOldProgramObject();
        if(clearWindow)
            clearWindow();
        //create the program, synchronized because it may not an atomic operation
        synchronized (GlDecorator.class) {
            if (programObject == 0)
                programObject = GLES30.glCreateProgram();
        }

        //attach shader
        for(int shaderObj : shaderObject)
            GLES30.glAttachShader(programObject, shaderObj);
        //link program
        GLES30.glLinkProgram(programObject);

        int[] programLinkAndLogStatus = new int[2];
        //getting program link status
        getProgramiv(programObject, GLES30.GL_LINK_STATUS, programLinkAndLogStatus, 0);

        if(programLinkAndLogStatus[0] ==  GLES30.GL_FALSE) {
            //getting info log length of failure
            getProgramiv(programObject, GLES30.GL_INFO_LOG_LENGTH, programLinkAndLogStatus, 1);
            //getting the information of linking failure
            String programLog = GLES30.glGetProgramInfoLog(programObject);
            detachOldProgramObject();
            throw new BlockExecution(String.format("program linking failure %s with log length %s", programLog,
                    programLinkAndLogStatus[1]));
        }
        else
            Utils.logMessage(String.format("program linking success"));

        return resetShaderAndProgram(programObject, shaderObject);
    }

    /**
     * After linking the program with attached shaders of different stages we may detach those shaders from the program.
     * the program linking status won't be affected by detaching the shaders, only these shaders won't be associated
     * with the program.
     * after detaching the shaders with program their are no use of shaders so we can delete those shadres
     * @param program the program object
     * @param shaderObject the shades object*/
    private static  int resetShaderAndProgram(int program, Integer ... shaderObject){
        for(int shaderObj : shaderObject) {
            GLES30.glDetachShader(program, shaderObj);
            Utils.logMessage(String.format("shader %s detached from program %s", shaderObj, program));
            GLES30.glDeleteShader(shaderObj);
            Utils.logMessage(String.format("shader %s deleted", shaderObj));
        }
        return program;
    }

    /**
     * returns the parameter from the program object onto the operation has been performed;
     * the operations can GL_ACTIVE_ATTRIBUTES, GL_ACTIVE_UNIFORMS, GL_ACTIVE_UNIFORM_BLOCKS,
     * GL_ACTIVE_UNIFORM_MAX_LENGTH, GL_ATTACHED_SHADERS, GL_DELETE_STATUS, GL_INFO_LOG_LENGTH, GL_LINK_STATUS etc...
     * @param programObject the program object which has been created
     * @param operationToPerform (like GLES30.GL_LINK_STATUS) operation which will be performed on programObject
     * @param result the array in which the result will be filled on certain position
     * @param position the position onto which the result will be filled
     * */
    private static  void getProgramiv(int programObject, int operationToPerform, int[] result, int position){
        GLES30.glGetProgramiv(programObject, operationToPerform, result, position);
    }
    /**
     * should be called after setting the new buffer colors, buffers bits to be cleared when generation new window and
     * view port screen co-ordinates;
     * if any of them needs to be changed then call this function
     * */
    private static void clearWindow(){
        boolean colorChange = clearColoraReplica != clearColora || clearColorbReplica != clearColorb ||
                clearColorgReplica != clearColorg || clearColorrReplica != clearColorr;
        boolean depthChange = clearDepth != depthReplica;
        if(colorChange) {
            setClearingColorOfWindow();
            clearColoraReplica = clearColora; clearColorbReplica = clearColorb;
            clearColorgReplica = clearColorg; clearColorrReplica = clearColorr;
        }
        boolean bufferChange = bufferBitsReplica != bufferBits;
        //if(colorChange || bufferChange || depthChange) {
            clearWindowWithBuffers(); if(bufferChange) bufferBitsReplica = bufferBits;
        //}
        if(bottomLeftXReplica!= bottomLeftX || bottomLeftYReplica != bottomLeftY ||
                widthReplica != width || heightReplica != height){
            setViewPort();
            bottomLeftXReplica = bottomLeftX; bottomLeftYReplica = bottomLeftY;
            widthReplica = width; heightReplica = height;
        }
    }

    /**
     * should be called when need to delete a program object currently attached with the window;
     * also can have the deletion status, it could be false because opengl won't remove it until it's not
     * totaly detached to the window.
     * all the shaders attached with this program object will automatically detached but not delete until previously
     * deletion has been requested on that shader
     * */
    private static void detachOldProgramObject(){
        GLES30.glDeleteProgram(programObject);
        GLES30.glUseProgram(0);
        int[] programDeletionStatus = new int[2];
        GLES30.glGetProgramiv(programObject, GLES30.GL_DELETE_STATUS, programDeletionStatus, 0);

        if(programDeletionStatus[0] == GLES30.GL_FALSE){
            GLES30.glGetProgramiv(programObject, GLES30.GL_INFO_LOG_LENGTH, programDeletionStatus, 1);
            String logMessage = GLES30.glGetProgramInfoLog(programObject);
            Utils.logMessage(Utils.formatMessage("program %d has not been delete yet, log: %s with loglength %d",
                    programObject, logMessage, programDeletionStatus[1]));
        }else
            Utils.logMessage(Utils.formatMessage("program %d deletion was successful", programObject));
        GLES30.glDisableVertexAttribArray(0);
        programObject = 0;
    }
    public final static int getProgramObject(){
        return programObject;
    }





    private static final IntBuffer textureBufferObject = IntBuffer.allocate(1);
    /**
     * to load texture on the screen
     *
     * */
    public static void loadTextureFromResource(Context context,int resId){
        GLES30.glGenTextures(1, textureBufferObject);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureBufferObject.get(0));
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);

        setBitmap(context, resId);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, getBitmap(), 0);
    }
    public static IntBuffer getTextureBufferObject(){
        return textureBufferObject;
    }

    private static Bitmap bitmap;
    private static final Bitmap getBitmap(){
        return bitmap;
    }
    public static void setBitmap(Context context, int resId){
        if(bitmap == null) {
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inScaled = false;
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, op);
        }
    }
}