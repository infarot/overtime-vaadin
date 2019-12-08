package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.client.OvertimeClient;
import com.overtime.api.OvertimeDto;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.*;

class OvertimeForm extends VerticalLayout {

    private final OvertimeClient overtimeClient;

    private Long employeeId;

    OvertimeForm(List<OvertimeDto> overtime, Long employeeId, OvertimeClient overtimeClient) {
        this.employeeId = employeeId;
        this.overtimeClient = overtimeClient;

        Label ctrlInfo = new Label("Hold ctrl to proceed");
        Button delete = new Button("Delete this employee");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickListener(this::proceedDelete);
        Icon icon = VaadinIcon.PLUS.create();
        Button add = new Button("", c -> {
            Dialog dialog = new Dialog(new OvertimeAddForm(employeeId, overtimeClient));
            dialog.setHeight("400px");
            dialog.setWidth("400px");
            dialog.open();
        });
        List<OvertimeDto> overtimeList = new ArrayList<>(overtime);
        overtimeList.sort(Comparator.comparing(OvertimeDto::getOvertimeDate));
        add.setIcon(icon);
        Grid<OvertimeDto> grid = new Grid<>();
        grid.addColumn(OvertimeDto::getAmount).setHeader("Amount");
        grid.addColumn(OvertimeDto::getOvertimeDate).setHeader("Event date");
        grid.addColumn(OvertimeDto::getPickUpDate).setHeader("Equalization date");
        grid.addColumn(OvertimeDto::getRemarks).setHeader("Remarks");

        grid.setItems(overtimeList);

        grid.addSelectionListener(listener -> listener.getFirstSelectedItem()
                .ifPresent(o -> {
                    Dialog dialog = new Dialog(new OvertimeAddForm(o, employeeId, overtimeClient));
                    dialog.setHeight("400px");
                    dialog.setWidth("400px");
                    dialog.open();
                }));

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(add, grid, ctrlInfo, delete);
    }

    private void proceedDelete(ClickEvent clickEvent) {
        try {
            if (clickEvent.isCtrlKey()) {
                overtimeClient.deleteEmployee(String.valueOf(employeeId));
                getUI().ifPresent(ui -> ui.getPage().reload());
            }
        } catch (Exception e) {
            Notification.show("Unable to delete employee");
        }
    }
}
