package com.dawid.overtimevaadin.communication.request;

import com.dawid.overtimevaadin.communication.constant.OvertimeURI;
import com.dawid.overtimevaadin.dto.Employee;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class EmployeeRequest {

    public List<Employee> getAllEmployee() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", VaadinSession.getCurrent().getAttribute("token").toString());
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<List<Employee>> employees;
        try {
            employees = restTemplate.exchange(
                    OvertimeURI.employee,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return employees.getBody();
    }

    public Integer addEmployee(Employee employee) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", VaadinSession.getCurrent().getAttribute("token").toString());
        HttpEntity<Employee> entity = new HttpEntity<>(employee, httpHeaders);
        ResponseEntity<Object> valid;
        try {
            valid = restTemplate.exchange(
                    OvertimeURI.employee,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valid.getStatusCodeValue();
    }

    public Integer deleteEmployee(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", VaadinSession.getCurrent().getAttribute("token").toString());
        HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<Object> valid;
        try {
            valid = restTemplate.exchange(
                    OvertimeURI.employee + "/" + id,
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
