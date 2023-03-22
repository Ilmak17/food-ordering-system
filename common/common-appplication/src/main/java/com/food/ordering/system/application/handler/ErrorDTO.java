package com.food.ordering.system.application.handler;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDTO {
    String code;
    String message;
}
