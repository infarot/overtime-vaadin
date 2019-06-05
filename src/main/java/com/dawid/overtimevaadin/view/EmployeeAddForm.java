package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.EmployeeRequest;
import com.dawid.overtimevaadin.dto.Employee;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("add-employee")
public class EmployeeAddForm extends HorizontalLayout {
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private Button add = new Button("Add");
    private Button back = new Button("Back");
    private Binder<Employee> binder = new Binder<>(Employee.class);


    public EmployeeAddForm() {
        if (VaadinSession.getCurrent().getAttribute("token") != null) {
            VerticalLayout verticalLayout = new VerticalLayout();
            add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            add.addClickListener(this::proceedEmployeeAdd);
            add.addClickShortcut(Key.ENTER);
            back.addClickListener(this::proceedBack);


            verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            HorizontalLayout buttons = new HorizontalLayout();
            buttons.add(add, back);
            verticalLayout.add(name, lastName, buttons);
            add(verticalLayout);
            setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);


            Employee employee = new Employee();
            binder.bindInstanceFields(this);
            binder.readBean(employee);
            binder.setBean(employee);
        } else {
            add(new MainView());
        }

    }

    private void proceedEmployeeAdd(ClickEvent event) {
        Employee employee = binder.getBean();
        try {
            EmployeeRequest employeeRequest = new EmployeeRequest();
            employeeRequest.addEmployee(employee);
            getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
        } catch (Exception e) {
            Notification.show("Please provide valid employee credentials");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
    }

}
