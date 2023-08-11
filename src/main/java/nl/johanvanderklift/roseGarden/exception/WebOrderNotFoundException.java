package nl.johanvanderklift.roseGarden.exception;

public class WebOrderNotFoundException extends RuntimeException {

    public WebOrderNotFoundException() {
    }

    public WebOrderNotFoundException(Long webOrderId) {
        super("Weborder with id " + webOrderId + " was not found.");
    }

}
