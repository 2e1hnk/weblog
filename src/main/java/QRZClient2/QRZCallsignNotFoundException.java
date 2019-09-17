package QRZClient2;

public class QRZCallsignNotFoundException extends Exception {

    public QRZCallsignNotFoundException(String message) {
        super(message);
    }

    public QRZCallsignNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}