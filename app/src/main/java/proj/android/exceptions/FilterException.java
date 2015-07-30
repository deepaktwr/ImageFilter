package proj.android.exceptions;

import proj.android.helper.Utils;

/**
 * Created by deepak on 24/5/15.
 */
public class FilterException extends Exception{
    public FilterException(){
        Utils.logMessage("exception occurred in filtering");
    }
    public FilterException(String message){
        super(message);
        Utils.logMessage(message);
    }
    public FilterException(Throwable cause){
        super(cause);
        Utils.logMessage("by cause "+cause.getCause()+" with message "+cause.getMessage());
    }
    public FilterException(String message, Throwable cause){
        super(message, cause);
        Utils.logMessage(message+" by cause "+cause.getCause()+" with message "+cause.getMessage());
    }
}
