package com.platform.user_service.messaging.producer;

import com.platform.user_service.configs.rabbit.exchanges.ExchangeAbstractConfig;
import com.platform.user_service.dtos.common.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;
/**
 * Producer class for publishing new user events to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserProducer {
    /** RabbitTemplate for sending messages to RabbitMQ. */
    private final RabbitTemplate rabbitTemplate;
    /** Configuration for the user created exchange. */
    private final ExchangeAbstractConfig userCreatedExchangeConfig;

    /**
     * Publishes a new user event to the RabbitMQ exchange.
     *
     * @param userDto the user DTO containing new user details.
     */
    public void publishNewUserEvent(UserDto userDto) {
        try {
            log.debug("Entering publishNewUserEvent with newUserMessageDto: {}", userDto);
            rabbitTemplate.convertAndSend(
                    userCreatedExchangeConfig.getExchangeName(), "", userDto);
            log.debug("New user event published successfully");
        } catch (AmqpConnectException e) {
            log.error("❌ Failed to connect to RabbitMQ. The broker might be down.", e);
        } catch (MessageConversionException e) {
            log.error("❌ Error serializing user message: {}", userDto, e);
        } catch (AmqpException e) {
            log.error("❌ General error publishing message to RabbitMQ.", e);
        }
    }
}
