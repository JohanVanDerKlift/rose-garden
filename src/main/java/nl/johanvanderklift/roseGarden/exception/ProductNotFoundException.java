package nl.johanvanderklift.roseGarden.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
    }

    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " was not found.");
    }
}
