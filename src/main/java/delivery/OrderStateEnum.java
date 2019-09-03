package delivery;

public enum OrderStateEnum {

    CART,        // the order is only in the cart
    COMPLETED,   // the user confirmed the order
    IN_PROCESS,  // the producer started order implementation
    READY,       // the order is ready for delivery
    IN_DELIVERY, // the order is being delivered
    DELIVERED,   // the order is delivered, but we work with proclamations
    CLOSED,      // the order is delivered and accepted

    OrderStateEnum() {}
}
