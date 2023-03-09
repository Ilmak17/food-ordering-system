package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderAddress {
    @NotNull
    @Max(value = 50)
    String street;

    @NotNull
    @Max(value = 10)
    String postalCode;

    @NotNull
    @Max(value = 50)
    String city;
}
