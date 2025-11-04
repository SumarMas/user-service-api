package com.platform.user_service.messaging.producer;

import com.platform.user_service.configs.rabbit.exchanges.ExchangeAbstractConfig;
import com.platform.user_service.dtos.common.NgoStatusMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

/**
 * Producer class for publishing NGO status events to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NgoStatusProducer {
    /** RabbitTemplate for sending messages to RabbitMQ. */
    private final RabbitTemplate rabbitTemplate;
    /** Configuration for the NGO status exchange. */
    private final ExchangeAbstractConfig ngoStatusExchangeConfig;
    /**
     * Publishes an NGO status event to the RabbitMQ exchange.
     *
     * @param ngoStatusMessageDto the NGO status message
     *                            DTO containing status details.
     */
    public void publishNgoStatusEvent(NgoStatusMessageDto ngoStatusMessageDto) {
        try {
            log.debug("Entering publishNgoStatusEvent with ngoStatusMessageDto: {}", ngoStatusMessageDto);
            rabbitTemplate.convertAndSend(
                    ngoStatusExchangeConfig.getExchangeName(), "", ngoStatusMessageDto);
            log.debug("Ngo status event published successfully in fanout:{}", ngoStatusExchangeConfig.getExchangeName());
        } catch (AmqpConnectException e) {
            log.error("❌ Failed to connect to RabbitMQ. The broker might be down.", e);
        } catch (MessageConversionException e) {
            log.error("❌ Error serializing ngoStatus message: {}", ngoStatusMessageDto, e);
        } catch (AmqpException e) {
            log.error("❌ General error publishing message to RabbitMQ.", e);
        }
    }
}
