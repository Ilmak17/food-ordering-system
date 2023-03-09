package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestaurantId;

import java.util.List;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final List<Product> productList;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.id);
        productList = builder.productList;
        active = builder.active;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public boolean isActive() {
        return active;
    }

    public static final class Builder {
        private RestaurantId id;
        private List<Product> productList;
        private boolean active;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RestaurantId val) {
            id = val;
            return this;
        }

        public Builder productList(List<Product> val) {
            productList = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
