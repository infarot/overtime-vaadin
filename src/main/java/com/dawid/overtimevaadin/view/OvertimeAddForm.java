package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.client.OvertimeClient;
import com.overtime.api.OvertimeDto;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

class OvertimeAddForm extends VerticalLayout {

    private final OvertimeClient overtimeClient;

    private Binder<OvertimeDto> binder = new Binder<>(OvertimeDto.class);
    private Long employeeId;
    private TextField amount = new TextField("Amount");
    private DatePicker overtimeDate = new DatePicker("Event date");
    private DatePicker pickUpDate = new DatePicker("Equalization date");
    private TextArea remarks = new TextArea("Remarks");
    private Button save = new Button("Save", this::proceedAdd);

    OvertimeAddForm(Long employeeId, OvertimeClient overtimeClient) {
        this.overtimeClient = overtimeClient;
        this.employeeId = employeeId;

        OvertimeDto overtime = new OvertimeDto();
        binder.bind(amount, OvertimeDto::getAmount, OvertimeDto::setAmount);
        binder.bind(overtimeDate, this::overtimeDateFromString, this::overtimeDateToString);
        binder.bind(pickUpDate, this::pickUpDateFromString, this::pickUpDateToString);
        binder.bind(remarks, OvertimeDto::getRemarks, OvertimeDto::setRemarks);

        binder.readBean(overtime);
        binder.setBean(overtime);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(amount, overtimeDate, pickUpDate, remarks, save);
    }

    OvertimeAddForm(OvertimeDto overtime, Long employeeId, OvertimeClient overtimeClient) {
        this.overtimeClient = overtimeClient;
        overtime.setAmount(overtime.getAmount().substring(2));
        this.employeeId = employeeId;
        binder.bind(amount, OvertimeDto::getAmount, OvertimeDto::setAmount);
        binder.bind(overtimeDate, this::overtimeDateFromString, this::overtimeDateToString);
        binder.bind(pickUpDate, this::pickUpDateFromString, this::pickUpDateToString);
        binder.bind(remarks, OvertimeDto::getRemarks, OvertimeDto::setRemarks);
        binder.readBean(overtime);
        binder.setBean(overtime);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        Button deleteOvertime = new Button("Delete", this::proceedDeleteOvertime);
        add(amount, overtimeDate, pickUpDate, remarks, save, deleteOvertime);
    }

    private void proceedAdd(ClickEvent clickEvent) {
        OvertimeDto overtime = binder.getBean();
        if (overtime.getOvertimeDate() == null) {
            Notification.show("Please provide date");
        } else {
            try {
                if (overtime.getAmount().startsWith("-")) {
                    Duration duration = Duration.parse("-PT" + overtime.getAmount().substring(1));
                    overtime.setAmount(String.valueOf(duration));
                    overtimeClient.addOvertime(String.valueOf(employeeId), overtime);
                    getUI().ifPresent(ui -> ui.getPage().reload());
                } else {
                    Duration duration = Duration.parse("PT" + overtime.getAmount());
                    overtime.setAmount(String.valueOf(duration));
                    overtimeClient.addOvertime(String.valueOf(employeeId), overtime);
                    getUI().ifPresent(ui -> ui.getPage().reload());
                }
            } catch (DateTimeParseException e) {
                Notification.show("Please provide correct amount");
            } catch (Exception e) {
                Notification.show("Invalid request or server is offline");
            }
        }
    }

    private void proceedDeleteOvertime(ClickEvent clickEvent) {
        OvertimeDto overtime = binder.getBean();
        try {
            overtimeClient.deleteOvertime(String.valueOf(overtime.getId()), String.valueOf(employeeId));
            getUI().ifPresent(ui -> ui.getPage().reload());
        } catch (Exception e) {
            Notification.show("Unable to delete overtime");
        }
    }

    private LocalDate overtimeDateFromString(OvertimeDto overtimeDate) {
        return Objects.isNull(overtimeDate.getOvertimeDate()) ? null :
                LocalDate.parse(overtimeDate.getOvertimeDate());
    }

    private void overtimeDateToString(OvertimeDto overtimeDate, LocalDate date) {
        if (overtimeDate != null)
            overtimeDate.setOvertimeDate(String.valueOf(date));
    }

    private LocalDate pickUpDateFromString(OvertimeDto overtimeDate) {
        return Objects.isNull(overtimeDate.getPickUpDate()) ? null :
                LocalDate.parse(overtimeDate.getPickUpDate());
    }

    private void pickUpDateToString(OvertimeDto overtimeDate, LocalDate date) {
        if (overtimeDate != null)
            overtimeDate.setPickUpDate(String.valueOf(date));
    }
}
