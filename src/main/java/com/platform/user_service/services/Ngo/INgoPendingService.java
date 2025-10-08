package com.platform.user_service.services.Ngo;

import com.platform.user_service.dtos.common.NgoDto;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface INgoPendingService {
    /**
     * Retrieves a list of pending validation NGOs.
     *
     * @return a list of NgoPendingDto representing the pending NGOs
     */
    List<NgoDto> getPending();
}
