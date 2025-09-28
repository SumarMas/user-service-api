package com.platform.user_service.restClients.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.user_service.controllers.manageExceptions.CustomException;
import com.platform.user_service.dtos.common.ErrorApi;
import com.platform.user_service.dtos.common.RegisterAuthDto;
import com.platform.user_service.dtos.response.TokenResponseDto;
import com.platform.user_service.restClients.IAuthRestClient;
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

/**
 * Implementation of IAuthRestClient for interacting with the auth service API.
 */
@Service
@SuppressWarnings("PMD.LooseCoupling")
public class AuthRestClient implements IAuthRestClient {
    /** Logger instance for logging information and errors. */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AuthRestClient.class);
    /** RestTemplate instance for making HTTP requests. */
    private final RestTemplate restTemplate;
    /** ObjectMapper instance for JSON processing. */
    private final ObjectMapper objectMapper;
    /** Base URL for the auth service. */
    private final String baseUrl;

    /**
     * Constructs an AuthRestClient with the specified RestTemplate and base URL.
     *
     * @param restTemplateParam the RestTemplate instance for making HTTP requests
     * @param baseUrlParam      the base URL for the auth service,
     *                          injected from application properties
     * @param objectMapperParam the ObjectMapper instance for JSON processing
     */
    public AuthRestClient(RestTemplate restTemplateParam,
                          @Value("${pool.auth.url}") String baseUrlParam,
                          ObjectMapper objectMapperParam) {
        this.objectMapper = objectMapperParam;
        this.restTemplate = restTemplateParam;
        this.baseUrl = baseUrlParam;
    }

    /**
     * Registers a new user by sending a POST request to the auth service.
     *
     * @param registerAuthDto the registration details
     * @return a ResponseEntity containing the
     * TokenResponseDto with the generated JWT token
     */
    @Override
    public ResponseEntity<TokenResponseDto> registerAuthEntity(RegisterAuthDto registerAuthDto) {
        LOGGER.info("In registerAuthEntity method");
        String postUrl = baseUrl + "/api/v1/auth/register";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<RegisterAuthDto> requestEntity = new HttpEntity<>(registerAuthDto, headers);
            LOGGER.trace("Sending POST request to URL: {}", postUrl);
            return restTemplate.exchange(
                    postUrl,
                    HttpMethod.POST,
                    requestEntity,
                    TokenResponseDto.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            LOGGER.error("HTTP error during registerAuthEntity: {}", ex.getMessage());
            handleError(ex);
            return null;
        }

    }

    private void handleError(HttpStatusCodeException ex) {
        try {
            ErrorApi error = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorApi.class);
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                throw new CustomException(error.getMessage(), HttpStatus.CONFLICT);
            }
            throw new CustomException("Unexpected error from auth-service ", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (JsonProcessingException parseEx) {
            throw new CustomException("Unexpected error from auth-service: " + ex.getMessage(),
                    HttpStatus.valueOf(ex.getStatusCode().value()), parseEx);
        }
    }
}
