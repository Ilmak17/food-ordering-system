package com.food.ordering.system.order.service.domain.dto.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderItem {
    @NotNull
    UUID productId;

    @NotNull
    Integer quantity;

    @NotNull
    BigDecimal price;

    @NotNull
    BigDecimal subtotal;
}
