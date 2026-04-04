package server.exceptions;

public class BannedSubscriberException extends RuntimeException {
    public BannedSubscriberException(String message) {
        super(message);
    }
}
