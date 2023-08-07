package nl.johanvanderklift.roseGarden.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String email) {
        super("The user with email " + email + " was not found in the database.");
    }
}
