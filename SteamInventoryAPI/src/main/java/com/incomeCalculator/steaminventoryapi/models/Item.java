package com.incomeCalculator.steaminventoryapi.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "ITEMS")
public class Item {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    private Long classId;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) && Objects.equals(classId, item.classId) && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, classId, name);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", classId=" + classId +
                ", name='" + name + '\'' +
                '}';
    }
    
}
