package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.LoginRequest;

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
import com.vaadin.flow.server.VaadinSession;

@Route("login")
public class LoginForm extends HorizontalLayout {
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button login = new Button("Login");
    private Button back = new Button("Back");
    private Binder<User> binder = new Binder<>(User.class);


    public LoginForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        login.addClickListener(this::proceedLogin);
        login.addClickShortcut(Key.ENTER);
        back.addClickListener(this::proceedBack);


        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(login, back);
        verticalLayout.add(username, password, buttons);
        add(verticalLayout);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);


        User user = new User();
        binder.bindInstanceFields(this);
        binder.readBean(user);
        binder.setBean(user);


    }

    private void proceedLogin(ClickEvent event) {
        try {
            User user = binder.getBean();
            LoginRequest request = new LoginRequest();
            String token = request.loginAndGetToken(user);
            if (!token.isEmpty()) {
                VaadinSession session = VaadinSession.getCurrent();
                session.setAttribute("token", token);
                getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
            }

        } catch (Exception e) {
            Notification.show("Login failed - please check username or password");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent((ui) -> ui.getPage().executeJavaScript("window.location.href = '/'"));
    }
}
