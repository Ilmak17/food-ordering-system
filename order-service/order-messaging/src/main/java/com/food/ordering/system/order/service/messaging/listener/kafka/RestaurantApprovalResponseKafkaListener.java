package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApprovalMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    OrderMessagingDataMapper mapper;
    RestaurantApprovalMessageListener restaurantApprovalMessageListener;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name")
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of restaurant approval responses received with keys: {}, partitions: {} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());
        messages.forEach(restaurantApprovalResponse -> {
            try {
                if (OrderApprovalStatus.APPROVED == restaurantApprovalResponse.getOrderApprovalStatus()) {
                    log.info("Processing approved for order id: {}",  restaurantApprovalResponse.getOrderId());
                    restaurantApprovalMessageListener.orderApproved(
                            mapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponse)
                    );
                } else if (OrderApprovalStatus.REJECTED == restaurantApprovalResponse.getOrderApprovalStatus()) {
                    restaurantApprovalMessageListener.orderRejected(
                            mapper.approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponse)
                    );
                }
            } catch (OptimisticLockingFailureException e) {
                /* For optimistic lock, this means another thread finished the work,
                do not throw error to prevent reading the data from kafka agan!
                */
                log.error("Caught optimistic locking exception in RestaurantApprovalResponseKafkaListener for order id: {}",
                        restaurantApprovalResponse.getOrderId());
            } catch (OrderNotFoundException e) {
                log.error("No order found for order id: {}", restaurantApprovalResponse.getOrderId());
            }
        });
    }
}
