package com.dawid.overtimevaadin.view;

import com.dawid.overtimevaadin.dto.Overtime;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.*;

public class OvertimeForm extends VerticalLayout {


    public OvertimeForm(Set<Overtime> overtime, Long employeeId) {
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
        add(add, grid);
    }
}
