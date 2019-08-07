package group.msg.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Animal implements Serializable {
    @GeneratedValue
    @Id
    public int id;

    public String tip;
    public String nume;
    public int age;
    public String ownerName;

    public Animal() {
    }

    public Animal(int id, String tip, String nume, int age, String ownerName) {
        this.id = id;
        this.tip = tip;
        this.nume = nume;
        this.age = age;
        this.ownerName = ownerName;
    }
}
