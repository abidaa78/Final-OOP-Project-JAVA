package gui;

import entities.*;
import manager.PetCareManager;
import storage.DataStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {

    private PetCareManager manager;

    // PET UI
    private DefaultTableModel petModel;
    private JTable petTable;

    // VET UI
    private DefaultTableModel vetModel;
    private JTable vetTable;

    // APPOINTMENT UI
    private DefaultTableModel apptModel;
    private JTable apptTable;

    public MainFrame() {
        // Load data
        Object data = DataStorage.load();
        manager = (data instanceof PetCareManager) ? (PetCareManager) data : new PetCareManager();

        setTitle("FluffyCare - Pet Care Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Pets", buildPetPanel());
        tabs.add("Vets", buildVetPanel());
        tabs.add("Appointments", buildApptPanel());
        add(tabs);

        refreshAll();
    }

    // ------------ PETS ---------------
    private JPanel buildPetPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        petModel = new DefaultTableModel(new String[]{"Name", "Species", "Age", "Owner"}, 0);
        petTable = new JTable(petModel);
        panel.add(new JScrollPane(petTable), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Update");
        JButton del = new JButton("Delete");
        controls.add(add); controls.add(edit); controls.add(del);
        panel.add(controls, BorderLayout.NORTH);

        add.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField species = new JTextField();
            JTextField age = new JTextField();
            JTextField owner = new JTextField();
            Object[] fields = {
                    "Name:", name,
                    "Species:", species,
                    "Age:", age,
                    "Owner Name:", owner
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Add Pet", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    int a = Integer.parseInt(age.getText().trim());
                    manager.addPet(new Pet(name.getText().trim(), species.getText().trim(), a, owner.getText().trim()));
                    persist(); refreshPets();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be an integer.");
                }
            }
        });

        edit.addActionListener(e -> {
            int row = petTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a pet to update."); return; }
            Pet existing = manager.getPets().get(row);

            JTextField name = new JTextField(existing.getName());
            JTextField species = new JTextField(existing.getSpecies());
            JTextField age = new JTextField(String.valueOf(existing.getAge()));
            JTextField owner = new JTextField(existing.getOwnerName());
            Object[] fields = {
                    "Name:", name,
                    "Species:", species,
                    "Age:", age,
                    "Owner Name:", owner
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Update Pet", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    int a = Integer.parseInt(age.getText().trim());
                    Pet updated = new Pet(name.getText().trim(), species.getText().trim(), a, owner.getText().trim());
                    // preserve id by replacing fields instead of new object? For simplicity we replace list element.
                    manager.updatePet(row, updated);
                    persist(); refreshPets(); refreshAppointments(); // appointments show pet name/species
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Age must be an integer.");
                }
            }
        });

        del.addActionListener(e -> {
            int row = petTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a pet to delete."); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Deleting a pet will not automatically remove appointments.\nProceed?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deletePet(row);
                persist(); refreshAll();
            }
        });

        return panel;
    }

    // ------------ VETS ---------------
    private JPanel buildVetPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        vetModel = new DefaultTableModel(new String[]{"Name", "Specialization"}, 0);
        vetTable = new JTable(vetModel);
        panel.add(new JScrollPane(vetTable), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Update");
        JButton del = new JButton("Delete");
        controls.add(add); controls.add(edit); controls.add(del);
        panel.add(controls, BorderLayout.NORTH);

        add.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField spec = new JTextField();
            Object[] fields = {
                    "Name:", name,
                    "Specialization:", spec
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Add Vet", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                manager.addVet(new Vet(name.getText().trim(), spec.getText().trim()));
                persist(); refreshVets();
            }
        });

        edit.addActionListener(e -> {
            int row = vetTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a vet to update."); return; }
            Vet existing = manager.getVets().get(row);

            JTextField name = new JTextField(existing.getName());
            JTextField spec = new JTextField(existing.getSpecialization());
            Object[] fields = {
                    "Name:", name,
                    "Specialization:", spec
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Update Vet", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                manager.updateVet(row, new Vet(name.getText().trim(), spec.getText().trim()));
                persist(); refreshVets(); refreshAppointments(); // appointments show vet name/spec
            }
        });

        del.addActionListener(e -> {
            int row = vetTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a vet to delete."); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Deleting a vet will not automatically remove appointments.\nProceed?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteVet(row);
                persist(); refreshAll();
            }
        });

        return panel;
    }

    // ----------- APPOINTMENTS ------------
    private JPanel buildApptPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        apptModel = new DefaultTableModel(new String[]{"Pet", "Vet", "Date (YYYY-MM-DD)", "Service", "Notes"}, 0);
        apptTable = new JTable(apptModel);
        panel.add(new JScrollPane(apptTable), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton add = new JButton("Add");
        JButton edit = new JButton("Update");
        JButton del = new JButton("Delete");
        controls.add(add); controls.add(edit); controls.add(del);
        panel.add(controls, BorderLayout.NORTH);

        add.addActionListener(e -> {
            if (manager.getPets().isEmpty() || manager.getVets().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add at least one Pet and one Vet first.");
                return;
            }
            Pet pet = (Pet) JOptionPane.showInputDialog(this, "Select Pet:", "Pet",
                    JOptionPane.QUESTION_MESSAGE, null, manager.getPets().toArray(), null);
            Vet vet = (Vet) JOptionPane.showInputDialog(this, "Select Vet:", "Vet",
                    JOptionPane.QUESTION_MESSAGE, null, manager.getVets().toArray(), null);

            JTextField date = new JTextField(LocalDate.now().toString());
            JTextField service = new JTextField("Checkup");
            JTextField notes = new JTextField();
            Object[] fields = {
                    "Date (YYYY-MM-DD):", date,
                    "Service:", service,
                    "Notes:", notes
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Add Appointment", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    LocalDate d = LocalDate.parse(date.getText().trim());
                    manager.addAppointment(new Appointment(pet, vet, d, service.getText().trim(), notes.getText().trim()));
                    persist(); refreshAppointments();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                }
            }
        });

        edit.addActionListener(e -> {
            int row = apptTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment to update."); return; }
            Appointment existing = manager.getAppointments().get(row);

            Pet pet = (Pet) JOptionPane.showInputDialog(this, "Select Pet:", "Pet",
                    JOptionPane.QUESTION_MESSAGE, null, manager.getPets().toArray(), existing.getPet());
            Vet vet = (Vet) JOptionPane.showInputDialog(this, "Select Vet:", "Vet",
                    JOptionPane.QUESTION_MESSAGE, null, manager.getVets().toArray(), existing.getVet());

            JTextField date = new JTextField(existing.getDate().toString());
            JTextField service = new JTextField(existing.getService());
            JTextField notes = new JTextField(existing.getNotes());
            Object[] fields = {
                    "Date (YYYY-MM-DD):", date,
                    "Service:", service,
                    "Notes:", notes
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Update Appointment", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    LocalDate d = LocalDate.parse(date.getText().trim());
                    Appointment updated = new Appointment(pet, vet, d, service.getText().trim(), notes.getText().trim());
                    manager.updateAppointment(row, updated);
                    persist(); refreshAppointments();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                }
            }
        });

        del.addActionListener(e -> {
            int row = apptTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment to delete."); return; }
            manager.deleteAppointment(row);
            persist(); refreshAppointments();
        });

        return panel;
    }

    // -------- helpers ----------
    private void persist() { DataStorage.save(manager); }

    private void refreshPets() {
        petModel.setRowCount(0);
        for (Pet p : manager.getPets()) {
            petModel.addRow(new Object[]{ p.getName(), p.getSpecies(), p.getAge(), p.getOwnerName() });
        }
    }

    private void refreshVets() {
        vetModel.setRowCount(0);
        for (Vet v : manager.getVets()) {
            vetModel.addRow(new Object[]{ v.getName(), v.getSpecialization() });
        }
    }

    private void refreshAppointments() {
        apptModel.setRowCount(0);
        for (Appointment a : manager.getAppointments()) {
            apptModel.addRow(new Object[]{
                    a.getPet() == null ? "-" : a.getPet().toString(),
                    a.getVet() == null ? "-" : a.getVet().toString(),
                    a.getDate() == null ? "-" : a.getDate().toString(),
                    a.getService(),
                    a.getNotes()
            });
        }
    }

    private void refreshAll() {
        refreshPets();
        refreshVets();
        refreshAppointments();
    }
}