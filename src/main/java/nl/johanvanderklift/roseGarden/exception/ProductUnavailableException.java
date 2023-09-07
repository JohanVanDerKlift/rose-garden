package nl.johanvanderklift.roseGarden.exception;

public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException() {
    }

    public ProductUnavailableException(String productName) {
        super(productName + " is not available at the moment");
    }
}
