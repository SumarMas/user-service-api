package com.platform.user_service.restClients.campaign.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.campaign.CampaignDto;
import com.platform.user_service.dtos.common.ErrorApi;
import com.platform.user_service.enums.CampaignState;
import com.platform.user_service.restClients.campaign.ICampaignRestClient;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the ICampaignRestClient interface
 * for interacting with the Campaign service.
 */
@Service
@Slf4j
@SuppressWarnings("PMD.LooseCoupling")
public class CampaignRestClient implements ICampaignRestClient {
    /**
     * RestTemplate instance for making HTTP requests.
     */
    private final RestTemplate restTemplate;
    /**
     * ObjectMapper instance for JSON processing.
     */
    private final ObjectMapper objectMapper;
    /**
     * Base URL for the user service.
     */
    private final String rootUrl;

    /**
     * Constructs a NgoRestClient with the specified RestTemplate and root URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param rootUrlParam      the base URL for the user service,
     *                          injected from application properties
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     */
    public CampaignRestClient(RestTemplate restTemplateParam,
                              @Value("${pool.campaign.url}") String rootUrlParam,
                              ObjectMapper objectMapperParam) {
        this.rootUrl = rootUrlParam;
        this.restTemplate = restTemplateParam;
        this.objectMapper = objectMapperParam;
    }

    /**
     * Retrieves a CampaignDto by its state and associated NGO ID.
     *
     * @param state The state of the campaign.
     * @param ngoId The unique identifier of the associated NGO.
     * @return A ResponseEntity containing the CampaignDto
     * corresponding to the provided state and NGO ID.
     */
    @Override
    public ResponseEntity<CampaignDto[]> getCampaignsByStateAndNgoId(CampaignState state, UUID ngoId) {
        try {
            URI uri = UriComponentsBuilder
                    .fromUriString(rootUrl)
                    .path("/api/v1/campaigns/filter")
                    .queryParamIfPresent("state", Optional.ofNullable(state))
                    .queryParamIfPresent("ngoId", Optional.ofNullable(ngoId))
                    .build()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

            log.trace("Sending GET request to URL: {}", uri);

            return restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    CampaignDto[].class
            );

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get data campaigns: {}", ex.getMessage());
            handleError(ex);
            return null;
        }
    }

    private void handleError(HttpStatusCodeException ex) {
        try {
            ErrorApi error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorApi.class);
            if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CustomException(error.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CustomException(error.getMessage(), HttpStatus.NOT_FOUND);
            }
            throw new CustomException("Unexpected error from campaign-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from campaign-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
