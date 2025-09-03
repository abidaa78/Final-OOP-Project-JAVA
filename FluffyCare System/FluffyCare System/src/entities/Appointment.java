package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Appointment implements Serializable {
    private String id;
    private Pet pet;
    private Vet vet;
    private LocalDate date;
    private String service; // e.g., Vaccination, Checkup, Grooming
    private String notes;

    public Appointment(Pet pet, Vet vet, LocalDate date, String service, String notes) {
        this.id = UUID.randomUUID().toString();
        this.pet = pet;
        this.vet = vet;
        this.date = date;
        this.service = service;
        this.notes = notes;
    }

    public String getId() { return id; }
    public Pet getPet() { return pet; }
    public Vet getVet() { return vet; }
    public LocalDate getDate() { return date; }
    public String getService() { return service; }
    public String getNotes() { return notes; }

    public void setPet(Pet pet) { this.pet = pet; }
    public void setVet(Vet vet) { this.vet = vet; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setService(String service) { this.service = service; }
    public void setNotes(String notes) { this.notes = notes; }
}