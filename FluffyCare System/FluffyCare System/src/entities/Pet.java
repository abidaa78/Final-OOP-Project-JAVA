package entities;

import java.io.Serializable;
import java.util.UUID;

public class Pet implements Serializable {
    private String id;
    private String name;
    private String species;   // e.g., Dog, Cat
    private int age;          // in years
    private String ownerName; // simple owner info (kept inside Pet)

    public Pet(String name, String species, int age, String ownerName) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
    public String getOwnerName() { return ownerName; }

    public void setName(String name) { this.name = name; }
    public void setSpecies(String species) { this.species = species; }
    public void setAge(int age) { this.age = age; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    @Override
    public String toString() {
        return name + " (" + species + ")";
    }
}