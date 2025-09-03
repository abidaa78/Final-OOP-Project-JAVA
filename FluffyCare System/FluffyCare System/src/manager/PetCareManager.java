package manager;

import entities.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetCareManager implements Serializable {
    private final List<Pet> pets = new ArrayList<>();
    private final List<Vet> vets = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();

    // PET
    public void addPet(Pet p) { pets.add(p); }
    public void updatePet(int index, Pet updated) { pets.set(index, updated); }
    public void deletePet(int index) { pets.remove(index); }
    public List<Pet> getPets() { return pets; }

    // VET
    public void addVet(Vet v) { vets.add(v); }
    public void updateVet(int index, Vet updated) { vets.set(index, updated); }
    public void deleteVet(int index) { vets.remove(index); }
    public List<Vet> getVets() { return vets; }

    // APPOINTMENT
    public void addAppointment(Appointment a) { appointments.add(a); }
    public void updateAppointment(int index, Appointment updated) { appointments.set(index, updated); }
    public void deleteAppointment(int index) { appointments.remove(index); }
    public List<Appointment> getAppointments() { return appointments; }
}