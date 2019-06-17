package com.dawid.overtimevaadin.communication.request;

import com.dawid.overtimevaadin.communication.constant.OvertimeURI;
import com.dawid.overtimevaadin.dto.Employee;
import com.dawid.overtimevaadin.dto.Overtime;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OvertimeRequest {
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    public OvertimeRequest() {
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    public int addOvertime(Overtime overtime, Long employeeId) {
        httpHeaders.set("Authorization", VaadinSession.getCurrent().getAttribute("token").toString());
        HttpEntity<Overtime> entity = new HttpEntity<>(overtime, httpHeaders);
        ResponseEntity<Object> valid;
        try {
            valid = restTemplate.exchange(
                    OvertimeURI.overtime + employeeId,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valid.getStatusCodeValue();
    }

    public int deleteOvertime(Overtime overtime, Long employeeId) {
        httpHeaders.set("Authorization", VaadinSession.getCurrent().getAttribute("token").toString());
        HttpEntity<Overtime> entity = new HttpEntity<>(overtime, httpHeaders);
        ResponseEntity<Object> valid;
        try {
            valid = restTemplate.exchange(
                    OvertimeURI.overtime + employeeId + "/" + overtime.getId(),
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valid.getStatusCodeValue();
    }
}
