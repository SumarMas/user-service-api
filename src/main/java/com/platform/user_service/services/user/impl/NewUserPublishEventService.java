package com.platform.user_service.services.user.impl;

import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.messaging.producer.NewUserProducer;
import com.platform.user_service.services.user.INewUserPublishEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for publishing new user events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NewUserPublishEventService implements INewUserPublishEventService {
    /** Producer for publishing new user events to RabbitMQ. */
    private final NewUserProducer newUserProducer;
    /**
     * Publishes an event when a new user is created.
     *
     * @param userDto the data of the newly created user
     */
    @Override
    public void publishNewUserEvent(UserDto userDto) {
        log.trace("Publishing event for new user ID: {}", userDto.getId());
        newUserProducer.publishNewUserEvent(userDto);
    }
}
