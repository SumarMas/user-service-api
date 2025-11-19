package com.platform.user_service.restClients.donation.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.ErrorApi;
import com.platform.user_service.dtos.donation.DonationsDto;
import com.platform.user_service.enums.DonationStatus;
import com.platform.user_service.restClients.donation.IDonationRestClient;
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
import java.util.Set;
import java.util.UUID;
/**
 * Implementation of the IDonationRestClient interface for
 * interacting with the donation service.
 */
@Service
@Slf4j
@SuppressWarnings("PMD.LooseCoupling")
public class DonationRestClient implements IDonationRestClient {
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
     * Constructs a DonationRestClient with the specified RestTemplate and root URL.
     *
     * @param restTemplateParam    the RestTemplate instance for making HTTP requests
     * @param rootUrlParam         the base URL for the user service,
     *                             injected from application properties
     * @param objectMapperParam    the ObjectMapper instance for JSON processing
     */
    public DonationRestClient(RestTemplate restTemplateParam,
                              @Value("${pool.donation.url}") String rootUrlParam,
                              ObjectMapper objectMapperParam) {
        this.rootUrl = rootUrlParam;
        this.restTemplate = restTemplateParam;
        this.objectMapper = objectMapperParam;
    }

    /**
     * Retrieves donations by campaign IDs and status.
     *
     * @param status      the status of the donations to filter by
     * @param campaignIds the set of campaign IDs to retrieve donations for
     * @return a ResponseEntity containing a DonationsDto with the filtered donations
     */
    @Override
    public ResponseEntity<DonationsDto> getDonantionsByCampaignsIdAndStatus(Set<DonationStatus> status,
                                                                            Set<UUID> campaignIds) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(rootUrl)
                    .path("/api/v1/donations");

            // Solo agregar si hay datos
            if (status != null && !status.isEmpty()) {
                status.forEach(st -> builder.queryParam("status", st));
            }

            if (campaignIds != null && !campaignIds.isEmpty()) {
                campaignIds.forEach(id -> builder.queryParam("campaign", id));
            }

            URI uri = builder.build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

            log.trace("Sending GET request to URL: {}", uri);

            return restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    DonationsDto.class
            );

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get data donations: {}", ex.getMessage());
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
            throw new CustomException("Unexpected error from donation-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from donation-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
