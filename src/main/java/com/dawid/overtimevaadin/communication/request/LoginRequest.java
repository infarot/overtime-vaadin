package com.dawid.overtimevaadin.communication.request;

import com.dawid.overtimevaadin.communication.constant.OvertimeURI;
import com.dawid.overtimevaadin.dto.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class LoginRequest {

    public String loginAndGetToken(User user) {
        RestTemplate rest = new RestTemplate();
        try {
            ResponseEntity<HttpStatus> entity = rest.postForEntity(OvertimeURI.login, user, HttpStatus.class);
            if (entity.getStatusCodeValue() == 200) {
                return entity.getHeaders().getFirst("Authorization");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }

}
