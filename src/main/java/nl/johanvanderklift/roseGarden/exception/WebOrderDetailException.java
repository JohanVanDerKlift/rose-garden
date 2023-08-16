package nl.johanvanderklift.roseGarden.exception;

public class WebOrderDetailException extends RuntimeException {

    public WebOrderDetailException() {
    }

    public WebOrderDetailException(String message) {
        super(message);
    }
}
