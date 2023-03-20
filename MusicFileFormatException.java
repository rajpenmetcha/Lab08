import java.io.Serializable;

/**
 * Exception to be thrown when files provided for MusicRecommender is malformed.
 *
 * @version 2022-07-25
 * @author Purdue CS
 */
public class MusicFileFormatException extends Exception {

    /**
     * Calls the constructor of the exception superclass with the message passed in as a parameter
     * @param message The message for the exception
     */
    public MusicFileFormatException(String message) {
        super(message);
    }
}
