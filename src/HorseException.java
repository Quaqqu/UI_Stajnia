/**
 * Wyjątek rzucany przy operacjach na koniach
 */
public class HorseException extends Exception {
    
    public HorseException(String message) {
        super(message);
    }
    
    public HorseException(String message, Throwable cause) {
        super(message, cause);
    }
}
