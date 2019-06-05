package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.EmployeeRequest;
import com.dawid.overtimevaadin.dto.Employee;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Comparator;


@Route("")
public class MainView extends VerticalLayout {


    public MainView() {
        Label header = new Label("Employee hour management system");
        if (VaadinSession.getCurrent().getAttribute("token") == null) {
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Label label = new Label("Please login or register to proceed");
            Button register = new Button("Register");
            register.addClickListener(this::proceedRegister);
            Button login = new Button("Login");
            login.addClickListener(this::proceedLogin);

            horizontalLayout.add(register, login);
            add(header, label, horizontalLayout);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        } else {
            EmployeeRequest employeeRequest = new EmployeeRequest();
            Grid<Employee> grid = new Grid<>();
            grid.addColumn(Employee::getId).setHeader("Id").setComparator(Comparator.comparingLong(Employee::getId));
            grid.addColumn(Employee::getName).setHeader("Name");
            grid.addColumn(Employee::getLastName).setHeader("Last Name");
            grid.addColumn(e -> e.getStatistic().getBalance()).setHeader("Balance");


            Icon icon = VaadinIcon.PLUS.create();

            Button addEmployee = new Button("", this::proceedEmployeeAdd);
            addEmployee.setIcon(icon);
            Button logout = new Button("Logout", this::proceedLogout);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            grid.setItems(employeeRequest.getAllEmployee());


            add(header, addEmployee, grid, logout);
        }
    }

    private void proceedLogin(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/login'"));
    }

    private void proceedRegister(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/sign-up'"));
    }

    private void proceedLogout(ClickEvent event) {
        VaadinSession.getCurrent().setAttribute("token", null);
        getUI().ifPresent((ui -> ui.getPage().reload()));
    }

    private void proceedEmployeeAdd(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/add-employee'"));
    }
}
