package com.food.ordering.system.dataaccess.restaurant.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RestaurantEntityId.class)
@Entity
@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {

    @Id
    private UUID id;

    @Id
    private UUID productId;

    private String restaurantName;

    private String productName;

    private Boolean restaurantActive;

    private BigDecimal productPrice;
    private Boolean productAvailable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return restaurantActive == that.restaurantActive && Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(productName, that.productName) && Objects.equals(productPrice, that.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, restaurantName, productName, restaurantActive, productPrice);
    }
}
