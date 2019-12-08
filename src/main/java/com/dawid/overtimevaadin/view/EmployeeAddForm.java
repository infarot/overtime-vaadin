package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.client.OvertimeClient;
import com.overtime.api.EmployeeDto;
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

    private final OvertimeClient overtimeClient;

    private Binder<EmployeeDto> binder = new Binder<>(EmployeeDto.class);

    public EmployeeAddForm(OvertimeClient overtimeClient) {
        this.overtimeClient = overtimeClient;
        if (VaadinSession.getCurrent().getAttribute("token") != null) {
            VerticalLayout verticalLayout = new VerticalLayout();
            Button add = new Button("Add");
            add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            add.addClickListener(this::proceedEmployeeAdd);
            add.addClickShortcut(Key.ENTER);
            Button back = new Button("Back");
            back.addClickListener(this::proceedBack);

            verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
            HorizontalLayout buttons = new HorizontalLayout();
            buttons.add(add, back);
            TextField lastName = new TextField("Last name");
            TextField name = new TextField("Name");
            verticalLayout.add(name, lastName, buttons);
            add(verticalLayout);
            setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

            EmployeeDto employee = new EmployeeDto();
            binder.forField(name).bind(EmployeeDto::getName, EmployeeDto::setName);
            binder.forField(lastName).bind(EmployeeDto::getLastName, EmployeeDto::setLastName);
            binder.readBean(employee);
            binder.setBean(employee);
        } else {
            add(new MainView(overtimeClient));
        }
    }

    private void proceedEmployeeAdd(ClickEvent event) {
        EmployeeDto employee = binder.getBean();
        try {
            overtimeClient.addEmployee(employee);
            getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
        } catch (Exception e) {
            Notification.show("Please provide valid employee credentials");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
    }
}
