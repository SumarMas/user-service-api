package com.platform.user_service.services.Ngo.Impl;

import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.NgoStatusMessageDto;
import com.platform.user_service.dtos.common.UserDto;
import com.platform.user_service.enums.NgoStatus;
import com.platform.user_service.messaging.producer.NgoStatusProducer;
import com.platform.user_service.services.Ngo.INgoStatusPublishEventService;
import com.platform.user_service.services.user.IGetUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
/**
 * Service implementation for publishing NGO status events.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NgoStatusPublishEventService implements INgoStatusPublishEventService {
    /** Producer for publishing NGO status events to RabbitMQ. */
    private final NgoStatusProducer ngoStatusProducer;
    /** Service for retrieving user information. */
    private final IGetUserService getUserService;
    /**
     * Publishes an NGO status event.
     *
     * @param userId           The UUID of the user associated with the NGO.
     * @param ngoName          The name of the NGO.
     * @param ngoStatus        The status of the NGO.
     * @param ngoStatusMessage Additional message related to the NGO status.
     */
    @Override
    public void publishNgoStatusEvent(UUID userId, String ngoName, NgoStatus ngoStatus, String ngoStatusMessage) {
        try {
            UserDto userDto = getUserDto(userId);
            log.trace("Publishing NGO status event for userId={}, ngoName={}, ngoStatus={}, ngoStatusMessage={}",
                    userId, ngoName, ngoStatus, ngoStatusMessage);
            NgoStatusMessageDto messageDto = NgoStatusMessageDto.builder()
                    .user(userDto)
                    .nameNgo(ngoName)
                    .status(ngoStatus)
                    .message(ngoStatusMessage)
                    .build();
            ngoStatusProducer.publishNgoStatusEvent(messageDto);
        } catch (CustomException ex) {
            log.error("Error occurred when try to send notification: {}", ex.getMessage(), ex);
        }
    }

    private UserDto getUserDto(UUID userId) {
        return getUserService.getUserById(userId);
    }
}
