package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantAppliactionServiceException extends DomainException {
    public RestaurantAppliactionServiceException(String message) {
        super(message);
    }

    public RestaurantAppliactionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
