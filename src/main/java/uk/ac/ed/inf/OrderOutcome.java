package uk.ac.ed.inf;

/**
 * Represents the order outcome of an order.
 */
public enum OrderOutcome {

    Delivered ,
    ValidButNotDelivered ,
    InvalidCardNumber ,
    InvalidExpiryDate ,
    InvalidCvv ,
    InvalidTotal ,
    InvalidPizzaNotDefined ,
    InvalidPizzaCount ,
    InvalidPizzaCombinationMultipleSuppliers ,
    Invalid,
    NotChecked

}
