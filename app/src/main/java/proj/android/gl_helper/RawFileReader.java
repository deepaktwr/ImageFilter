package proj.android.gl_helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import proj.android.exceptions.FilterException;
import proj.android.fragments.FilterFragment;

/**
 * Created by deepak on 12/6/15.
 */
public class RawFileReader{

    public static String getShaderFromRaw(Context context,int shaderRawId)
            throws FilterException{
        StringBuilder stringBuilder = new StringBuilder();
        InputStream in=context.getResources().openRawResource(shaderRawId);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader br=new BufferedReader(inputStreamReader);

        String next=null;
        try{
            while((next=br.readLine())!=null){
                stringBuilder.append(next);
                stringBuilder.append("\n");
            }
        }catch(IOException exception){
            throw new FilterException("could not able to read raw file", exception);
        }

        return stringBuilder.toString();
    }
}
