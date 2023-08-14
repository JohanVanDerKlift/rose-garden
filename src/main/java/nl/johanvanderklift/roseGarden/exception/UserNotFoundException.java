package nl.johanvanderklift.roseGarden.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String username) {
        super("The user with username " + username + " was not found in the database.");
    }
}
