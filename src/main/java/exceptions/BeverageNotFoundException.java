package exceptions;

/**
 * Thrown when requested Beverage is unsupported.
 */
public class BeverageNotFoundException extends Exception {
    public BeverageNotFoundException(String message) {
        super(message);
    }
}
