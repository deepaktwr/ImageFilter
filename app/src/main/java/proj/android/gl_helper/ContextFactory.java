package proj.android.gl_helper;


import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public class ContextFactory implements GLSurfaceView.EGLContextFactory {

  private static int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    private static double glVersion = 3.0;

    @Override
    public javax.microedition.khronos.egl.EGLContext createContext(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eglDisplay, javax.microedition.khronos.egl.EGLConfig eglConfig) {
        Log.w("s", "creating OpenGL ES " + glVersion + " context");
        int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, (int) glVersion,
                EGL10.EGL_NONE };
        // attempt to create a OpenGL ES 3.0 context
        EGLContext context = egl10.eglCreateContext(
                eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
        return context; // returns null if 3.0 is not supported;
    }

    @Override
    public void destroyContext(EGL10 egl10, javax.microedition.khronos.egl.EGLDisplay eglDisplay, javax.microedition.khronos.egl.EGLContext eglContext) {

    }
}