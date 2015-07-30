package proj.android.gl_helper;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import proj.android.exceptions.BlockExecution;
import proj.android.exceptions.FilterException;
import proj.android.fragments.FilterFragment;
import proj.android.helper.Utils;

/**
 * reads the file from raw resource
 * @author deepak
 */
public class RawFileReader{

    private Context context;
    private RawReadStatusListener statusListener;
    public RawFileReader(Context context, RawReadStatusListener statusListener){
        this.context=context;
        this.statusListener=statusListener;
    }

    /**
     * read the shader code from raw file
     * @param shaderRawId the id of raw file to read
     * @throws IOException
     * @see BlockExecution
     * */
    public void getShaderFromRaw(int shaderRawId, ShaderType shaderType, Boolean ... modification)
            throws IOException{
        StringBuilder stringBuilder = new StringBuilder();
        if(shaderRawId == 0 || context ==null)
            throw new BlockExecution("parameters not valid for reading the raw file");
        try(InputStream in=context.getResources().openRawResource(shaderRawId);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader br=new BufferedReader(inputStreamReader, 4*1024)) {


            String next;
            if(br == null)
                throw new BlockExecution("could not read data from file with id "+shaderRawId);
            while ((next = br.readLine()) != null) {
                stringBuilder.append(next);
                stringBuilder.append("\n");
             }
        }finally{
            if(!TextUtils.isEmpty(stringBuilder))
                statusListener.onReadingComplete(stringBuilder.toString(), shaderType, modification);
            else
                statusListener.onReadingFailure(shaderType);
        }
    }
/*    public void getShaderFromRawAsync(int shaderRawId){
        //AsynchronousFileChannel is not present in Android yet
    }*/
    /**
     * read the shader d=source code from raw file
     * @param shaderRawId the raw file id from context
     * @param shaderType type of the shader as fragment or vertex
     * @throws IOException whenever an input/output error comes
     * @throws FilterException whenever other errors come
     * */
    public void readShaderFromRawChannel(int shaderRawId, ShaderType shaderType,String fileName) throws IOException{
        StringBuilder data = new StringBuilder();
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + shaderRawId);
        try(FileInputStream fileStream = new FileInputStream(uri.toString());
            FileChannel fileChannel = fileStream.getChannel()){
            if(fileChannel == null)
                throw new BlockExecution("the channel is null, could not found file at raw id "+shaderRawId+" at path "+uri.toString());

            ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
            buffer.clear();
            while (fileChannel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining())
                    data.append((char) buffer.get());
                buffer.clear();
            }
        }finally {
            if(!TextUtils.isEmpty(data))
                statusListener.onReadingComplete(data.toString(), shaderType);
            else
                statusListener.onReadingFailure(shaderType);
        }
    }
}
