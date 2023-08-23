package nl.johanvanderklift.roseGarden.exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException() {
    }

    public FileNotFoundException(Long id) {
        super("File with id " + id + " not found.");
    }
}
