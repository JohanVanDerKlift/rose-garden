package nl.johanvanderklift.roseGarden.exception;

public class LastAdminException extends RuntimeException {

    public LastAdminException() {
    }

    public LastAdminException(String username) {
        super("Can not remove last admin with username " + username + ". For the application to run properly there should be at least one admin present!");
    }
}
