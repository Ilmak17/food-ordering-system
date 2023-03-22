package com.food.ordering.system.order.service.messaging.publisher.kafka;

import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.KafkaMessageHelper;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    OrderMessagingDataMapper mapper;
    OrderServiceConfigData configData;
    KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);
        try {
              PaymentRequestAvroModel paymentRequestAvroModel = mapper
                      .orderCreatedEventToPaymentRequestAvroModel(domainEvent);
              kafkaProducer.send(configData.getPaymentRequestTopicName(),
                      orderId,
                      paymentRequestAvroModel,
                      kafkaMessageHelper.getKafkaCallback(configData.getPaymentResponseTopicName(),
                              paymentRequestAvroModel,
                              orderId,
                              "PaymentRequestAvroModel"));

              log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
             log.info("Error while sending PaymentRequestAvroModel " +
                     "message to kafka with order id: {}, error: {}", orderId, e.getMessage());
         }
    }

}
