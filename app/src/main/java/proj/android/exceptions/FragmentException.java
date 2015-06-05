package proj.android.exceptions;

import proj.android.helper.Utils;

/**
 * Created by deepak on 24/5/15.
 */
public class FragmentException extends Exception{
    public FragmentException(){
        Utils.logMessage("exception occurred in fragment behaviour");
    }
    public FragmentException(String message){
        super(message);
        Utils.logMessage(message);
    }
    public FragmentException(Throwable cause){
        super(cause);
        Utils.logMessage(cause.getMessage());
    }
    public FragmentException(String message, Throwable cause){
        super(message, cause);
        Utils.logMessage(message+" by cause "+cause.getMessage());
    }
}
