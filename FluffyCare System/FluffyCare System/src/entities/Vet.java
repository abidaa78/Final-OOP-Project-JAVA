package entities;

import java.io.Serializable;
import java.util.UUID;

public class Vet implements Serializable {
    private String id;
    private String name;
    private String specialization; // e.g., Surgery, Dermatology, General

    public Vet(String name, String specialization) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.specialization = specialization;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }

    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return name + " - " + specialization;
    }
}