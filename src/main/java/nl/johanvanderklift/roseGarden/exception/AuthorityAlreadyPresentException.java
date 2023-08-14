package nl.johanvanderklift.roseGarden.exception;

public class AuthorityAlreadyPresentException extends RuntimeException {

    public AuthorityAlreadyPresentException() {
    }

    public AuthorityAlreadyPresentException(String auth) {
        super("Authority " + auth + " already belongs to user.");
    }
}
