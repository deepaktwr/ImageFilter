package proj.android.gl_shapes;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import proj.android.gl_helper.GlDecorator;
import static android.opengl.GLES30.*;

/**
 * Created by deepak on 27/6/15.
 */
public class Draw2DShapes {
    /**
     * sequesce of coordinates -> ( ++, -+, --, +- )
     * range 0-1
     * */
    private float c(float val){
        return val/100f;
    }
    public void drawSimpleLine(){
        float[] lineCoords = {
                0.4f, 0.3f, 1.0f, 1.0f,
                -0.2f, 0.8f, 1.0f, 1.0f
        };
        //1 because number of object bind is only 1
        IntBuffer objectBuffer = IntBuffer.allocate(1);

        //use the program we we have created
        glUseProgram(GlDecorator.getProgramObject());
        // generate n buffer objects  which we need to process, here 1 is n, second param is IntBuffer which takes n1 allocation
        // bytes for n number of object, where n1 = total number of objects to bind
        glGenBuffers(1, objectBuffer);
        // bind any one object with the array buffer
        glBindBuffer(GL_ARRAY_BUFFER, objectBuffer.get());

        //32 bytes as float data is 8*4 bytes per float data
        FloatBuffer vertexBuffer = FloatBuffer.allocate(lineCoords.length);
        vertexBuffer.put(lineCoords);
        //so that the opengl start reading from beggening
        vertexBuffer.position(0);
        // fill data in the array buffer
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GL_STATIC_DRAW);

        //positon can be set form GLSL layout=0
        glEnableVertexAttribArray(0);
        //tell opengl how to process data
        glVertexAttribPointer(0, 4, GL_FLOAT, false/*GLES20.GL_FALSE*/, 0, 0);
        //draw it on window
        glDrawArrays(GL_LINES, 0, 2);
        // empty array buffer data
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    public void drawTwoLines(){
        float[] line1={
                0.2f, 0.1f, 1.0f, 1.0f,
                0.8f, 0.9f, 1.0f, 1.0f
        };
        float[] line2={
                0.4f, 0.5f, 1.0f, 1.0f,
                0.1f, 0.3f, 1.0f, 1.0f
        };

        IntBuffer objectToBind = IntBuffer.allocate(2);
        FloatBuffer coordData = FloatBuffer.allocate(line1.length+line2.length);
        coordData.put(line1);
        coordData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(2, objectToBind);
        glBindBuffer(GL_ARRAY_BUFFER, objectToBind.get());

        glBufferData(GL_ARRAY_BUFFER, (coordData.capacity() / 2) * 4, coordData, GL_STATIC_DRAW);
        //must be zero
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_LINES, 0, 2);

        //coordData.position(coordData.capacity()-1);
        coordData.mark();
        coordData.put(line2);
        coordData.reset();

