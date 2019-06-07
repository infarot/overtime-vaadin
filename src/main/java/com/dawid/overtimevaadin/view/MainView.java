package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.EmployeeRequest;
import com.dawid.overtimevaadin.dto.Employee;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Comparator;
import java.util.List;


@Route("")
public class MainView extends VerticalLayout {
    private Label header = new Label("Employee hour management system");

    public MainView() {

        if (VaadinSession.getCurrent().getAttribute("token") == null) {
            showUIWhenLoggedOut();
        } else {
            try {
                showUIWhenLoggedIn();
            } catch (Exception e){
                Notification.show("Please login first");
                showUIWhenLoggedOut();
            }
        }
    }

    private void showUIWhenLoggedOut() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Label label = new Label("Please login or register to proceed");
        Button register = new Button("Register");
        register.addClickListener(this::proceedRegister);
        Button login = new Button("Login");
        login.addClickListener(this::proceedLogin);
        horizontalLayout.add(register, login);
        add(header, label, horizontalLayout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private void showUIWhenLoggedIn() {
        TreeGrid<Employee> grid = new TreeGrid<>();
        grid.addColumn(Employee::getName).setHeader("Name")
                .setComparator(Comparator.comparing(Employee::getName));
        grid.addColumn(Employee::getLastName).setHeader("Last name")
                .setComparator(Comparator.comparing(Employee::getLastName));
        grid.addColumn(e -> e.getStatistic().getFormattedBalance()).setHeader("Balance")
                .setComparator(Comparator.comparing(e -> e.getStatistic().getBalance()));
        grid.addSelectionListener(selectionEvent -> selectionEvent.getFirstSelectedItem().
                ifPresent(e -> {
                    Dialog dialog = new Dialog(new OvertimeForm(e.getStatistic().getOvertime(), e.getId()));
                    dialog.setWidth("800px");
                    dialog.setHeight("500px");
                    dialog.open();
                }));

        Icon icon = VaadinIcon.PLUS.create();

        Button addEmployee = new Button("", this::proceedEmployeeAdd);
        addEmployee.setIcon(icon);
        Button logout = new Button("Logout", this::proceedLogout);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        grid.setItems(getUserEmployeeList());


        add(header, addEmployee, grid, logout);
    }

    private List<Employee> getUserEmployeeList(){
        EmployeeRequest employeeRequest = new EmployeeRequest();
        return employeeRequest.getAllEmployee();
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
