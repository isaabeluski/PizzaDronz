package uk.ac.ed.inf;

/**
 * Exception used to inform the user when an invalid combination in an
 * order is made.
 */
public class InvalidPizzaCombinationException extends Exception{

    /**
     * Exception used when an invalid pizza combination is made in an order.
     * @param errorMessage Explains why the combination is invalid.
     */
    public InvalidPizzaCombinationException(String errorMessage) {
        super(errorMessage);
    }
}