        glBindBuffer(GL_ARRAY_BUFFER, objectToBind.get());
        glBufferData(GL_ARRAY_BUFFER, (coordData.capacity() / 2) * 4, coordData, GL_STATIC_DRAW);
       // glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_LINES, 0, 2);
        glBindBuffer(GL_ARRAY_BUFFER, 0);


    }
    public void drawStripLines(){
        float[] stripLineCo1={
                .35f, .25f, 1f, 1f,
                -.1f, .9f, 1f, 1f
        };
        float[] stripLineCo2={
                -.7f, .2f, 1f, 1f,
                -.2f, -.18f, 1f, 1f
        };
        float[] stripLineCo3={
                -.07f, -.7f, 1f, 1f,
                .3f, -.2f, 1f, 1f
        };

        IntBuffer objectToBind = IntBuffer.allocate(1);
        FloatBuffer buffer = FloatBuffer.allocate(stripLineCo1.length+stripLineCo2.length+stripLineCo3.length);

        buffer.put(stripLineCo1);
        buffer.put(stripLineCo2);
        buffer.put(stripLineCo3);
        buffer.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(1, objectToBind);
        glBindBuffer(GL_ARRAY_BUFFER, objectToBind.get());
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, buffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        //in strip it'll connect last to first except the final one
        glDrawArrays(GL_LINE_STRIP, 0, 6);
        //in lo0p it'll connect last to first int all
        //glDrawArrays(GL_LINE_LOOP, 0, 6);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public void drawSimpleTriangle(){
        float[] triCoord = {
                c(30), c(15), 1f, 1f,
                c(-35), c(50), 1f, 1f,
                c(-40), c(-80), 1f, 1f
        };
        IntBuffer genBuff = IntBuffer.allocate(1);
        FloatBuffer buffData = FloatBuffer.allocate(triCoord.length);
        buffData.put(triCoord);
        buffData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(1, genBuff);
        glBindBuffer(GL_ARRAY_BUFFER, genBuff.get());

        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    public void drawTwoTriangles(){
        float[] tri1={
                c(40), c(20), 1f, 1f,
                c(15), c(-25), 1f, 1f,
                c(70), c(-20), 1f, 1f
        };
        float[] tri2={
                c(-50), c(-5), 1f, 1f,
                c(-90), c(-50), 1f, 1f,
                c(-15), c(-45), 1f, 1f
        };
        IntBuffer genBuff = IntBuffer.allocate(2);
        FloatBuffer buffData = FloatBuffer.allocate(tri1.length+tri2.length);
        buffData.put(tri1);
        buffData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(2, genBuff);
        glBindBuffer(GL_ARRAY_BUFFER, genBuff.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        buffData.mark();
        buffData.put(tri2);
        buffData.reset();

        glBindBuffer(GL_ARRAY_BUFFER, genBuff.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }
    public void drawStripTriangles(){
        float[] tri1={
                c(70), c(40), 1f, 1f,
                c(55), c(75), 1f, 1f,
                c(35), c(35), 1f, 1f

        };
        float[] tri2={
                c(-60), c(0), 1f, 1f,
                c(-65), c(60), 1f, 1f,
                c(-90), c(-10), 1f, 1f,

        };
        float[] tri3={
                c(35), c(-75), 1f, 1f,
                c(10), c(-10), 1f, 1f,
                c(-15), c(-80), 1f, 1f,

        };

        float[] triN1={
                c(70), c(-50), 1f, 1f,
                c(55), c(-15), 1f, 1f,
                c(35), c(-55), 1f, 1f

        };
        float[] triN2={
                c(-60), c(-90), 1f, 1f,
                c(-65), c(-30), 1f, 1f,
                c(-90), c(-100), 1f, 1f,

        };

        IntBuffer genBuff = IntBuffer.allocate(2);
        FloatBuffer buffData = FloatBuffer.allocate(tri1.length+tri2.length+triN1.length+triN2.length);
        buffData.put(tri1);
        buffData.put(tri2);
        buffData.put(triN1);
        buffData.put(triN2);
        buffData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(2, genBuff);
        glBindBuffer(GL_ARRAY_BUFFER, genBuff.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        glDrawArrays(GL_TRIANGLES, 6, 12);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void drawSimpleRectengle(){
        float[] quard={
                c(50), c(-40),1f,1f,
                c(60),c(20),1f,1f,
                c(-15),c(15),1f,1f,
                c(-80),c(-70),1f,1f
        };
        float[] tri1={
                c(50), c(-40),1f,1f,
                c(60),c(20),1f,1f,
                c(-15),c(15),1f,1f,
        };
        float[] tri2={
                c(50), c(-40),1f,1f,
                c(-15),c(15),1f,1f,
                c(-80),c(-70),1f,1f

        };
        IntBuffer genBuff = IntBuffer.allocate(1);
        FloatBuffer buffData = FloatBuffer.allocate(tri1.length+tri2.length);
        buffData.put(tri1);
        buffData.put(tri2);
        buffData.position(0);

        glUseProgram(GlDecorator.getProgramObject());
        glGenBuffers(1,genBuff);
        glBindBuffer(GL_ARRAY_BUFFER, genBuff.get());
        glBufferData(GL_ARRAY_BUFFER, buffData.capacity() * 4, buffData, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0,4,GL_FLOAT,false,0,0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
}
