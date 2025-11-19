package com.platform.user_service.restClients.payout.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.ErrorApi;
import com.platform.user_service.dtos.payout.PayoutRequestDto;
import com.platform.user_service.restClients.payout.IPayoutRestClient;
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

import java.util.UUID;

@Service
@Slf4j
@SuppressWarnings("PMD.LooseCoupling")
public class PayoutRestClient implements IPayoutRestClient {
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
     * Constructs a PayoutRestClient with the specified RestTemplate and root URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param rootUrlParam      the base URL for the user service,
     *                          injected from application properties
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     */
    public PayoutRestClient(RestTemplate restTemplateParam,
                             @Value("${pool.payout.url}") String rootUrlParam,
                             ObjectMapper objectMapperParam) {
        this.rootUrl = rootUrlParam;
        this.restTemplate = restTemplateParam;
        this.objectMapper = objectMapperParam;
    }

    /**
     * Retrieves PayoutRequestDto array by NGO ID.
     *
     * @param ngoId The unique identifier of the NGO.
     * @return A ResponseEntity containing the PayoutRequestDtos
     * corresponding to the provided NGO ID.
     */
    @Override
    public ResponseEntity<PayoutRequestDto[]> getPayoutRequestsByNgoId(UUID ngoId) {
        String getUrl = rootUrl + "/api/v1/payouts/by-ngo/{ngoId}";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);
            log.trace("Sending GET request to URL: {}", getUrl);
            return restTemplate.exchange(
                    getUrl,
                    HttpMethod.GET,
                    requestEntity,
                    PayoutRequestDto[].class,
                    ngoId
            );
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP error during get payouts: {}", ex.getMessage());
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
