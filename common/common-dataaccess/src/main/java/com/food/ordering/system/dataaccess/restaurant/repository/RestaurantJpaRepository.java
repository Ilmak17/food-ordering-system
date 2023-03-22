package com.food.ordering.system.dataaccess.restaurant.repository;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID> {

    Optional<List<RestaurantEntity>> findByIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);

    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(UUID value, List<UUID> restaurantProducts);
}
