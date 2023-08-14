package nl.johanvanderklift.roseGarden.exception;

public class WebOrderStatusException extends RuntimeException {

    public WebOrderStatusException() {
    }

    public WebOrderStatusException(String message) {
        super(message);
    }
}
