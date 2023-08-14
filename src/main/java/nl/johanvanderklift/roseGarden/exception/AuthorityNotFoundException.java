package nl.johanvanderklift.roseGarden.exception;

public class AuthorityNotFoundException extends RuntimeException{

    public AuthorityNotFoundException() {
    }

    public AuthorityNotFoundException(String authority) {
        super("Authority " + authority + " doesn't exist.");
    }
}
