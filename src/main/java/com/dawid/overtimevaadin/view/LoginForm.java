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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;

@Route("login")
@Slf4j
public class LoginForm extends HorizontalLayout {

    private final OvertimeClient overtimeClient;

    private Binder<ApplicationUserDto> binder = new Binder<>(ApplicationUserDto.class);

    public LoginForm(OvertimeClient overtimeClient) {
        this.overtimeClient = overtimeClient;

        VerticalLayout verticalLayout = new VerticalLayout();
        Button login = new Button("Login");
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        login.addClickListener(this::proceedLogin);
        login.addClickShortcut(Key.ENTER);
        Button back = new Button("Back");
        back.addClickListener(this::proceedBack);

        verticalLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(login, back);
        TextField username = new TextField("Username");
        PasswordField password = new PasswordField("Password");
        verticalLayout.add(username, password, buttons);
        add(verticalLayout);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        ApplicationUserDto user = new ApplicationUserDto();
        binder.forField(username).bind(ApplicationUserDto::getUsername, ApplicationUserDto::setUsername);
        binder.forField(password).bind(ApplicationUserDto::getPassword, ApplicationUserDto::setPassword);
        binder.readBean(user);
        binder.setBean(user);
    }

    private void proceedLogin(ClickEvent event) {
        try {
            ApplicationUserDto user = binder.getBean();
            String token = overtimeClient.loginAndGetToken(user);
            if (!token.isEmpty()) {
                VaadinSession session = VaadinSession.getCurrent();
                session.setAttribute("token", token);
                getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            Notification.show("Login failed - please check username or password");
        }
    }

    private void proceedBack(ClickEvent event) {
        getUI().ifPresent(ui -> ui.getPage().executeJs("window.location.href = '/'"));
    }
}
