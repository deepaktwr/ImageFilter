package proj.android.exceptions;

import proj.android.helper.Utils;

/**
 * Created by root on 25/6/15.
 */
public class BlockExecution extends RuntimeException{
    public BlockExecution(String message){
        super(message);
        Utils.logMessage(message);
    }
    public BlockExecution(String message, Throwable cause){
        super(message, cause);
        Utils.logMessage(message+" by cause "+cause.getCause()+" with message "+cause.getMessage());
    }
    public BlockExecution(Throwable cause){
        super(cause);
        Utils.logMessage("by cause " + cause.getCause() + " with message " + cause.getMessage());
    }
}
