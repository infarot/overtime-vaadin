package com.dawid.overtimevaadin.client;

import com.overtime.api.*;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OvertimeClient {

    private static final String TOKEN = "token";

    private final EmployeeControllerApi employeeApi;
    private final AuthControllerApi authApi;

    @Autowired
    public OvertimeClient(@Value("${overtime}") String url, RestTemplate restTemplate) {
        final ApiClient apiClient = new ApiClient(restTemplate).setBasePath(url);
        employeeApi = new EmployeeControllerApi(apiClient);
        authApi = new AuthControllerApi(apiClient);
    }

    public List<EmployeeDto> getAllEmployees() {
        final String token = String.valueOf(VaadinSession.getCurrent().getAttribute(TOKEN));
        return employeeApi.getAllEmployeesForApplicationUserUsingGET(token);
    }

    public Long addEmployee(EmployeeDto employee) {
        final String token = String.valueOf(VaadinSession.getCurrent().getAttribute(TOKEN));
        return this.employeeApi.addNewEmployeeUsingPOST(token, employee);
    }

    public void deleteEmployee(String id) {
        final String token = String.valueOf(VaadinSession.getCurrent().getAttribute(TOKEN));
        employeeApi.deleteEmployeeUsingDELETE(token, id);
    }

    public void addOvertime(String id, OvertimeDto overtime) {
        final String token = String.valueOf(VaadinSession.getCurrent().getAttribute(TOKEN));
        employeeApi.addOvertimeToEmployeeUsingPOST(token, id, overtime);
    }

    public void deleteOvertime(String employeeId, String overtimeId) {
        final String token = String.valueOf(VaadinSession.getCurrent().getAttribute(TOKEN));
        employeeApi.deleteOvertimeFromEmployeeUsingDELETE(token, employeeId, overtimeId);
    }

    public int registerNewUser(ApplicationUserDto user) {
        try {
            authApi.signUpUsingPOST(user);
            return 200;
        } catch (Exception e) {
            return 500;
        }
    }

    public String loginAndGetToken(ApplicationUserDto user) {
        RestTemplate rest = new RestTemplate();
        try {
            ResponseEntity<HttpStatus> entity = rest.postForEntity
                    (authApi.getApiClient().getBasePath() + "/login",
                            user, HttpStatus.class);
            if (entity.getStatusCodeValue() == 200) {
                return entity.getHeaders().getFirst("Authorization");
            }
        } catch (HttpClientErrorException e) {
            log.error("Error during user login: {}", e.getResponseBodyAsString());
        }
        return "";
    }
}
