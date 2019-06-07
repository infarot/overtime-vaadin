package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.communication.request.EmployeeRequest;
import com.dawid.overtimevaadin.dto.Overtime;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


import java.util.*;

public class OvertimeForm extends VerticalLayout {
    private Long employeeId;


    public OvertimeForm(Set<Overtime> overtime, Long employeeId) {
        this.employeeId = employeeId;
        Button delete = new Button("Delete this employee");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(this::proceedDelete);
        Icon icon = VaadinIcon.PLUS.create();
        Button add = new Button("", c -> {
            Dialog dialog = new Dialog(new OvertimeAddForm(employeeId));
            dialog.setHeight("400px");
            dialog.setWidth("400px");
            dialog.open();
        });
        List<Overtime> overtimeList = new ArrayList<>(overtime);
        Collections.sort(overtimeList);
        add.setIcon(icon);
        Grid<Overtime> grid = new Grid<>();
        grid.addColumn(Overtime::getFormattedAmount).setHeader("Amount");
        grid.addColumn(Overtime::getOvertimeDate).setHeader("Event date");
        grid.addColumn(Overtime::getPickUpDate).setHeader("Equalization date");
        grid.addColumn(Overtime::getRemarks).setHeader("Remarks");

        grid.setItems(overtimeList);

        grid.addSelectionListener(listener -> listener.getFirstSelectedItem()
                .ifPresent(o -> {
                    Dialog dialog = new Dialog(new OvertimeAddForm(o, employeeId));
                    dialog.setHeight("400px");
                    dialog.setWidth("400px");
                    dialog.open();
                }));

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(add, grid, delete);
    }

    private void proceedDelete(ClickEvent clickEvent) {
        try {
            EmployeeRequest employeeRequest = new EmployeeRequest();
            employeeRequest.deleteEmployee(employeeId);
            getUI().ifPresent(ui -> ui.getPage().reload());
        } catch (Exception e) {
            Notification.show("Unable to delete employee");
        }
    }
}
