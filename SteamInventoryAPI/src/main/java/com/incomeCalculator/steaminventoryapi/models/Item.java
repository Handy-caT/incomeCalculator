package com.incomeCalculator.steaminventoryapi.models;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "ITEMS")
@Table(indexes = {
        @Index(name = "classIdIndex", columnList = "classId"),
        @Index(name = "idx_item_classid", columnList = "classId")
})
public class Item {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    private Long classId;
    private String name;

    public Item(Long id, Long classId, String name) {
        this.id = id;
        this.classId = classId;
        this.name = name;
    }

    public Item(Long classId, String name) {
        this.classId = classId;
        this.name = name;
    }

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
