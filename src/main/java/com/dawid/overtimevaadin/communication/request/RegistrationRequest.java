package com.dawid.overtimevaadin.communication.request;

import com.dawid.overtimevaadin.communication.constant.OvertimeURI;
import com.dawid.overtimevaadin.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class RegistrationRequest {

    public Integer register(User user) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<HttpStatus> entity;
        try {
            entity = template.postForEntity(OvertimeURI.register, user, HttpStatus.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return entity.getStatusCodeValue();
    }
}
