package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.RegistrationRequest;
import com.dawid.overtimevaadin.dto.User;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Route("sign-up")
public class RegistrationForm extends HorizontalLayout {
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button register = new Button("Register");
    private Button back = new Button("Back");
    private Binder<User> binder = new Binder<>(User.class);


    public RegistrationForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addClickListener(this::proceedRegistration);
        register.addClickShortcut(Key.ENTER);
        back.addClickListener(this::proceedBack);


        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(register, back);
        verticalLayout.add(username, password, buttons);
        add(verticalLayout);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);


        User user = new User();
        binder.bindInstanceFields(this);
        binder.readBean(user);
        binder.setBean(user);


    }

    private void proceedRegistration(ClickEvent event) {
        try {
            User user = binder.getBean();
            RegistrationRequest registrationRequest = new RegistrationRequest();
            int statusCode = registrationRequest.register(user);
            if (statusCode == 200) {
                getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
            }
        } catch (Exception e) {
            Notification.show("Please provide correct user credentials");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
    }
}
