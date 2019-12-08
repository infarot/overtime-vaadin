package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.client.OvertimeClient;
import com.overtime.api.ApplicationUserDto;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Route("sign-up")
public class RegistrationForm extends HorizontalLayout {

    private final OvertimeClient overtimeClient;

    private Binder<ApplicationUserDto> binder = new Binder<>(ApplicationUserDto.class);

    public RegistrationForm(OvertimeClient overtimeClient) {
        this.overtimeClient = overtimeClient;

        VerticalLayout verticalLayout = new VerticalLayout();
        Button register = new Button("Register");
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickListener(this::proceedRegistration);
        register.addClickShortcut(Key.ENTER);
        Button back = new Button("Back");
        back.addClickListener(this::proceedBack);

        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(register, back);
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        EmailField email = new EmailField("Email");
        verticalLayout.add(email, username, password, buttons);
        add(verticalLayout);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ApplicationUserDto user = new ApplicationUserDto();
        binder.forField(username).bind(ApplicationUserDto::getUsername, ApplicationUserDto::setUsername);
        binder.forField(password).bind(ApplicationUserDto::getPassword, ApplicationUserDto::setPassword);
        binder.forField(email).bind(ApplicationUserDto::getEmail, ApplicationUserDto::setEmail);
        binder.readBean(user);
        binder.setBean(user);
    }

    private void proceedRegistration(ClickEvent event) {
        try {
            ApplicationUserDto user = binder.getBean();
            int statusCode = overtimeClient.registerNewUser(user);
            if (statusCode == 200) {
                getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
            }
        } catch (Exception e) {
            Notification.show("Please provide correct user credentials");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
    }
}
