package nl.johanvanderklift.roseGarden.exception;

public class NotAuthorisedException extends RuntimeException {
    public NotAuthorisedException() {
    }

    public NotAuthorisedException(String message) {
        super(message);
    }
}
