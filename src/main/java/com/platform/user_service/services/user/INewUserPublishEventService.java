package com.platform.user_service.services.user;

import com.platform.user_service.dtos.common.UserDto;
/**
 * Service interface for publishing events related to new user creation.
 */
public interface INewUserPublishEventService {
    /**
     * Publishes an event when a new user is created.
     *
     * @param userDto the data of the newly created user
     */
    void publishNewUserEvent(UserDto userDto);
}
