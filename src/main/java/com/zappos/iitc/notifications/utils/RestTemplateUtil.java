package com.zappos.iitc.notifications.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zappos.iitc.notifications.constants.AppConstants.LIMIT;
import static com.zappos.iitc.notifications.constants.AppConstants.OFFSET;

@Component
public class RestTemplateUtil {

    @Autowired
    Environment environment;

    @Autowired
    RestTemplate restTemplate;

    public <T> ResponseEntity<T> getPagedApiResponse(String uri_, String path_suffix, Map<String, Object> queryParams, Pageable pageable, Class type){
        String uri = environment.getProperty(uri_) + path_suffix;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        if(queryParams != null){
            queryParams.forEach((key,val)-> {
                builder.queryParam(key, val);
            });
        }

        builder.queryParam(OFFSET, pageable.getOffset());
        builder.queryParam(LIMIT, pageable.getPageSize());
        try {
            List<T> list = new ArrayList<>();
            ResponseEntity<T> res = (ResponseEntity<T>)restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null,type);
            return res;
        } catch (HttpClientErrorException e) {

            e.printStackTrace();
        }
        return null;
    }

    public <T> T getApiResponse(String uri_, String path_suffix, Map<String, Object> queryParams, Class type){
        String uri = environment.getProperty(uri_) + path_suffix;

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        if(queryParams != null){
            queryParams.forEach((key,val)-> {
                builder.queryParam(key, val);
            });
        }

        try {
           // T response = new ArrayList<>();
            ResponseEntity<T> res = (ResponseEntity<T>)restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    null,type);
            T response = res.getBody();
            return response;
        } catch (HttpClientErrorException e) {

            e.printStackTrace();
        }
        return null;
    }

}
