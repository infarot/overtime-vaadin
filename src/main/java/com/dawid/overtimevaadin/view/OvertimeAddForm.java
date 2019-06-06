package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.OvertimeRequest;
import com.dawid.overtimevaadin.dto.Overtime;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.time.Duration;
import java.time.format.DateTimeParseException;


public class OvertimeAddForm extends VerticalLayout {
    private Binder<Overtime> binder = new Binder<>(Overtime.class);
    private Long employeeId;
    private TextField amount = new TextField("Amount");
    private DatePicker overtimeDate = new DatePicker("Event date");
    private DatePicker pickUpDate = new DatePicker("Equalization date");
    private TextArea remarks = new TextArea("Remarks");
    private Button save = new Button("Save", this::proceedAdd);


    public OvertimeAddForm(Long employeeId) {
        this.employeeId = employeeId;


        Overtime overtime = new Overtime();
        binder.bind(amount, Overtime::getStringAmount, Overtime::setStringAmount);
        binder.bind(overtimeDate, Overtime::getOvertimeDate, Overtime::setOvertimeDate);
        binder.bind(pickUpDate, Overtime::getPickUpDate, Overtime::setPickUpDate);
        binder.bind(remarks, Overtime::getRemarks, Overtime::setRemarks);

        binder.readBean(overtime);
        binder.setBean(overtime);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(amount, overtimeDate, pickUpDate, remarks, save);
    }

    public OvertimeAddForm(Overtime overtime, Long employeeId) {
        overtime.setStringAmount(overtime.getAmount().toString().substring(2));
        this.employeeId = employeeId;
        binder.bind(amount, Overtime::getStringAmount, Overtime::setStringAmount);
        binder.bind(overtimeDate, Overtime::getOvertimeDate, Overtime::setOvertimeDate);
        binder.bind(pickUpDate, Overtime::getPickUpDate, Overtime::setPickUpDate);
        binder.bind(remarks, Overtime::getRemarks, Overtime::setRemarks);
        binder.readBean(overtime);
        binder.setBean(overtime);

        overtimeDate.setEnabled(false);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(amount, overtimeDate, pickUpDate, remarks, save);
    }

    private void proceedAdd(ClickEvent clickEvent) {
        Overtime overtime = binder.getBean();
        if (overtime.getOvertimeDate() == null) {
            Notification.show("Please provide date");
        } else {
            try {
                if (overtime.getStringAmount().startsWith("-")) {
                    Duration duration = Duration.parse("-PT" + overtime.getStringAmount().substring(1));
                    overtime.setAmount(duration);
                    OvertimeRequest request = new OvertimeRequest();
                    request.addOvertime(overtime, employeeId);
                    getUI().ifPresent(ui -> ui.getPage().reload());
                } else {
                    Duration duration = Duration.parse("PT" + overtime.getStringAmount());
                    overtime.setAmount(duration);
                    OvertimeRequest request = new OvertimeRequest();
                    request.addOvertime(overtime, employeeId);
                    getUI().ifPresent(ui -> ui.getPage().reload());
                }
            } catch (DateTimeParseException e) {
                Notification.show("Please provide correct amount");
            } catch (Exception e) {
                Notification.show("Invalid request or server is offline");
            }
        }
    }
}
