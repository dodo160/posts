package com.posts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.posts.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Validated
public abstract class ExternalApiAbstractService<T> implements ExternalApiService<T> {

    @Value("${external.api.service.baseurl}")
    protected String baseUrl;

    private final Class<T> type;

    protected ExternalApiAbstractService() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    abstract String getEntityPath();
    abstract String getValidationMsg(Integer id, String url);

    private T getResource(final String url) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, type);
    }

    private List<T> getResources(final String url){
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<T>>() {}).getBody();
    }

    @Override
    public List<T> getAll() {
        final List<T> listResources = getResources(baseUrl + getEntityPath());
        return Objects.nonNull(listResources) ? listResources.stream().map(o -> new ObjectMapper().convertValue(o, type)).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public T getById(final Integer id) {
        final String url = baseUrl + getEntityPath() + "/" + id;
        try {
            return getResource(url);
        } catch (HttpClientErrorException ex) {
            if (HttpStatus.NOT_FOUND.equals(ex.getStatusCode())) {
                throw new NotFoundException(getValidationMsg(id, url));
            }
            throw ex;
        }
    }
}
