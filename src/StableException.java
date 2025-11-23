public class StableException extends Exception {
    public StableException(String message) {
        super(message);
    }

    public StableException(String message, Throwable cause) {
        super(message, cause);
    }
}